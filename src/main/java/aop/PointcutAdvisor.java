package aop;

public interface PointcutAdvisor extends Advisor {
    PointCut getPointCut(); //返回PointCut对象
}
