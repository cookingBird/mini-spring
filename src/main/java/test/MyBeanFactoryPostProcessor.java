package test;

import beans.BeansException;
import beans.factory.BeanFactory;
import beans.factory.config.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println(".........MyBeanFactoryPostProcessor...........");
    }
}
