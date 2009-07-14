/*
 * Main.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.thought_tracker
import scala.reflect.Manifest;

import org.junit.Assert._;
import org.junit.Test;
import Lists._;

class ListsTest {

    def expect[E <: Throwable](f: => Unit) (implicit manifest: Manifest[E]): Unit = {
        val clazz = manifest.erasure.asInstanceOf[Class[E]]
        try {
            f
        } catch {
            case t => if (clazz isInstance t) return else fail("received: " + t.toString)
        }
    }

    @Test def p01(): Unit = {
        assertEquals(8, last(List(1, 1, 2, 3, 5, 8)))
        assertEquals(8, last(List(8)))
        expect[NoSuchElementException] { last(List()) }
    }

    @Test def p02(): Unit = {
        assertEquals(5, penultimate(List(1, 1, 2, 3, 5, 8)))
        expect[NoSuchElementException] { penultimate(List()) }
        expect[NoSuchElementException] { penultimate(List(1)) }
    }

    @Test def p03(): Unit = {
        assertEquals(2, nth(2, List(1, 1, 2, 3, 5, 8)))
        expect[NoSuchElementException] { nth(1, List(1)) }
    }

    @Test def p04(): Unit = {
        assertEquals(6, length(List(1, 1, 2, 3, 5, 8)))
        assertEquals(0, length(List()))
    }

    @Test def p05(): Unit = {
        assertEquals(List(8, 5, 3, 2, 1, 1), reverse(List(1, 1, 2, 3, 5, 8)))
        assertEquals(List(), reverse(List()))
    }

    @Test def p06(): Unit = {
        assertFalse(isPalindrome(List(1, 2, 3, 4)))
        assertTrue(isPalindrome(List(1, 2, 3, 2, 1)))
        assertTrue(isPalindrome(List(1)))
        assertTrue(isPalindrome(List()))
        assertFalse(isPalindrome(List(1, 2)))
    }

    @Test def p07(): Unit = {
        assertEquals(List(1, 1, 2, 3, 5, 8), flatten(List(List(1, 1), 2, List(3, List(5, 8)))))

    }

    @Test def p08(): Unit = {
        assertEquals(List('a, 'b, 'c, 'a, 'd, 'e), compress(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)))
        assertEquals(List(1), compress(List(1)))
        assertEquals(List(), compress(List()))
    }

    @Test def p09(): Unit = {
        assertEquals(
            List(List('a, 'a, 'a, 'a), List('b), List('c, 'c), List('a, 'a), List('d), List('e, 'e, 'e, 'e)),
            pack(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
        )
    }

    @Test def p10(): Unit = {
        assertEquals(
            List((4,'a), (1,'b), (2,'c), (2,'a), (1,'d), (4,'e)),
            encode(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
        )
    }
    
    @Test def p11(): Unit = {
        assertEquals(
            List((4,'a), 'b, (2,'c), (2,'a), 'd, (4,'e)),
            encodeModified(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
        )
    }

    @Test def p12(): Unit = {
        assertEquals(
            List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e),
            decode(List((4, 'a), (1, 'b), (2, 'c), (2, 'a), (1, 'd), (4, 'e)))
        )
    }
    @Test def p13(): Unit = {
        assertEquals(
            List((4,'a), (1,'b), (2,'c), (2,'a), (1,'d), (4,'e)),
            encodeDirect(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
        )
    }

    @Test def p14(): Unit = {
        assertEquals(
            List('a, 'a, 'b, 'b, 'c, 'c, 'c, 'c, 'd, 'd),
            duplicate(List('a, 'b, 'c, 'c, 'd))
        )
    }

    @Test def p15(): Unit = {
        assertEquals(
            List('a, 'a, 'a, 'b, 'b, 'b, 'c, 'c, 'c, 'c, 'c, 'c, 'd, 'd, 'd),
            duplicateN(3, List('a, 'b, 'c, 'c, 'd))
          )
    }

    @Test def p16(): Unit = {
        assertEquals(
            List('a, 'b, 'd, 'e, 'g, 'h, 'j, 'k),
            dropEveryNth(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        )
        assertEquals(
            List[Symbol](),
            dropEveryNth(1, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        )
    }

    @Test def p17(): Unit = {
        assertEquals(
            (List('a, 'b, 'c),List('d, 'e, 'f, 'g, 'h, 'i, 'j, 'k)),
            split(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        )
    }

    @Test def p18(): Unit = {
        assertEquals(
            (List('a, 'b, 'c),List('d, 'e, 'f, 'g, 'h, 'i, 'j, 'k)),
            split(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        )
    }

    @Test def p19(): Unit = {
        assertEquals(
            List('d, 'e, 'f, 'g, 'h, 'i, 'j, 'k, 'a, 'b, 'c),
            rotate(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        )

        assertEquals(
            List('j, 'k, 'a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i),
            rotate(-2, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        )
    }

    @Test def p20(): Unit = {
        assertEquals(
            (List('a, 'c, 'd),'b),
            removeAt(1, List('a, 'b, 'c, 'd))
        )
    }

    @Test def p21(): Unit = {
        assertEquals(
            List('a, 'new, 'b, 'c, 'd),
            insertAt('new, 1, List('a, 'b, 'c, 'd))
        )
    }

    @Test def p22(): Unit = {
        assertEquals(
            List(4, 5, 6, 7, 8, 9),
            range(4, 9)
        )
    }

    @Test def p26(): Unit = {
        assertEquals(
            List(List(1, 2), List(1, 3), List(1, 4), List(2, 3), List(2, 4), List(3, 4)),
            combinations(2, List(1,2,3,4))
        )
    }
}
