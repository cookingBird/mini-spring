package web.context.support;

import beans.BeansException;
import beans.factory.ConfigurableListableBeanFactory;
import beans.factory.DefaultListableBeanFactory;
import beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import beans.factory.config.BeanDefinition;
import beans.factory.event.ApplicationEvent;
import beans.factory.event.ApplicationListener;
import beans.factory.event.DefaultApplicationEventPublisher;
import context.AbstractApplicationContext;
import web.context.WebApplicationContext;
import web.helper.XmlScanComponentHelper;
import web.servlet.ClassPathXmlResource;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;
    private WebApplicationContext parentApplicationContext;

    private DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(String fileName) {
        this(fileName, null);
    }

    public AnnotationConfigWebApplicationContext(String fileName, WebApplicationContext parentApplicationContext) {
        this.beanFactory = new DefaultListableBeanFactory();
        this.parentApplicationContext = parentApplicationContext;
        this.beanFactory.setParentBeanFactory(this.parentApplicationContext.getBeanFactory());
        this.servletContext = parentApplicationContext.getServletContext();
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(fileName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        List<String> packageNames = XmlScanComponentHelper.getNodeValue(new ClassPathXmlResource(xmlPath));
        List<String> controllerNames = this.scanPackages(packageNames);
        this.loadBeanDefinitions(controllerNames);
        if (true) {
            try {
                refresh();
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<String> scanPackages(List<String> packages) {
        List<String> mapControllerNames = new ArrayList<>();
        for (String packName : packages) {
            mapControllerNames.addAll(scanPackage(packName));
        }
        return mapControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempNames = new ArrayList<>();
        URI uri = null;
        try {
            uri = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).toURI();
            if (uri == null) return tempNames;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File dir = new File(uri);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                tempNames.addAll(scanPackage(packageName + "." + file.getName()));
            } else {
                String controllerName = packageName
                        + "."
                        + file.getName().replace(".class", "");
                tempNames.add(controllerName);
            }
        }
        return tempNames;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    protected void initApplicationEventPublisher() {
        this.setApplicationEventPublisher(new DefaultApplicationEventPublisher());
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }


    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor(bf));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

    }


    @Override
    protected void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    protected void finishRefresh() {

    }

    private void loadBeanDefinitions(List<String> controllerNames) {
        for (String controller : controllerNames) {
            String id = controller;
            String beanClassName = controller;
            BeanDefinition beanDefinition = new BeanDefinition(id, beanClassName);
            this.beanFactory.registryBeanDefinition(id, beanDefinition);
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object result = super.getBean(beanName);
        if (result == null) {
            result = this.parentApplicationContext.getBean(beanName);
        }
        return result;
    }

    public void setParent(WebApplicationContext parentApplicationContext) {
        this.parentApplicationContext = parentApplicationContext;
        this.beanFactory.setParentBeanFactory(this.parentApplicationContext.getBeanFactory());
    }
}
