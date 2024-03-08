package zTest.aop;

public class RealSubject implements ISubject {
    @Override
    public String doAction(String name) {
        System.out.println("real subject: " + name);
        return "SUCCESS";
    }
}
