/*
 * https://prof.ti.bfh.ch/hew1/informatik3/prolog/p-99/
 * http://aperiodic.net/phil/scala/s-99/
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.thought_tracker



object Lists {
    //p01
    def last[A](xs: List[A]): A = xs match {
        // xs.last
        case y::Nil => y
        case _ => last(xs.tail)
    }


    //p02
    def penultimate[A](xs: List[A]): A = xs match {
        // if (xs.length > 2) xs.takeRight(2).head else throw new NoSuchElementException
        case x :: _ :: Nil => x
        case _ => penultimate(xs.tail)
    }

    //p03
    def nth[A](n: Int, xs: List[A]): A =
	// xs(n)
	if (n == 0) xs.head else nth(n-1, xs.tail)

    //p04
    def length[A](xs: List[A]): Int = {
        // xs.length

        // (0 /: xs) { (acc, _) => acc + 1 }
        xs.foldLeft(0) { (acc, _) => acc + 1 }

        // def count( acc: Int, ys: List[A]): Int =
        //     if (ys.isEmpty) acc else count(acc + 1, ys.tail)
        // count(0, xs)
    }

    //p05
    def reverse[A](xs: List[A]): List[A] = {
        // xs.reverse

        // xs.foldLeft(List[A]()) { (acc, x) => x :: acc }
        (List[A]() /: xs) { (acc, x) => x :: acc }

        // def move(from: List[A], to: List[A]): List[A] =
        //     if (from.isEmpty) to else move(from.tail, from.head :: to)
        // move(xs, Nil)
    }

    //p06
    def isPalindrome[A](xs: List[A]): Boolean = {
        // xs == xs.reverse
        // xs == reverse(xs)
        if (xs.isEmpty) return true
        var result: Boolean = true
        val half: Int = xs.length / 2
        val last: Int = xs.length - 1
        for (i <- 0 to half) {
            if (xs(i) != xs(last - i)) result = false
        }
        result
    }

    //p07
    def flatten(xs: List[Any]): List[Any] = xs flatMap {
        // xs.flatten
        case (ls: List[Any]) => flatten(ls)
        case n: Any => List(n)
    }


    //p08
    def compress[A](xs: List[A]): List[A] = {
        //xs match {
        //    case x :: y :: ls => if (x == y) compress(y :: ls) else x :: compress(y :: ls)
        //    case ls => ls
        //}

        def compressRight(acc: List[A], ls: List[A]): List[A] = {
            ls match {
                case y :: ys => compressRight(y :: acc, ys.dropWhile( _ == y))
                case Nil => acc.reverse
            }
        }
        compressRight(Nil, xs)
    }

    //p09
    def pack[A](xs: List[A]): List[List[A]] = {
        def packTo(acc: List[List[A]], ls: List[A]): List[List[A]] = {
            if (ls isEmpty) acc.reverse
            else {
                val (head, tail) = ls.span(_ == ls.head)
                packTo(head :: acc, tail)
            }
        }
	
        packTo(Nil, xs)
    }

    //p10
    def encode[A](xs: List[A]): List[(Int, A)] = {
        pack(xs) map(ls => (ls.length, ls.head))
    }

    //p11
    def encodeModified[A](xs: List[A]): List[Any] = {
        pack(xs) map { ls => if (ls.length == 1) ls.head else (ls.length, ls.head) }
    }

    def ntimes[A](n: Int, item: A): Iterable[A] = {
        //for(i <- 0 until n) yield item
        Stream.make(n, item)
        // for lists List.make(....)
    }

    //p12
    def decode[A](encoded: List[(Int, A)]): List[A] = {
        encoded.flatMap(Function.tupled(ntimes[A]))
    }

    //p13  Run-length encoding of a list (direct solution).
    def encodeDirect[A](ls: List[A]): List[(Int, A)] = {
        //if (ls isEmpty) Nil
        //else {
        //    val (same, rest) = ls.span(_ == ls.head)
        //    (same.length, same.head) :: encodeDirect(rest)
        //}
        
        def encodeTo(acc: List[(Int, A)], xs: List[A]): List[(Int, A)] = {
            if (xs isEmpty) acc reverse
            else {
                val (same, rest) = xs.span( _ == xs.head)
                encodeTo((same.length, same.head) :: acc, rest)
            }
        }
        encodeTo(Nil, ls)
    }

    //p14
    def duplicate[A](ls : List[A]): List[A] = duplicateN(2, ls)

    //p15 Duplicate the elements of a list a given number of times.
    def duplicateN[A](n: Int, ls: List[A]): List[A] = {
        //def ntimesTo(n: Int, x: A, acc: List[A]): List[A] = {
        //    if (n == 0) acc else ntimesTo(n - 1, x, x::acc)
        //}
        //val result = (List[A]() /: ls)  { (acc, x) => ntimesTo(n, x, acc) }
        //result.reverse
        ls flatMap( x => List.make(n, x))
    }

    //p16
    def dropEveryNth[A](n: Int, ls: List[A]): List[A] = {
        //ls.zipWithIndex.filter(p => (p._2 +1) % n != 0 ).map(_._1)
        for (p <- ls.zipWithIndex if ((p._2 + 1) % n != 0)) yield p._1

        /*
        def collectTo(acc : List[A], cnt: Int, xs: List[A]): List[A] = {
            if (xs isEmpty) acc reverse
            else {
                if (cnt == 1) collectTo(acc, n, xs.tail)
                else collectTo(xs.head :: acc, cnt - 1, xs.tail)
            }
        }
        collectTo(List[A](), n, ls)
        */
    }

    //p17
    def split[A](n: Int, ls: List[A]): (List[A], List[A]) = {
        //ls splitAt n
        //(ls take n, ls drop n)


        //val (l,r) = ((List[A](), List[A]()) /: ls.zipWithIndex) ({ (acc, x) =>
        //    val (left, right) = acc
        //    val (item, index) = x
        //    if (index < n) (item :: left, right) else (left, item :: right)
        //})
        //(l.reverse, r.reverse)

        val r = ((List[A](), 1, List[A]()) /: ls) ({ (acc,x) =>
            val (left, cnt, right) = acc
            if (cnt <= n) (x :: left, cnt + 1, right) else (left, cnt, x :: right)
        })
        (r._1 reverse, r._3 reverse)
    }

    //p18 Extract a slice from a list.
    def slice[A](fromInclusive: Int, toExclusive: Int, ls: List[A]): List[A] = {
        //ls.slice(fromInclusive, toExclusive)
        ls.drop(fromInclusive).take(toExclusive - fromInclusive)
    }

    //p19 Rotate a list N places to the left.
    def rotate[A](n: Int, ls: List[A]): List[A] = {
        if (n < 0) rotateRight(-n, ls) else rotateLeft(n, ls)
     }

    def rotateLeft[A](n: Int, ls: List[A]): List[A] = {
        ls.drop(n) ::: ls.take(n)
    }

    def rotateRight[A](n: Int, ls: List[A]): List[A] = {
        ls.takeRight(n) ::: ls.dropRight(n)
    }

    //p20 Remove the Kth element from a list.
    // Return the list and the removed element in a Tuple.
    // Elements are numbered from 0.
    def removeAt[A](index: Int, ls: List[A]): (List[A],A) = {
        val (left, right) = split(index, ls)
        right match {
            case x :: xs => (left ::: xs, x)
            case _ => throw new NoSuchElementException
        }
    }

    //p21 Insert an element at a given position into a list.
    def insertAt[A](item: A, position: Int, ls: List[A]): List[A] = {
        //l.splitAt(n) match {nt
        //    case (pre, post) => pre ::: e :: post
        //}

        val (left,right) = ls.splitAt(position)
        left ::: (item :: right)
    }

    //p22 Create a list containing all integers within a given range.
    def range(fromInclusive: Int, toInclusive: Int): List[Int] = {
        // List.range(fromInclusive, toInclusive + 1)
        def accRange(from: Int, to: Int, acc: List[Int]): List[Int] = {
            if (from > to) acc else accRange(from, to - 1, to :: acc)
        }
        accRange(fromInclusive, toInclusive, List[Int]())
    }

    //p26 Generate the combinations of K distinct objects chosen from the N elements of a list.
    //In how many ways can a committee of 3 be chosen from a group of 12 people?
    //We all know that there are C(12,3) = 220  possibilities (C(N,K) denotes the well-known binomial coefficient).
    //For pure mathematicians, this result may be great. But we want to really generate all the possibilities.
    def combinations[A](k: Int, ls: List[A]): List[List[A]] = {
        def comb(xs: List[A], partial: List[A], acc: List[List[A]]) : List[List[A]] = {
            if (k == partial.length) return partial.reverse :: acc
            if (xs isEmpty) return acc
            
            val with_head = comb(xs.tail, xs.head :: partial, acc)
            val without_head = comb(xs.tail, partial, acc)

            return with_head ::: without_head
        }

        comb(ls, List[A](), List[List[A]]())
    }

}