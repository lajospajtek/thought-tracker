package org.tap.component.b;


import org.tap.api.IEvent;

/**
*/ // concrete visitor implementation, could be in another org.tap.component as well
public class VisitorBImpl extends VisitorB {

    @Override
    public void visitUnknown(final IEvent e) {
        System.out.println("VB: ? " + e.getClass());
    }

    @Override
    public void visit(final EventB1 eb1) {
        System.out.println("VB: eb1");
    }

    @Override
    public void visit(final EventB2 eb2) {
        System.out.println("VB: eb2");
    }
}
