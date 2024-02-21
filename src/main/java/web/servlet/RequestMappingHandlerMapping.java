package web.servlet;

import web.bind.annotation.RequestMapping;
import web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class RequestMappingHandlerMapping implements HandlerMapping {

    WebApplicationContext webApplicationContext;
    private final MappingRegistry registry = new MappingRegistry();

    public RequestMappingHandlerMapping(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        this.initMapping();
    }

    private void initMapping() {
        Class<?> clz = null;
        Object object = null;
        Method[] methods = null;
        String[] controllerNames = this.webApplicationContext.getBeanDefinitionNames().toArray(new String[0]);
        for (String name : controllerNames) {
            try {
                clz = Class.forName(name);
                object = this.webApplicationContext.getBean(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            methods = clz.getDeclaredMethods();
            if (methods != null) {
                for (Method method : methods) {
                    boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                    if (isRequestMapping) {
                        String url = method.getAnnotation(RequestMapping.class).value();
                        this.registry.getUrlMappingNames().add(url);
                        this.registry.getMappingMethod().put(url, method);
                        this.registry.getMappingObject().put(url, object);
                    }
                }
            }
        }
    }

    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        String url = request.getServletPath();
        if (!this.registry.getUrlMappingNames().contains(url)) {
            return null;
        }
        Method method = this.registry.getMappingMethod().get(url);
        Object object = this.registry.getMappingObject().get(url);
        return new HandlerMethod(method, object);
    }
}
