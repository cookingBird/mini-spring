package context;

import beans.factory.config.BeanPostProcessor;
import beans.BeansException;
import beans.factory.BeanFactory;
import beans.factory.ConfigurableBeanFactory;
import beans.factory.ConfigurableListableBeanFactory;
import beans.factory.ListableBeanFactory;
import beans.factory.event.ApplicationEventPublisher;
import core.env.Environment;
import core.env.EnvironmentCapable;

public interface ApplicationContext extends BeanFactory, ListableBeanFactory, ConfigurableBeanFactory, EnvironmentCapable, ApplicationEventPublisher {
    String getApplicationName();

    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    void setEnvironment(Environment environment);

    Environment getEnvironment();

    void addBeanFactoryPostProcessor(BeanPostProcessor postProcessor);

    void refresh() throws BeansException, IllegalStateException;

    void close();

    boolean isActive();
}
