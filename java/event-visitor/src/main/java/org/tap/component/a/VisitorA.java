package org.tap.component.a;


import org.tap.api.Visitor;

/**
*/ // API: superclass for the visitors defined by this org.tap.component
abstract class VisitorA extends Visitor<EventA,VisitorA> {
    abstract void visit(EventA1 ea1);
    abstract void visit(EventA2 ea2);
}
