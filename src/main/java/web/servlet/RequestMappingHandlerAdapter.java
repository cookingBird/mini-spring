package web.servlet;

import web.bind.annotation.PathVariable;
import web.bind.annotation.ResponseBody;
import web.bind.HttpMessageConverter;
import web.bind.WebDataBinder;
import web.bind.support.WebBindingInitializer;
import web.bind.support.WebDataBinderFactory;
import web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
    WebApplicationContext webApplicationContext;

    HttpMessageConverter httpMessageConverter;
    WebBindingInitializer webBindingInitializer;

    public HttpMessageConverter getHttpMessageConverter() {
        return httpMessageConverter;
    }

    public void setHttpMessageConverter(HttpMessageConverter httpMessageConverter) {
        this.httpMessageConverter = httpMessageConverter;
    }

    public WebBindingInitializer getWebBindingInitializer() {
        return webBindingInitializer;
    }

    public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }

    public RequestMappingHandlerAdapter() {
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.handleInternal(request, response, (HandlerMethod) handler);
    }

    private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod method) {
        ModelAndView modelAndView = null;
        try {
            modelAndView = this.invokeHandlerMethod(request, response, method);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        WebDataBinderFactory webDataBinderFactory = new WebDataBinderFactory();
        Parameter[] parameters = handlerMethod.getMethod().getParameters();
        Object[] paramsObjects = new Object[parameters.length];
        int i = 0;
        for (Parameter parameter : parameters) {
            Class<?> parameterClz = parameter.getType();
            if (parameterClz == HttpServletRequest.class) {
                paramsObjects[i] = request;
            } else if (parameterClz == HttpServletResponse.class) {
                paramsObjects[i] = response;

            } else if (parameter.isAnnotationPresent(PathVariable.class)) {
                String sServletPath = request.getServletPath();
                int index = sServletPath.lastIndexOf("/");
                String sParam = sServletPath.substring(index + 1);
                if (int.class.isAssignableFrom(parameter.getType())) {
                    paramsObjects[i] = Integer.parseInt(sParam);
                } else if (String.class.isAssignableFrom(parameter.getType())) {
                    paramsObjects[i] = sParam;
                }

            } else if (parameterClz != HttpServletRequest.class && parameterClz != HttpServletResponse.class) {
                Object paramObj = parameter.getType().newInstance();
                WebDataBinder webDataBinder = webDataBinderFactory.createBinder(
                        request,
                        paramObj,
                        parameter.getName()
                );
                webBindingInitializer.initBinder(webDataBinder);
                webDataBinder.bind(request);
                paramsObjects[i] = paramObj;
            }
            i++;
        }
        Method method = handlerMethod.getMethod();
        Object ctx = handlerMethod.getBean();
        Object result = method.invoke(ctx, paramsObjects);
        Class<?> returnType = method.getReturnType();

        ModelAndView modelAndView = null;
        try {
            if (method.isAnnotationPresent(ResponseBody.class)) {
                this.httpMessageConverter.write(result, response);
            } else if (void.class == returnType) {

            } else {
                if (result instanceof ModelAndView) {
                    modelAndView = (ModelAndView) result;
                } else if (result instanceof String) {
                    String sTarget = (String) result;
                    modelAndView = new ModelAndView();
                    modelAndView.setViewName(sTarget);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }
}
