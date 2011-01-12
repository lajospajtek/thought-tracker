package org.tap.api;

/**
 * marker interface for all events
 * takes some of the generic verbosity away in the subclasses
 */
public interface IEvent {
    void accept(IVisitor visitor);
}
