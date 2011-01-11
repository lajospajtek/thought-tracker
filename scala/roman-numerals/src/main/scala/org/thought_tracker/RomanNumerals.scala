/*
http://rubyquiz.com/quiz22.html
Roman Numerals (#22)

This week's quiz is to write a converter to and from Roman numerals.

The script should be a standard Unix filter, reading from files specified on the command-line or STDIN and writing to STDOUT.
Each line of input will contain one integer (between 1 and 3999) expressed as an Arabic or Roman numeral.
There should be one line of output for each line of input, containing the original number in the opposite format.

For example, given the following input:

III
29
38
CCXCI
1999

The correct output is:

3
XXIX
XXXVIII
291
MCMXCIX

If you're not familiar with or need a refresher on Roman numerals, the rules are simple.
First, there are seven letters associated with seven values:

I = 1
V = 5
X = 10
L = 50
C = 100
D = 500
M = 1000

You can combine letters to add values, by listing them largest to smallest from left to right:

II is 2
VIII is 8
XXXI is 31

However, you may only list three consecutive identical letters.
That requires a special rule to express numbers like 4 and 900.
That rule is that a single lower value may proceed a larger value, to indicate subtraction.
This rule is only used to build values not reachable by the previous rules:

IV is 4
CM is 900

But 15 is XV, not XVX.
*/

package org.thought_tracker

object RomanNumerals {
    // sorted for the filter
    val numerals = List(("M", 1000), ("CM", 900), ("D", 500), ("CD", 400), ("C", 100),  ("XC", 90),  ("L", 50),  ("XL", 40), ("X", 10),   ("IX", 9),   ("V", 5),   ("IV", 4), ("I", 1))

    def apply(x: Int) : String = {
        unfold(x, next).mkString("")
    }

    def unapply(roman : String) : Int = {
        val found = numerals.foldLeft("", 0)((acc: (String,Int), x: (String,Int)) => if ((roman startsWith x._1) && (x._1.length > acc._1.length)) x else acc)
        if (found._2 > 0) found._2 + unapply(roman.substring(found._1.length)) else 0
    }

    def next(number: Int) =  numerals.find(_._2 <= number) match {
        case Some((roman, value)) => Some((roman, number - value))
        case _ => None
    }

    def unfold[T,R](init: T, f: (T => Option[(R,T)])): List[R] = f(init) match {
        case Some((r,t)) => r::unfold(t, f)
        case None => Nil
    }
}
