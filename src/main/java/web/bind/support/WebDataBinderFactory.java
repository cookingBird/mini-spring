package web.bind.support;

import web.bind.WebDataBinder;

import javax.servlet.http.HttpServletRequest;

public class WebDataBinderFactory {
    public WebDataBinder createBinder(HttpServletRequest request, Object target, String objectName) {
        WebDataBinder binder = new WebDataBinder(target, objectName);
        initBinder(binder, request);
        return binder;
    }

    protected void initBinder(WebDataBinder dataBinder, HttpServletRequest request) {
    }
}
