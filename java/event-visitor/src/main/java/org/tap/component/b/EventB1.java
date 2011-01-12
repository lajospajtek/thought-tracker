package org.tap.component.b;

/**
*/ // concrete event 1
public class EventB1 extends EventB {
    @Override
    public void accept(final VisitorB visitor) { visitor.visit(this); }
}
