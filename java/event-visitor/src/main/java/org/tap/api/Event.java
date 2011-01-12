package org.tap.api;

import java.lang.reflect.ParameterizedType;

/**
*/
public abstract class Event<E extends Event<E,V>, V extends Visitor<E, V>> implements IEvent {
    private final Class<E> eventType;
    private final Class<V> visitorType;

    @SuppressWarnings("unchecked")
    protected Event() {
        // the direct generic superclass should be the event type: EventA1 extends EA
        eventType = (Class<E>) getClass().getGenericSuperclass();
        // the superclass of EA is api.Event<EA,VA>,
        final ParameterizedType superclass = (ParameterizedType) eventType.getGenericSuperclass();
        // the 2nd argument of api.Event<EA,VA> is the event type
        visitorType = (Class<V>) superclass.getActualTypeArguments()[1];
    }

    @Override
    public void accept(final IVisitor v) {
        if (visitorType.isInstance(v)) {
            accept(visitorType.cast(v));
        } else {
            v.visitUnknown(this);
        }
    }

    protected abstract void accept(final V visitor);
}
