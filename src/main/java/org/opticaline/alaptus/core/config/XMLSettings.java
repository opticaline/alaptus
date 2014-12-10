package org.opticaline.alaptus.core.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nathan on 14-8-25.
 */
public class XMLSettings {
    private String[] paths;
    private Map<String, Object> settings;
    private SAXReader reader = new SAXReader();
    private List<Element> elements = new ArrayList<Element>();

    public void setXMLPath(String... paths) {
        this.paths = paths;
    }

    public String[] getXMLPath() {
        return this.paths;
    }

    public void load() throws DocumentException {
        for (String string : paths) {
            this.load(string);
        }
    }

    public void load(String path) throws DocumentException {
        if (settings == null) {
            settings = new HashMap<String, Object>();
        }
        Document document = reader.read(path);
        Element root = document.getRootElement();
        elements.add(root);
    }

    public void reset() {
        elements = new ArrayList<Element>();
    }

    public String getProperty(String string) {
        for (Element element : elements) {
            Element result = element.element(string);
            if (result != null) {
                return result.getStringValue();
            }
        }
        return null;
    }

    public String[] getPropertyValues(String string) {
        return null;
    }

}
