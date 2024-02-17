package beans.factory.event;

import java.util.EventListener;

public interface IApplicationListener<E extends ApplicationEvent> extends EventListener {
    void onApplicationEvent(E ev);
}
