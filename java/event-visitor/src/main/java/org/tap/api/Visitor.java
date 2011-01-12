package org.tap.api;

import java.lang.reflect.ParameterizedType;


/**
*/
public abstract class Visitor<E extends Event<E,V>, V extends Visitor<E, V>> implements IVisitor {
    final transient Class<E> eventType;
    final transient Class<V> visitorType;

    @SuppressWarnings("unchecked")
    protected Visitor() {
        // the direct generic superclass should be the visitor type: VAI extends VA
        visitorType = (Class<V>) getClass().getGenericSuperclass();
        // the superclass of VA is api.Visitor<EA,VA>,
        final ParameterizedType superclass = (ParameterizedType) visitorType.getGenericSuperclass();
        // the 1st argument of api.Visitor<EA,VA> is the event type
        eventType = (Class<E>) superclass.getActualTypeArguments()[0];
    }

    public abstract void visitUnknown(IEvent e);
}
