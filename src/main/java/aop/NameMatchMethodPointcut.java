package aop;

import util.PatternMatchUtils;

import java.lang.reflect.Method;

public class NameMatchMethodPointcut implements MethodMatcher, PointCut {
    private String mappedName = "";

    public NameMatchMethodPointcut() {

    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName);
    }

    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return null;
    }
}
