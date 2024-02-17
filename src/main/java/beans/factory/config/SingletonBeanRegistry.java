package beans.factory.config;

public interface SingletonBeanRegistry {
    void registerSingleton(String beaName, Object singletonObj);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonsName();

    void removeSingleton(String beanName);
}
