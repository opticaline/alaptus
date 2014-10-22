package org.opticaline.ssh;

import com.jcraft.jsch.*;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by neusoft on 2014/9/28.
 */
public class Batch {
    private String settings;
    private XMLConfiguration config;

    public Batch(String settings) {
        this.settings = settings;
        this.openSettings();
    }

    private void openSettings() {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<>(XMLConfiguration.class)
                        .configure(params.xml()
                                .setFileName(this.settings)
                                .setSchemaValidation(true));
        try {
            this.config = builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private Channel publicChannel() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(
                this.config.getString("login.public[@username]"),
                this.config.getString("login.public[@ip]"),
                this.config.getInt("login.public[@port]"));
        session.setProxy(new ProxySOCKS5("127.0.0.1", 18));
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword(this.config.getString("login.public[@password]"));
        session.connect(30000);
        Channel channel = session.openChannel("shell");
        channel.connect(3000);
        return channel;
    }

    private Channel forward(Channel channel, String ip, String username, String password) throws IOException {
        OutputStream os = channel.getOutputStream();
        InputStream is = channel.getInputStream();
        byte[] bytes = new byte[1024];
        while (is.read(bytes) != -1) {
            String line = new String(bytes);
            //System.out.println(line);
            if (line.contains("password")) {
                os.write((password + "\r\n").getBytes());
                os.flush();
            } else if (line.contains("Last login")) {
                break;
            } else if (line.contains("[root@logs12 ~]#")) {
                os.write(("ssh " + username + "@" + ip + "\r\n").getBytes());
                os.flush();
            }
        }
        return channel;
    }

    public void run() throws JSchException, IOException {
        List<Object> ips = config.getList("login.forwards.forward[@ip]");
        List<Object> usernames = config.getList("login.forwards.forward[@username]");
        List<Object> passwords = config.getList("login.forwards.forward[@password]");
        List<Object> commands = config.getList("missions.mission[@command]");
        List<Object> missionPwds = config.getList("missions.mission[@password]");
        for (int i = 0; i < ips.size(); i++) {
            final Channel channel = forward(publicChannel(), (String) ips.get(i),
                    (String) usernames.get(i),
                    (String) passwords.get(i));
            new Thread(new Mission(channel, commands, missionPwds, (String) passwords.get(i), i)).start();
        }
    }

    class Mission implements Runnable {
        private Channel channel;
        private List<Object> commands;
        private List<Object> passwords;
        private String password;
        private int index;

        public Mission(Channel channel, List<Object> commands, List<Object> passwords,
                       String password, int index) {
            this.channel = channel;
            this.commands = commands;
            this.passwords = passwords;
            this.password = password;
            this.index = index;
        }

        @Override
        public void run() {
            try {
                OutputStream os = this.channel.getOutputStream();
                InputStream is = this.channel.getInputStream();
                byte[] bytes = new byte[1024];
                int i = 0;
                String current = "";
                boolean close = false;
                System.out.println("run");
                while (is.read(bytes) != -1) {
                    String line = new String(bytes);
                    if (line.contains("password")) {
                        os.write(current.getBytes());
                        os.flush();
                        close = true;
                    } else if (line.contains("[root@")) {
                        if(i >= commands.size())
                            continue;
                        String command = commands.get(i).toString() + "\r\n";
                        command = command.replaceAll("\\{length\\}", String.valueOf(index));
                        os.write(command.getBytes());
                        os.flush();
                        current = passwords.get(0).toString();
                        if (current == null || current.length() == 0) {
                            current = password;
                        }
                        current += "\r\n";
                        i++;
                    } else if(close){
                        is.close();
                        os.close();
                        channel.disconnect();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
