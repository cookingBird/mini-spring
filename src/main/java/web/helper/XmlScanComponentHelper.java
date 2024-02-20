package web.helper;

import org.dom4j.Element;
import web.servlet.ClassPathXmlResource;

import java.util.ArrayList;
import java.util.List;

public class XmlScanComponentHelper {
    public XmlScanComponentHelper() {
    }

    public static List<String> getNodeValue(ClassPathXmlResource resource) {
        List<String> packages = new ArrayList<>();
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            packages.add(element.attributeValue("base-package"));
        }
        return packages;
    }
}
