package aop.framework.autoproxy;

import aop.AopProxyFactory;
import aop.PointcutAdvisor;
import aop.ProxyFactoryBean;
import aop.support.DefaultAopProxyFactory;
import beans.BeansException;
import beans.factory.BeanFactory;
import beans.factory.config.BeanPostProcessor;
import util.PatternMatchUtils;

public class BeanNameAutoProxyCreator implements BeanPostProcessor {
    String pattern;
    private BeanFactory beanFactory;
    private AopProxyFactory aopProxyFactory;
    private String interceptorName;
    private PointcutAdvisor advisor;

    public BeanNameAutoProxyCreator() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(this.isMatch(beanName,this.pattern)){
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setTarget(bean);
            proxyFactoryBean.setBeanFactory(beanFactory);
            proxyFactoryBean.setInterceptorName(interceptorName);
            proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
            return proxyFactoryBean;
        }
        return bean;
    }

    protected boolean isMatch(String beanName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
