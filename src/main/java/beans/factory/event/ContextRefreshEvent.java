package beans.factory.event;

public class ContextRefreshEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;

    public ContextRefreshEvent(Object source) {
        super(source);
    }

    @Override
    public String toString() {
        return "ContextRefreshEvent" + msg;
    }
}
