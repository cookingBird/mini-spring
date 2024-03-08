package aop;

public class NameMatchMethodPointcutAdvisor implements PointcutAdvisor {

    private Advice advice;
    private MethodInterceptor methodInterceptor;
    private String mappedName;
    private final NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();

    public NameMatchMethodPointcutAdvisor() {

    }

    public NameMatchMethodPointcutAdvisor(Advice advice) {
        this.advice = advice;
        this.setAdvice(advice);
    }

    public Advice getAdvice() {
        return this.advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
        MethodInterceptor methodInterceptor1 = null;
        if (advice instanceof BeforeAdvice) {
            methodInterceptor1 = new MethodBeforeAdviceInterceptor((MethodBeforeAdvice) advice);
        } else if (advice instanceof AfterAdvice) {
            methodInterceptor1 = new MethodReturningAdviceInterceptor((AfterReturningAdvice) advice);
        } else if (advice instanceof MethodInterceptor) {
            methodInterceptor1 = (MethodInterceptor) advice;
        }
        this.setMethodInterceptor(methodInterceptor1);
    }

    @Override
    public MethodInterceptor getMethodInterceptor() {
        return this.methodInterceptor;
    }

    @Override
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    @Override
    public PointCut getPointCut() {
        return null;
    }

    public String getMappedName() {
        return this.mappedName;
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
        this.pointcut.setMappedName(mappedName);
    }
}
