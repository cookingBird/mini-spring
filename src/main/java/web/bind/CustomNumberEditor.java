package web.bind;

import util.NumberUtils;
import util.StringUtils;

import java.text.NumberFormat;

public class CustomNumberEditor implements PropertyEditor {

    Class<? extends Number> numberClz;
    private NumberFormat numberFormat;
    private boolean allowEmpty;
    private Object value;

    public CustomNumberEditor(Class<? extends Number> numberClz, boolean allowEmpty) {
        this(numberClz, null, allowEmpty);
    }

    public CustomNumberEditor(Class<? extends Number> numberClz, NumberFormat numberFormat, boolean allowEmpty) {
        this.numberClz = numberClz;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            setValue(null);
        } else if (this.numberFormat != null) {
            setValue(NumberUtils.parseNumber(text, this.numberClz, this.numberFormat));
        } else {
            setValue(NumberUtils.parseNumber(text, this.numberClz));
        }
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            this.value = NumberUtils.convertNumberToTargetClass((Number) value, this.numberClz);
        } else {
            this.value = value;
        }
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public Object getAsText() {
        Object value = this.value;
        if (value == null) {
            return "";
        }
        if (this.numberFormat != null) {
            return this.numberFormat.format(this.value);
        } else {
            return value.toString();
        }
    }
}
