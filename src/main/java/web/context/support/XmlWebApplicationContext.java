package web.context.support;

import context.ClasspathXmlApplicationContext;
import web.context.WebApplicationContext;

import javax.servlet.ServletContext;

public class XmlWebApplicationContext extends ClasspathXmlApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;

    public XmlWebApplicationContext(String fileName) {
        super(fileName);
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
