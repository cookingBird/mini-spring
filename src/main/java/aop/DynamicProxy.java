package aop;

import java.lang.reflect.Proxy;

public class DynamicProxy {
    private Object object = null;

    public DynamicProxy() {
    }

    public DynamicProxy(Object object) {
        this.object = object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return Proxy
                .newProxyInstance(DynamicProxy.class.getClassLoader(),
                        object.getClass().getInterfaces(),
                        (proxy, method, args) -> {
                            if (method.getName().equals("doAction")) {
                                //do something
                                System.out.println("dynamic proxy before doAction");
                                return method.invoke(object, args);
                            }
                            return null;
                        }
                );
    }
}
