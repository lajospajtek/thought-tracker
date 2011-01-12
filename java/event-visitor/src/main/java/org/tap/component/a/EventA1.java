package org.tap.component.a;

/**
*/ // concrete event 1
public class EventA1 extends EventA {
    @Override
    protected void accept(final VisitorA visitor) {
        visitor.visit(this);
    }
}
