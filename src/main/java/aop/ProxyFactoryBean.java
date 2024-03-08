package aop;

import aop.support.DefaultAdvisor;
import aop.support.DefaultAopProxyFactory;
import beans.BeansException;
import beans.factory.BeanFactory;
import beans.factory.FactoryBean;

public class ProxyFactoryBean implements FactoryBean<Object> {
    private String interceptorName;
    private PointcutAdvisor advisor;
    private BeanFactory beanFactory;

    private AopProxyFactory aopProxyFactory;

    private Object target;

    private Object singletonInstance;

    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getSingletonInstance() {
        return singletonInstance;
    }

    public void setSingletonInstance(Object singletonInstance) {
        this.singletonInstance = singletonInstance;
    }

    public String getInterceptorName() {
        if(this.singletonInstance == null){
            this.singletonInstance = this.createAopProxy().getProxy();
        }
        return interceptorName;
    }

    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }

    public PointcutAdvisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(PointcutAdvisor advisor) {
        this.advisor = advisor;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    private synchronized void initializeAdvisor() {
        Object advice = null;
        try {
            advice = this.beanFactory.getBean(this.interceptorName);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        this.advisor = (PointcutAdvisor) advice;
    }

    protected AopProxyFactory getAopProxyFactory() {
        return this.aopProxyFactory;
    }

    protected AopProxy createAopProxy() {
        return this.getAopProxyFactory().createProxy(target, this.advisor);
    }

    @Override
    public Object getObject() throws Exception {
        initializeAdvisor();
        return this.getSingletonInstance();
    }

    @Override
    public Class<?> getObjectType() throws Exception {
        return null;
    }
}
