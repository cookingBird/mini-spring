package beans.factory;

import beans.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;

    boolean containsBean(String beanName);

    boolean isSingleton(String beanName);

    boolean iPrototype(String beanName);

    Class<?> getType(String name);
}
