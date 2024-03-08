package aop.support;

import aop.Advisor;
import aop.AopProxy;
import aop.AopProxyFactory;
import aop.PointcutAdvisor;

public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createProxy(Object target, PointcutAdvisor advisor) {
        return new JdkDynamicAopProxy(target, advisor);
    }
}
