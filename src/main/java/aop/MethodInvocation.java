package aop;

import java.lang.reflect.Method;

public interface MethodInvocation {
    Method getMethod();

    Object[] getArguments();

    Object getThis();

    Object proced() throws Throwable;
}
