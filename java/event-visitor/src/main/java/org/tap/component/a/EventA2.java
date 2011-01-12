package org.tap.component.a;

/**
*/ // concrete event 2
public class EventA2 extends EventA {
    @Override
    public void accept(final VisitorA visitor) {
        visitor.visit(this);
    }
}
