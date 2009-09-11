package org.thought_tracker;

import java.util.NoSuchElementException;

import junit.framework.Assert;

import org.junit.Test;

public class OptionTest extends Assert {

    @Test
    public void some_specifications() {
        Option<Integer> some = Option.some(5);
        
        assertTrue(some.hasValue());
        Integer a = some.getValue();
        Integer b = some.getValue();
        assertSame(a,b);
        assertEquals((Integer)5, a);
        
        int counted_elements_in_some = 0;
        for (Integer value : some) {
            counted_elements_in_some += 1;
            assertEquals((Integer)5, value);
        }
        assertEquals(1, counted_elements_in_some);
    }
    
    @Test
    public void none_specifications() {
        Option<Integer> none = Option.none();
        
        assertFalse(none.hasValue());
        try {
            none.getValue();
            fail("should throw exception, none.getValue() should not be allowed");
        } catch (NoSuchElementException e) {
            
        }
        
        int counted_elements_in_none = 0;
        for (Integer x : none) {
            counted_elements_in_none += 1;
            fail("none should have no elements");
        }
        assertEquals(0, counted_elements_in_none);
    }
}
