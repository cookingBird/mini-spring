package web.bind;

import beans.PropertyEditorRegistrySupport;
import beans.factory.config.PropertyValue;
import beans.factory.config.PropertyValues;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanWrapperImpl extends PropertyEditorRegistrySupport {
    Object wrappedObject;
    Class<?> clz;
    PropertyValues propertyValues;

    public BeanWrapperImpl(Object wrappedObject) {
        super.registerDefaultEditors();
        this.wrappedObject = wrappedObject;
        this.clz = this.wrappedObject.getClass();
    }

    public Object getWrappedObject() {
        return wrappedObject;
    }

    public void setWrappedObject(Object wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
        for (PropertyValue propertyValue : this.propertyValues.getPropertyValues()) {
            this.setPropertyValue(propertyValue);
        }
    }

    public void setPropertyValue(PropertyValue propertyValue) {
        BeanPropertyHandler handler = new BeanPropertyHandler(propertyValue.getName());
        PropertyEditor editor = this.getDefaultEditor(handler.getClass());
        editor.setAsText((String) propertyValue.getValue());
        handler.setValue(editor.getValue());
    }

    class BeanPropertyHandler {
        Class<?> propertyClz = null;
        Method writeMethod = null;
        Method readMethod = null;

        public BeanPropertyHandler(String propertyName) {
            try {
                Field field = clz.getField(propertyName);
                propertyClz = field.getType();
                try {
                    this.writeMethod = clz.getDeclaredMethod(
                            "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1),
                            propertyClz);
                    this.readMethod = clz.getDeclaredMethod(
                            "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1),
                            propertyClz);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        public Object getValue() {
            Object result = null;
            this.readMethod.setAccessible(true);
            try {
                readMethod.invoke(wrappedObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return result;
        }

        public void setValue(Object value) {
            this.writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
