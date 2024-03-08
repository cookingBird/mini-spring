package zTest.aop;

public class ProxySubject implements ISubject {
    private ISubject realSubject;

    public ProxySubject() {
        this.realSubject = new RealSubject();
    }

    @Override
    public String doAction(String name) {
        System.out.println("proxy subject");
        return realSubject.doAction(name);
    }
}
