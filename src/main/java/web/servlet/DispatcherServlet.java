package web.servlet;

import beans.BeansException;
import web.context.WebApplicationContext;
import web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
    public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";
    private List<String> packageNames = new ArrayList<>();
    private List<String> controllerNames = new ArrayList<>();
    private Map<String, Object> controllerObjs = new HashMap<>();
    private Map<String, Class<?>> controllerClass = new HashMap<>();

    private WebApplicationContext webApplicationContext;
    private WebApplicationContext parentApplicationContext;

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;


    private String configLocation;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.parentApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        configLocation = config.getInitParameter("contextConfigLocation");
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(configLocation, this.parentApplicationContext);
        refresh();
    }

    private void refresh() {
        this.initHandlerMapping(this.webApplicationContext);
        this.initHandlerAdapter(this.webApplicationContext);
        this.initViewResolver(this.webApplicationContext);
    }

    private void initHandlerMapping(WebApplicationContext webApplicationContext) {
        this.handlerMapping = new RequestMappingHandlerMapping(webApplicationContext);
    }

    private void initHandlerAdapter(WebApplicationContext webApplicationContext) {
        try {
            this.handlerAdapter = (HandlerAdapter) webApplicationContext.getBean(HANDLER_ADAPTER_BEAN_NAME);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = null;
        HttpServletRequest processedRequest = request;
        HandlerMethod method = this.handlerMapping.getHandler(request);
        String path = request.getServletPath();
        if (method == null) {
            return;
        }
        HandlerAdapter handlerAdapter = this.handlerAdapter;
        modelAndView = handlerAdapter.handle(request, response, method);
        this.render(request, response, modelAndView);
    }

    protected void render(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }
        String sTarget = modelAndView.getViewName();
        Map<String, Object> modelMap = modelAndView.getModel();
        View view = resolveViewName(sTarget, modelMap);
        if (view != null) {
            view.render(modelMap, request, response);
        }
    }

    protected View resolveViewName(String viewName, Map<String, Object> viewData) {
        View view = null;
        if (this.viewResolver != null) {
            try {
                view = viewResolver.resolveViewName(viewName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    protected void initViewResolver(WebApplicationContext webApplicationContext) {
        try {
            this.viewResolver = (ViewResolver) webApplicationContext.getBean(VIEW_RESOLVER_BEAN_NAME);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

}
