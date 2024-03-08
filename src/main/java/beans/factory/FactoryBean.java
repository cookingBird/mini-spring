package beans.factory;

public interface FactoryBean<T> {
    T getObject() throws Exception;

    Class<?> getObjectType() throws Exception;

    default boolean isSingleton() {
        return true;
    }
}
