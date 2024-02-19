package test;

import beans.factory.event.ApplicationEvent;
import beans.factory.event.ApplicationListener;
import beans.factory.event.ContextRefreshEvent;

public class MyListener implements ApplicationListener<ContextRefreshEvent> {
    public MyListener() {
    }

    @Override
    public void onApplicationEvent(ContextRefreshEvent ev) {
        System.out.println(".........refreshed.........beans count : " + ev);
    }
}
