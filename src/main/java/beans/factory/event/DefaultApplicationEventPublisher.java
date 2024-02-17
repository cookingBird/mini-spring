package beans.factory.event;

import java.util.ArrayList;
import java.util.List;

public class DefaultApplicationEventPublisher implements ApplicationEventPublisher {
    List<ApplicationListener> listeners = new ArrayList<>();

    @Override
    public void publishEvent(ApplicationEvent event) {
        for (ApplicationListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.listeners.add(listener);
    }

}
