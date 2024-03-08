package aop.support;

import aop.Advisor;
import aop.MethodInterceptor;

public class DefaultAdvisor implements Advisor {
    MethodInterceptor methodInterceptor;

    @Override
    public MethodInterceptor getMethodInterceptor() {
        return this.methodInterceptor;
    }

    @Override
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }
}
