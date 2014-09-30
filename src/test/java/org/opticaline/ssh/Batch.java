package org.opticaline.ssh;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by neusoft on 2014/9/28.
 */
public class Batch {

    public static void run(String settings) throws JSchException, IOException {


        //---
        JSch jsch = new JSch();
        Session session = jsch.getSession("root", "221.179.140.185", 33021);
        session.setProxy(new ProxySOCKS5("127.0.0.1", 18));
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword("UARbj#2014");
        session.connect();

        Channel channel = session.openChannel("shell");
        InputStream is = channel.getInputStream();
        OutputStream os = channel.getOutputStream();
        channel.connect();
        byte[] bytes = new byte[1024];
        while (is.read(bytes) != -1) {
            String line = new String(bytes);
            System.out.println(line);
            if (line.indexOf("password") > -1) {
                os.write("#cmccnsn@uar#2014".getBytes());
            } else {
                os.write("ls".getBytes());
            }
            os.flush();
        }
    }
}
