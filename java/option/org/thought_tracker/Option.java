package org.thought_tracker;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Option<T> implements Iterable<T> {
    @SuppressWarnings("unchecked")
    public static <X> Option<X> none() {
        return (Option<X>) _none;
    }

    public static <X> Option<X> some(X value) {
        assert value != null;
        return new Option<X>(value);
    }

    private static Option<?> _none = new None<Object>();
	private static class None<T> extends Option<T> {
		private None() {
			super(null);
		}

		@Override
		public T getValue() {
			throw new NoSuchElementException("None has no value");
		}

		@Override
		public boolean hasValue() {
			return false;
		}
		
		@Override
		public Iterator<T> iterator() {
		    return new Iterator<T>() {
                public boolean hasNext() { return false; }
                public T next() { throw new NoSuchElementException(); }
                public void remove() { throw new UnsupportedOperationException(); }
		    };
		}
	}


	private final T value;;

	private Option(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public boolean hasValue() {
		return true;
	}

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private boolean is_index_before_value = true;
            public boolean hasNext() { return is_index_before_value; }
            public T next() { is_index_before_value = false; return value; }
            public void remove() { throw new UnsupportedOperationException(); }
        };
    }
}
