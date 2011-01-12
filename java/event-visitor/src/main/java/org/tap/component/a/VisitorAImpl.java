package org.tap.component.a;


import org.tap.api.IEvent;

/**
 * concrete implementation visitor, could be in another org.tap.component
 */
public class VisitorAImpl extends VisitorA {

    @Override
    public void visitUnknown(final IEvent e) {
        System.out.println("VA: ? " + e.getClass());
    }

    @Override
    public void visit(final EventA1 ea1) {
        System.out.println("VA: ea1");
    }

    @Override
    public void visit(final EventA2 ea2) {
        System.out.println("VA: ea2");
    }
}
