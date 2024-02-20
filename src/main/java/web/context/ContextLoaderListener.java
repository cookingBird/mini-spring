package web.context;

import web.context.support.AnnotationConfigWebApplicationContext;
import web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {
    private WebApplicationContext context;
    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    public ContextLoaderListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initWebApplicationContext(sce.getServletContext());
    }

    private void initWebApplicationContext(ServletContext servletContext) {
        String contextLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        if (contextLocation != null) {
            WebApplicationContext wac = new XmlWebApplicationContext(contextLocation);
            wac.setServletContext(servletContext);
            this.context = wac;
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
