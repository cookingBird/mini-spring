package context;

import beans.*;
import beans.factory.ConfigurableListableBeanFactory;
import beans.factory.DefaultListableBeanFactory;
import beans.factory.config.BeanDefinition;
import beans.factory.config.BeanFactoryPostProcessor;
import beans.factory.config.BeanPostProcessor;
import beans.factory.config.Resource;
import beans.factory.event.*;
import beans.factory.xml.XmlBeanDefinitionReader;

import java.util.ArrayList;
import java.util.List;

public class ClasspathXmlApplicationContext extends AbstractApplicationContext implements ApplicationContext {
    private DefaultListableBeanFactory beanFactory;
    private DefaultApplicationEventPublisher applicationEventPublisher;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors =
            new ArrayList<BeanFactoryPostProcessor>();

    public ClasspathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClasspathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if (isRefresh) {
            try {
                this.refresh();
            } catch (BeansException e) {
                e.printStackTrace();
            }
            this.finishRefresh();
        }
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.applicationEventPublisher.addApplicationListener(listener);
    }

    @Override
    public void registerListeners() {
        String[] beanNames = this.getBeanFactory().getBeanDefinitionNames().toArray(new String[0]);
        for (String name : beanNames) {
            Object bean = null;
            try {
                bean = this.getBeanFactory().getBean(name);
            } catch (BeansException e) {
                e.printStackTrace();
            }
            if (bean instanceof ApplicationListener) {
                this.applicationEventPublisher.addApplicationListener((ApplicationListener) bean);
            }
        }
    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        System.out.println("try to registerBeanPostProcessors");
        String[] bdNames = this.beanFactory.getBeanDefinitionNames().toArray(new String[0]);
        for (String bdName : bdNames) {
            BeanDefinition bd = this.beanFactory.getBeanDefinition(bdName);
            String clzName = bd.getClassName();
            Class<?> clz = null;
            try {
                clz = Class.forName(clzName);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            if (BeanPostProcessor.class.isAssignableFrom(clz)) {
                System.out.println(" registerBeanPostProcessors : " + clzName);
                try {
                    //this.beanFactory.addBeanPostProcessor((BeanPostProcessor) clz.newInstance());
                    this.beanFactory.addBeanPostProcessor((BeanPostProcessor) (this.beanFactory.getBean(bdName)));
                } catch (BeansException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
        String[] beanNames = this.beanFactory.getBeanDefinitionNames().toArray(new String[0]);
        for (String bName : beanNames) {
            BeanDefinition bd = this.beanFactory.getBeanDefinition(bName);
            String className = bd.getClassName();
            Class<?> clz = null;
            try {
                clz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (BeanFactoryPostProcessor.class.isAssignableFrom(clz)) {
                try {
                    this.beanFactoryPostProcessors.add((BeanFactoryPostProcessor) clz.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        for (BeanFactoryPostProcessor beanPostProcessor : this.beanFactoryPostProcessors) {
            try {
                beanPostProcessor.postProcessBeanFactory(bf);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }


    @Override
    void initApplicationEventPublisher() {
        if (this.applicationEventPublisher == null) {
            this.applicationEventPublisher = new DefaultApplicationEventPublisher();
        }
    }

    @Override
    protected void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    void finishRefresh() {
        publishEvent(new ContextRefreshEvent(this));
    }

    @Override
    public String toString() {
        return "this is ClasspathXmlApplicationContext";
    }
}
