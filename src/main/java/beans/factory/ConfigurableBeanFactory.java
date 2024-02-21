package beans.factory;

import beans.factory.config.BeanPostProcessor;
import beans.factory.config.SingletonBeanRegistry;

public interface ConfigurableBeanFactory extends SingletonBeanRegistry, BeanFactory {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    int getBeanPostProcessorCount();

    void registerDependentBean(String beanName, String dependentBeanName);

    String[] getDependentBeans(String beanName);

    String[] getDependenciesForBean(String beanName);
}
