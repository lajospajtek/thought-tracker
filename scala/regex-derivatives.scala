// https://michid.wordpress.com/2010/12/06/regular-expression-matching-in-100-lines-of-code/

import scala.annotation.tailrec

trait RegExp {
  def isNullable: Boolean
  def derive(c: Char): RegExp
}

case object Empty extends RegExp {
  def isNullable = false
  def derive(c: Char): RegExp = Empty
}

case object Eps extends RegExp {
  def isNullable = true
  override def derive(c: Char) = Empty
}

case class Str(s: String) extends RegExp {
  def isNullable = s.isEmpty
  def derive(c: Char) = 
    if (s.isEmpty || s.head != c) Empty 
    else Str(s.tail)
}

case class Cat(r: RegExp, s: RegExp) extends RegExp {
  def isNullable = r.isNullable && s.isNullable
  def derive(c: Char) =
    if (r.isNullable) Or(Cat(r.derive(c), s), s.derive(c))
    else Cat(r.derive(c), s)
}

case class Star(r: RegExp) extends RegExp {
  def isNullable = true
  def derive(c: Char) = Cat(r.derive(c), this)
}

case class Or(r: RegExp, s: RegExp) extends RegExp {
  def isNullable = r.isNullable || s.isNullable
  def derive(c: Char) = Or(r.derive(c), s.derive(c))
}

case class And(r: RegExp, s: RegExp) extends RegExp {
  def isNullable = r.isNullable && s.isNullable
  def derive(c: Char) = And(r.derive(c), s.derive(c))
}

case class Not(r: RegExp) extends RegExp {
  def isNullable = !r.isNullable
  def derive(c: Char) = Not(r.derive(c))
}

object Matcher {
  @tailrec
  def isMatching(r: RegExp, s: String): Boolean = 
    if (s.isEmpty) r.isNullable
    else isMatching(r.derive(s.head), s.tail) 
}

object RegExpImplicits {
  implicit def stringToRegExp(s: String) = Str(s)

  private[RegExpImplicits] class RegExpOps(r: RegExp) {
    def | (s: RegExp) = Or(r, s)
    def & (s: RegExp) = And(r, s)
    def % = Star(r)
    def %(n: Int) = repeat(r, n)
    def ? = Or(Eps, r)
    def ! = Not(r)
    def +++ (s: RegExp) = Cat(r, s)
    def ~ (s: String) = Matcher.isMatching(r, s)
  }
  implicit def toRegExpOps(r: RegExp) = new RegExpOps(r)

  private[RegExpImplicits] class StringOps(s: String) {
    def | (r: RegExp) = Or(s, r)
    def | (r: String) = Or(s, r)
    def & (r: RegExp) = And(s, r)
    def & (r: String) = And(s, r)
    def % = Star(s)
    def % (n: Int) = repeat(Str(s), n)
    def ? = Or(Eps, s)
    def ! = Not(s)
    def +++ (r: RegExp) = Cat(s, r)
    def +++ (r: String) = Cat(s, r)
    def ~ (t: String) = Matcher.isMatching(s, t)
  }
  implicit def stringOps(s: String) = new StringOps(s)

  def repeat(r: RegExp, n: Int): RegExp =
    if (n <= 0) Star(r)
    else Cat(r, repeat(r, n - 1))
}

import RegExpImplicits._

def matches(s: String, r: RegExp): Unit = {
  val ok = r ~ s
  if (!ok) Console.println("Matched failed. string: %s".format(s)) 
}

def matchesNot(s: String, r: RegExp): Unit = {
  val ok = !(r ~ s)
  if (!ok) Console.println("Matched failed. string: %s".format(s)) 
}


val digit: RegExp = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
val int_regex = ("+" | "-").? +++ digit.%(1)
val real_regex = ("+" | "-").? +++ digit.%(1) +++ ("." +++ digit.%(1)).? +++ (("e" | "E") +++ ("+" | "-").? +++ digit.%(1)).?

val ints = List("0", "-4534", "+049", "99")
val reals = List("0.9", "-12.8", "+91.0", "9e12", "+9.21E-12", "-512E+01")
val errs = List("", "-", "+", "+-1", "-+2", "2-")

Console.println("testing int_regex...")
ints.foreach(s => matches(s,int_regex))
reals.foreach(s => matchesNot(s, int_regex))
errs.foreach(s => matchesNot(s, int_regex))
Console.println("done")

Console.println("testing real_regex...")
ints.foreach(s => matches(s,real_regex))
reals.foreach(s => matches(s, real_regex))
errs.foreach(s => matchesNot(s, real_regex))
Console.println("done")
