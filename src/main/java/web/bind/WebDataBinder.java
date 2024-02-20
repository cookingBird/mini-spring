package web.bind;

import beans.factory.config.PropertyValues;
import util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class WebDataBinder {
    private Object target;
    private Class<?> clz;
    private String objectName;

    public WebDataBinder(Object target) {
        this(target, "");
    }

    public WebDataBinder(Object target, String targetName) {
        this.target = target;
        this.objectName = targetName;
        this.clz = this.target.getClass();
    }

    public void bind(HttpServletRequest request) {
        PropertyValues propertyValues = parseParameters(request);
        this.addBindValues(propertyValues, request);
        doBind(propertyValues);
    }

    protected void doBind(PropertyValues propertyValues) {
        this.applyPropertyValues(propertyValues);
    }

    protected void applyPropertyValues(PropertyValues propertyValues) {
        this.getPropertyAccessor().setPropertyValues(propertyValues);
    }

    protected BeanWrapperImpl getPropertyAccessor() {
        return new BeanWrapperImpl(this.target);
    }

    private PropertyValues parseParameters(HttpServletRequest request) {
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }

    protected void addBindValues(PropertyValues propertyValues, HttpServletRequest request) {

    }
}
