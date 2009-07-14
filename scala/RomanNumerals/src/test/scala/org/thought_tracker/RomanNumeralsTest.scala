/*
 * RomanNumeralsTest.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.thought_tracker

import org.junit.Assert._;
import org.junit.Test;

class RomanNumeralsTest {

    @Test def should_transform_both_ways() = {
        assertEquals("CXXIII", RomanNumerals(123));
        assertEquals(1118, RomanNumerals.unapply("MCXVIII"));
    }

}
