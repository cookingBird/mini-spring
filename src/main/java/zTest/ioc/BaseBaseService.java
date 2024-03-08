package zTest.ioc;

public class BaseBaseService {
    AService as;

    public BaseBaseService() {
    }

    public BaseBaseService(AService as) {
        this.as = as;
    }

    public void sayHello() {
        System.out.println("Base BaseService says Hello");
        as.sayHello();
    }


    public AService getAs() {
        return as;
    }

    public void setAs(AService as) {
        this.as = as;
    }
}
