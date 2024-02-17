package beans.factory.support;

import beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import beans.factory.config.BeanPostProcessor;
import beans.BeansException;
import beans.factory.AutowireCapableBeanFactory;
import beans.factory.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory, ConfigurableBeanFactory {
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public Object applyBeanPostProcessorBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        for (BeanPostProcessor beanPostProcessor : this.getBeanPostProcessors()) {
            ((AutowiredAnnotationBeanPostProcessor) beanPostProcessor).setBeanFactory(this);
            result = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorAfterInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        for (BeanPostProcessor beanPostProcessor : this.getBeanPostProcessors()) {
            ((AutowiredAnnotationBeanPostProcessor) beanPostProcessor).setBeanFactory(this);
            result = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }
}
