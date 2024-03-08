package zTest.aop;

import aop.MethodInterceptor;
import aop.MethodInvocation;

public class TracingIntercepter implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("method " + invocation.getMethod() + " is called on " + invocation.getThis() + " with args " + invocation.getArguments());
        Object res = invocation.proced();
        System.out.println("method " + invocation.getMethod() + " returns " + res);
        return res;
    }
}
