package beans.factory.annotation;

import beans.BeansException;
import beans.factory.BeanFactory;
import beans.factory.annotation.Autowired;
import beans.factory.config.BeanPostProcessor;
import beans.factory.support.AbstractBeanFactory;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        Class<?> clz = bean.getClass();
        Field[] fields = clz.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if (isAutowired) {
                    String fieldName = field.getName();
                    Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                    field.setAccessible(true);
                    try {
                        field.set(result, autowiredObj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    System.out.println("autowire " + fieldName + " for bean " + beanName);
                }
            }
        }
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
