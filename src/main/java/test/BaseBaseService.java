package test;

public class BaseBaseService {
    AServiceImpl as;

    public BaseBaseService() {
    }

    public BaseBaseService(AServiceImpl as) {
        this.as = as;
    }

    public void sayHello() {
        System.out.println("Base BaseService says Hello");
        as.sayHello();
    }


    public AServiceImpl getAs() {
        return as;
    }

    public void setAs(AServiceImpl as) {
        this.as = as;
    }
}
