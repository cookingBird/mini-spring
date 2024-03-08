package aop;

public interface AopProxyFactory {
    AopProxy createProxy(Object target, PointcutAdvisor advisor);
}
