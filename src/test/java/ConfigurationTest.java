import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Nathan on 2014/9/27.
 */
public class ConfigurationTest {
    public static void main(String[] args) throws ConfigurationException, ParserConfigurationException, IOException, SAXException {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<>(XMLConfiguration.class)
                        .configure(params.xml()
                                .setFileName("config.xml")
                                .setSchemaValidation(true));


// This will throw a ConfigurationException if the XML document does not
// conform to its DTD.
        XMLConfiguration config = builder.getConfiguration();
        System.out.println(config.getList("login.forwards.forward[@ip]"));
        /*String forwards[] = (String[]) config.getArray(String.class, "login.forwards.forward");
        for (String s : forwards) {
            System.out.println(s);
        }*/
    }
}
