package org.tap;

import org.tap.api.IEvent;
import org.tap.api.IVisitor;
import org.tap.component.a.EventA1;
import org.tap.component.a.EventA2;
import org.tap.component.a.VisitorAImpl;
import org.tap.component.b.EventB1;
import org.tap.component.b.EventB2;
import org.tap.component.b.VisitorBImpl;

public class EventVisitor {

    public static void main(final String[] args) {

        final IEvent ea1 = new EventA1();
        final IEvent ea2 = new EventA2();
        final IEvent eb1 = new EventB1();
        final IEvent eb2 = new EventB2();
        final IVisitor va = new VisitorAImpl();
        final IVisitor vb = new VisitorBImpl();

        ea1.accept(va);
        ea2.accept(va);
        eb1.accept(va);
        eb2.accept(va);

        ea1.accept(vb);
        ea2.accept(vb);
        eb1.accept(vb);
        eb2.accept(vb);
    }
}
