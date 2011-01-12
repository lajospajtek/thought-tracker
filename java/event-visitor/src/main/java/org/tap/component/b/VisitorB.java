package org.tap.component.b;


import org.tap.api.Visitor;

/**
*/ // API: superclass for the visitors defined by this org.tap.component
public abstract class VisitorB extends Visitor<EventB,VisitorB> {
    abstract void visit(EventB2 eb2);
    abstract void visit(EventB1 eb1);
}
