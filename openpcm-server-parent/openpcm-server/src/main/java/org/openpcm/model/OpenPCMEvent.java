package org.openpcm.model;

import org.springframework.context.ApplicationEvent;

public class OpenPCMEvent extends ApplicationEvent {

    /**
     * serial version uid
     */
    private static final long serialVersionUID = -5855747004443931472L;

    private final EventType eventType;

    public OpenPCMEvent(Object source, EventType eventType) {
        super(source);
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public static enum EventType {
        APP_STARTED, APP_CLOSED
    }
}
