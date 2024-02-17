package beans.factory;

import beans.BeansException;
import beans.factory.config.BeanDefinition;
import beans.factory.support.AbstractAutowireCapableBeanFactory;

import java.util.*;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {

    @Override
    public int getBeanDefinitionCount() {
        return super.getBeanDefinitions().size();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List result = new ArrayList<>();
        for (String beanName : this.getBeanDefinitionNames()) {
            boolean matchFound = false;
            BeanDefinition mbd = this.getBeanDefinition(beanName);
            Class classToMatch = mbd.getClass();
            if (type.isAssignableFrom(classToMatch)) {
                matchFound = true;
            } else {
                matchFound = false;
            }
            if (matchFound) {
                result.add(beanName);
            }
        }
        return (String[]) result.toArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> getBeansOfType(Class<?> type) throws BeansException {
        String[] beanNames = getBeanNamesForType(type);
        Map<String, T> result = new LinkedHashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            Object beanInstance = getBean(beanName);
            result.put(beanName, (T) beanInstance);
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        return applyBeanPostProcessorsBeforeInitialization(existingBean, beanName);
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        return applyBeanPostProcessorAfterInitialization(existingBean, beanName);
    }


    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {
        String[] dependsOn = super.getBeanDefinition(beanName).getDependsOn();
        dependsOn[dependsOn.length] = dependentBeanName;
    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return super.getBeanDefinition(beanName).getDependsOn();
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        return new String[0];
    }
}
