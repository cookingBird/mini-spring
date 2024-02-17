package context;

import beans.factory.config.BeanPostProcessor;
import beans.BeansException;
import beans.factory.ConfigurableListableBeanFactory;
import beans.factory.event.ApplicationEvent;
import beans.factory.event.ApplicationListener;
import core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractApplicationContext implements ApplicationContext {
    private Environment environment;
    private final List<BeanPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
    private long startupDate;
    private final AtomicBoolean active = new AtomicBoolean();
    private final AtomicBoolean closed = new AtomicBoolean();

    @Override
    public Object getBean(String beanName) throws BeansException {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public boolean containsBean(String beanName) {
        return getBeanFactory().containsBean(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return getBeanFactory().isSingleton(beanName);
    }

    @Override
    public boolean iPrototype(String beanName) {
        return getBeanFactory().iPrototype(beanName);
    }

    @Override
    public Class<?> getType(String name) {
        return getBeanFactory().getType(name);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        getBeanFactory().addBeanPostProcessor(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return getBeanFactory().getBeanPostProcessorCount();
    }

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {
        getBeanFactory().registerDependentBean(beanName, dependentBeanName);
    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return getBeanFactory().getDependentBeans(beanName);
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        return getBeanFactory().getDependenciesForBean(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public List<String> getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<?> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public void registerSingleton(String beaName, Object singletonObj) {
        getBeanFactory().registerSingleton(beaName, singletonObj);
    }

    @Override
    public Object getSingleton(String beanName) {
        return getBeanFactory().getSingleton(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return getBeanFactory().containsSingleton(beanName);
    }

    @Override
    public String[] getSingletonsName() {
        return getBeanFactory().getSingletonsName();
    }

    @Override
    public void removeSingleton(String beanName) {
        getBeanFactory().removeSingleton(beanName);
    }


    @Override
    public String getApplicationName() {
        return null;
    }

    @Override
    public long getStartupDate() {
        return startupDate;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanPostProcessor postProcessor) {
        getBeanFactory().addBeanPostProcessor(postProcessor);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void refresh() throws BeansException, IllegalStateException {
        postProcessBeanFactory(getBeanFactory());

        registerBeanPostProcessors(getBeanFactory());

        initApplicationEventPublisher();

        onRefresh();

        registerListeners();

        finishRefresh();
    }

    @Override
    abstract public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    @Override
    abstract public void publishEvent(ApplicationEvent event);

    @Override
    abstract public void addApplicationListener(ApplicationListener listener);

    public abstract void registerListeners();

    public abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory bf);

    public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory bf);

    abstract void initApplicationEventPublisher();

    abstract protected void onRefresh();

    abstract void finishRefresh();
}
