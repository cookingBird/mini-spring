package beans.factory;

import beans.BeansException;

import java.util.List;
import java.util.Map;

public interface ListableBeanFactory extends BeanFactory {
    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

    List<String> getBeanDefinitionNames();

    String[] getBeanNamesForType(Class<?> type);

    <T> Map<String, T> getBeansOfType(Class<?> type) throws BeansException;
}
