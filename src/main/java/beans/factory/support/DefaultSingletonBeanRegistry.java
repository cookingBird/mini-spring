package beans.factory.support;

import beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private List<String> beanNames = new ArrayList<String>();
    private Map<String, Object> singletons = new ConcurrentHashMap<>();

    public DefaultSingletonBeanRegistry() {
    }

    @Override
    public void registerSingleton(String beaName, Object singletonObj) {
        synchronized (this.singletons) {
            this.singletons.put(beaName, singletonObj);
            this.beanNames.add(beaName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletons.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletons.containsKey(beanName);
    }

    @Override
    public String[] getSingletonsName() {
        return (String[]) this.beanNames.toArray();
    }

    @Override
    public void removeSingleton(String beanName) {
        synchronized (this.singletons) {
            this.beanNames.remove(beanName);
            this.singletons.remove(beanName);
        }
    }
}
