package web.helper;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import web.servlet.ClassPathXmlResource;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlScanComponentHelper {
    public XmlScanComponentHelper() {
    }

    public List<String> getNodeValue(ClassPathXmlResource resource) {
        List<String> packages = new ArrayList<>();
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            packages.add(element.attributeValue("base-package"));
        }
        return packages;
    }
}
