package beans.factory.support;

import beans.BeansException;
import beans.factory.BeanFactory;
import beans.factory.FactoryBean;
import beans.factory.config.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements BeanFactory, BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();
    private final List<String> beanDefinitionNames = new ArrayList<>();

    public AbstractBeanFactory() {
        super();
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                BeanDefinition beanDefinition = beanDefinitions.get(beanName);
                if (beanDefinition != null) {
                    singleton = this.createBean(beanDefinition);
                    this.registerBean(beanDefinition.getId(), singleton);

                    // bean postprocessor
                    // step 1: postProcessBeforeInitialization
                    this.applyBeanPostProcessorBeforeInitialization(singleton, beanName);
                    // step 2: init-method
                    if (beanDefinition.getInitMethodName() != null &&
                            !beanDefinition.equals("")) {
                        this.invokeInitMethod(beanDefinition, singleton);
                    }
                    // step 3: postProcessAfterInitialization
                    this.applyBeanPostProcessorAfterInitialization(singleton, beanName);
                    this.removeSingleton(beanName);
                    this.registerBean(beanName, singleton);
                }
            }
        }
        if (singleton instanceof FactoryBean) {
            try {
                return this.getObjectForBeanInstance(singleton, beanName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return singleton;
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) throws Exception {
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }

        FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
        return getObjectFromFactoryBean(factoryBean, beanName);
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        Constructor<?> con = null;
        Object obj = this.doCreateBean(beanDefinition);
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.handleProperties(beanDefinition, clz, obj);
        return obj;
    }

    private Object doCreateBean(BeanDefinition bd) {

        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;
        try {
            clz = Class.forName(bd.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ConstructorArgumentValues constructorArgumentValues = bd.getConstructorArgumentValues();
        if (constructorArgumentValues != null) {
            if (!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(constructorArgumentValue.getType()) || "java.lang.String".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    } else if ("Integer".equals(constructorArgumentValue.getType()) || "java.lang.Integer".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else if ("int".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue()).intValue();
                    } else {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    }
                }
                try {
                    con = clz.getConstructor(paramTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    obj = con.newInstance(paramValues);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    obj = clz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                obj = clz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println(bd.getId() + " bean created. ");
        return obj;
    }

    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
        System.out.println("handle properties for bean : " + bd.getId());
        PropertyValues propertyValues = bd.getPropertyValues();
        if (propertyValues != null) {
            if (!propertyValues.isEmpty()) {
                for (int i = 0; i < propertyValues.size(); i++) {
                    PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                    String pType = propertyValue.getType();
                    String pName = propertyValue.getName();
                    Object pValue = propertyValue.getValue();
                    boolean isRef = propertyValue.isRef();
                    Class<?>[] paramTypes = new Class[1];
                    Object[] paramValues = new Object[1];
                    if (!isRef) {
                        if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                            paramTypes[0] = String.class;
                        } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                            paramTypes[0] = Integer.class;
                        } else if ("int".equals(pType)) {
                            paramTypes[0] = int.class;
                        } else { // 默认为string
                            paramTypes[0] = String.class;
                        }
                        paramValues[0] = pValue;
                    } else {
                        try {
                            paramTypes[0] = Class.forName(pType);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            paramValues[0] = getBean((String) pValue);
                        } catch (BeansException e) {
                            e.printStackTrace();
                        }
                    }
                    String methodName = "set" + pName.substring(0, 1).toUpperCase()
                            + pName.substring(1);
                    Method method = null;
                    try {
                        method = clz.getMethod(methodName, paramTypes);
                        method.invoke(obj, paramValues);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean containsBean(String name) {
        return super.containsSingleton(name);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return this.beanDefinitions.get(beanName).isSingleton();
    }

    @Override
    public boolean iPrototype(String beanName) {
        return this.beanDefinitions.get(beanName).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitions.get(name).getClass();
    }

    public void registerBean(String beanName, Object obj) {
        super.registerSingleton(beanName, obj);
    }

    @Override
    public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitions.put(beanName, beanDefinition);
        this.beanDefinitionNames.add(beanName);
        if (!beanDefinition.isLazyInit()) {
            try {
                this.getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        this.beanDefinitionNames.remove(beanName);
        this.beanDefinitions.remove(beanName);
        super.removeSingleton(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitions.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionNames.contains(beanName);
    }

    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                this.getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    public void invokeInitMethod(BeanDefinition bd, Object obj) {
        Class<?> clz = bd.getClass();
        Method method = null;
        try {
            method = clz.getMethod(bd.getInitMethodName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            method.invoke(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    abstract public Object applyBeanPostProcessorBeforeInitialization(Object bean, String beanName) throws BeansException;

    abstract public Object applyBeanPostProcessorAfterInitialization(Object bean, String beanName) throws BeansException;

    public Map<String, BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    public List<String> getBeanDefinitionNames() {
        return beanDefinitionNames;
    }
}
