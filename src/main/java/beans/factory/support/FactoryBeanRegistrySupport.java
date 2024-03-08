package beans.factory.support;

import beans.factory.FactoryBean;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {
    protected Class<?> getTypeForFactoryBean(FactoryBean<?> factoryBean) throws Exception {

        Class<?> type = factoryBean.getObjectType();

        if (type != null && !factoryBean.isSingleton()) {
            return null;
        }

        return type;
    }

    protected Object getObjectFromFactoryBean(FactoryBean<?> factoryBean, String beanName) throws Exception {
        Object object = doGetObjectFromFactoryBean(factoryBean, beanName);
        return object;

    }

    private Object doGetObjectFromFactoryBean(FactoryBean<?> factoryBean, String beanName) {
        Object object = null;
        try {
            object = factoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
