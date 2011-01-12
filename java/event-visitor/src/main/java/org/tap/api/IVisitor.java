package org.tap.api;

/**
 * marker interface for all generic visitors
 * takes some of the generic verbosity away in the subclasses
*/
public interface IVisitor {
    void visitUnknown(IEvent e);
}
