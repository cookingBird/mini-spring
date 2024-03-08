package aop;

public class MethodReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice {
    private final AfterReturningAdvice advice;

    public MethodReturningAdviceInterceptor(AfterReturningAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object res = invocation.proced();
        this.advice.afterReturning(res, invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return res;
    }
}
