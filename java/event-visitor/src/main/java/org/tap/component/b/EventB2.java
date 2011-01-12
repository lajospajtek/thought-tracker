package org.tap.component.b;

/**
*/ // concrete event 2
public class EventB2 extends EventB {
    @Override
    public void accept(final VisitorB visitor) { visitor.visit(this); }
}
