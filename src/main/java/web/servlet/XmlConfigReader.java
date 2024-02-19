package web.servlet;


import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class XmlConfigReader {
    public XmlConfigReader() {
    }

    public Map<String, MappingValue> loadConfig(@NotNull ClassPathXmlResource resource) {
        Map<String, MappingValue> mappingValueMap = new HashMap<>();
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String id = element.attributeValue("id");
            String clz = element.attributeValue("class");
            String method = element.attributeValue("method");
            mappingValueMap.put(id, new MappingValue(id, clz, method));
        }
        return mappingValueMap;
    }
}
