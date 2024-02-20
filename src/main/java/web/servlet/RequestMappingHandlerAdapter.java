package web.servlet;

import web.bind.WebDataBinder;
import web.bind.WebDataBinderFactory;
import web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
    WebApplicationContext webApplicationContext;

    public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        WebDataBinderFactory webDataBinderFactory = new WebDataBinderFactory();
        Parameter[] parameters = handler.getMethod().getParameters();
        Object[] paramsObjects = new Object[parameters.length];
        int i = 0;
        for (Parameter parameter : parameters) {
            try {
                Object paramObj = parameter.getType().newInstance();
                WebDataBinder webDataBinder = webDataBinderFactory.createBinder(
                        request,
                        paramObj,
                        parameter.getName()
                );
                webDataBinder.bind(request);
                paramsObjects[i] = paramObj;
                i++;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Method method = handler.getMethod();
        Object ctx = handler.getBean();
        Object result = null;
        try {
            result = method.invoke(ctx, paramsObjects);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            response.getWriter().write(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
