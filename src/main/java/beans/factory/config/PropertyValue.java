package beans.factory.config;

public class PropertyValue {
    private final String name;
    private String type;
    private final Object value;
    private final Boolean isRef;

    public PropertyValue(String name, Object value) {
        this("String", name, value, false);
    }

    public PropertyValue(String type, String name, Object value) {
        this(type, name, value, false);
    }

    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
    }

    public boolean isRef() {
        return isRef;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
