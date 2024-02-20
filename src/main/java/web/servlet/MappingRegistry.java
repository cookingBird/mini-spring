package web.servlet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingRegistry {
    private List<String> urlMappingNames = new ArrayList<>();
    private Map<String, Object> mappingObject = new HashMap<>();
    private Map<String, Method> mappingMethod = new HashMap<>();

    public List<String> getUrlMappingNames() {
        return urlMappingNames;
    }

    public void setUrlMappingNames(List<String> urlMappingNames) {
        this.urlMappingNames = urlMappingNames;
    }

    public Map<String, Object> getMappingObject() {
        return mappingObject;
    }

    public void setMappingObject(Map<String, Object> mappingObject) {
        this.mappingObject = mappingObject;
    }

    public Map<String, Method> getMappingMethod() {
        return mappingMethod;
    }

    public void setMappingMethod(Map<String, Method> mappingMethod) {
        this.mappingMethod = mappingMethod;
    }
}
