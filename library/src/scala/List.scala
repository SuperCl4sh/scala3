/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2005, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |                                         **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
** $Id:List.scala 5359 2005-12-16 16:33:49 +0100 (Fri, 16 Dec 2005) dubochet $
\*                                                                      */

package scala;

import Predef._;

/** This object provides methods for creating specialized lists, and for
 *  transforming special kinds of lists (e.g. lists of lists).
 *
 *  @author  Martin Odersky and others
 *  @version 1.0, 15/07/2003
 */
object List {

  /** Create a list with given elements.
   *
   *  @param xs the elements to put in the list
   *  @return the list containing elements xs.
   */
  def apply[A](xs: A*): List[A] =
    // TODO: remove the type test once nsc becomes standard
    if (xs.isInstanceOf$erased[List[A]])
      xs.asInstanceOf$erased[List[A]];
    else
      fromArray(xs.asInstanceOf$erased[Array[A]]);

  /** Create a sorted list of all integers in a range.
   *
   *  @param from the start value of the list
   *  @param end the end value of the list
   *  @return the sorted list of all integers in range [from;end).
   */
  def range(from: Int, end: Int): List[Int] =
    range(from, end, 1);

  /** Create a sorted list of all integers in a range.
   *
   *  @param from the start value of the list
   *  @param end the end value of the list
   *  @param step the increment value of the list
   *  @return the sorted list of all integers in range [from;end).
   */
  def range(from: Int, end: Int, step: Int): List[Int] =
    if (from >= end) Nil
    else from :: range(from + step, end, step);

  /** Create a sorted list of all integers in a range.
   *
   *  @param from the start value of the list
   *  @param end the end value of the list
   *  @param step the increment function of the list
   *  @return the sorted list of all integers in range [from;end).
   */
  def range(from: Int, end: Int, step: Int => Int): List[Int] =
    if (from >= end) Nil
    else from :: range(step(from), end, step);

  /** Create a list containing several copies of an element.
   *
   *  @param n the length of the resulting list
   *  @param elem the element composing the resulting list
   *  @return a list composed of n elements all equal to elem
   */
  def make[a](n: int, elem: a): List[a] =
    if (n == 0) Nil
    else elem :: make(n - 1, elem);

  /** Create a list by applying a function to successive integers.
   *
   *  @param n     the length of the resulting list
   *  @param maker the procedure which, given an integer n, returns the
   *               nth element of the resulting list, where n is in [0;n).
   *  @return the list obtained by applying the maker function to successive
   *          integers from 0 to n (exclusive).
   */
  def tabulate[a](n: int, maker: int => a): List[a] = {
    def loop(i: int): List[a] =
      if (i == n) Nil
      else maker(i) :: loop(i + 1);
    loop(0)
  }

  /** Concatenate all the elements of a given list of lists.
   *  @param l the list of lists that are to be concatenated
   *  @return the concatenation of all the lists
   */
  def flatten[a](l: List[List[a]]): List[a] = l match {
    case Nil => Nil
    case head :: tail => head ::: flatten(tail)
  }

  /** Transforms a list of pair into a pair of lists.
   *
   *  @param l the list of pairs to unzip
   *  @return a pair of lists: the first list in the pair contains the list
   */
  def unzip[a,b](l: List[Pair[a,b]]): Pair[List[a], List[b]] = l match {
    case Nil => new Pair(Nil, Nil)
    case Pair(f, s) :: tail =>
      val Pair(fs, ss) = unzip(tail);
      Pair(f :: fs, s :: ss)
  }

  /** Converts an iterator to a list
   *
   *  @param it the iterator to convert
   *  @return a list that contains the elements returned by successive
   *  calls to <code>it.next</code>
   */
  def fromIterator[a](it: Iterator[a]): List[a] =
    if (it.hasNext) it.next :: fromIterator(it);
    else Nil;

  /** Converts an array into a list.
   *
   *  @param arr the array to convert
   *  @return a list that contains the same elements than <code>arr</code>
   *           in the same order
   */
  def fromArray[a](arr: Array[a]): List[a] = fromArray(arr, 0, arr.length);

  /** Converts a range of an array into a list.
   *
   *  @param arr the array to convert
   *  @param start the first index to consider
   *  @param len the lenght of the range to convert
   *  @return a list that contains the same elements than <code>arr</code>
   *           in the same order
   */
  def fromArray[a](arr: Array[a], start: Int, len: Int): List[a] = {
    var res: List[a] = Nil;
    var i = start + len;
    while (i > start) {
      i = i - 1;
      res = arr(i) :: res;
    }
    res
  }

  /** Parses a string which contains substrings separated by a
   *
   *  separator character and returns a list of all substrings.
   *  @param str the string to parse
   *  @param separator the separator character
   *  @return the list of substrings
   */
  def fromString(str: String, separator: Char): List[String] = {
    var words: List[String] = List();
    var pos = str.length();
    while (pos > 0) {
      val pos1 = str.lastIndexOf(separator, pos - 1);
      if (pos1 + 1 < pos)
	words = str.substring(pos1 + 1, pos) :: words;
      pos = pos1
    }
    words
  }
/*
    var res: List[String] = Nil;
    var start = 0;
    var end = str.indexOf(separator);
    while (end >= 0) {
      if (end > start)
        res = str.substring(start, end) :: res;
      end = end + 1;
      start = end;
      end = str.indexOf(separator, end);
    }
    res.reverse
  }
*/
  /** Returns the given string as a list of characters.
   *
   *  @param str the string to convert.
   *  @return the string as a list of characters.
   */
  def fromString(str: String): List[Char] = {
    var res: List[Char] = Nil;
    var i = str.length();
    while (i > 0) {
      i = i - 1;
      res = str.charAt(i) :: res;
    }
    res
  }

  /** Returns the given list of characters as a string.
   *
   *  @param xs the list to convert.
   *  @return the list in form of a string.
   */
  def toString(xs: List[Char]): String = {
    val sb = new StringBuffer();
    var ys = xs;
    while (!ys.isEmpty) {
      sb.append(ys.head);
      ys = ys.tail;
    }
    sb.toString()
  }

  /** Returns the list resulting from applying the given function <code>f</code> to
   *  corresponding elements of the argument lists.
   *
   *  @param f function to apply to each pair of elements.
   *  @return <code>[f(a0,b0), ..., f(an,bn)]</code> if the lists are
   *          <code>[a0, ..., ak]</code>, <code>[b0, ..., bl]</code> and
   *          <code>n = min(k,l)</code>
   */
  def map2[a,b,c](xs: List[a], ys: List[b])(f: (a, b) => c): List[c] =
    if (xs.isEmpty || ys.isEmpty) Nil
    else f(xs.head, ys.head) :: map2(xs.tail, ys.tail)(f);

  /** Like xs map f, but returns xs unchanged if function `f' maps all elements to themselves
   */
  def mapConserve[a <: AnyRef](xs: List[a])(f: a => a): List[a] = {
    if (xs.isEmpty) xs
    else {
      val head = xs.head;
      val head1 = f(head);
      val tail = xs.tail;
      val tail1 = mapConserve(tail)(f);
      if ((head1 eq head) && (tail1 eq tail)) xs else head1 :: tail1
    }
  }

  /** Returns the list resulting from applying the given function <code>f</code> to
   *  corresponding elements of the argument lists.
   *
   *  @param f function to apply to each pair of elements.
   *  @return <code>[f(a0,b0,c0), ..., f(an,bn,cn)]</code> if the lists are
   *          <code>[a0, ..., ak]</code>, <code>[b0, ..., bl]</code>, <code>[c0, ..., cm]</code> and
   *          <code>n = min(k,l,m)</code>
   */
  def map3[a,b,c, d](xs: List[a], ys: List[b], zs: List[c])(f: (a, b, c) => d): List[d] =
    if (xs.isEmpty || ys.isEmpty || zs.isEmpty) Nil
    else f(xs.head, ys.head, zs.head) :: map3(xs.tail, ys.tail, zs.tail)(f);

  /** Tests whether the given predicate <code>p</code> holds
   *  for all corresponding elements of the argument lists.
   *
   *  @param p function to apply to each pair of elements.
   *  @return <code>n == 0 || (p(a0,b0) &amp;&amp; ... &amp;&amp; p(an,bn))]</code> if the lists are
   *          <code>[a0, ..., ak]</code>, <code>[b0, ..., bl]</code> and
   *          <code>m = min(k,l)</code>
   */
  def forall2[a,b](xs: List[a], ys: List[b])(f: (a, b) => boolean): boolean =
    if (xs.isEmpty || ys.isEmpty) true
    else f(xs.head, ys.head) && forall2(xs.tail, ys.tail)(f);

  /** Tests whether the given predicate <code>p</code> holds
   *  for some corresponding elements of the argument lists.
   *
   *  @param p function to apply to each pair of elements.
   *  @return <code>n != 0 &amp;&amp; (p(a0,b0) || ... || p(an,bn))]</code> if the lists are
   *          <code>[a0, ..., ak]</code>, <code>[b0, ..., bl]</code> and
   *          <code>m = min(k,l)</code>
   */
  def exists2[a,b](xs: List[a], ys: List[b])(f: (a, b) => boolean): boolean =
    if (xs.isEmpty || ys.isEmpty) false
    else f(xs.head, ys.head) || exists2(xs.tail, ys.tail)(f);

  /** Transposes a list of lists.
   *  pre: All element lists have the same length.
   */
  def transpose[a](xss: List[List[a]]): List[List[a]] =
    if (xss.head.isEmpty) List()
    else (xss map (xs => xs.head)) :: transpose(xss map (xs => xs.tail));

  /** Lists with ordered elements are ordered
   */
  implicit def list2ordered[a <% Ordered[a]](x: List[a]): Ordered[List[a]] = new Ordered[List[a]] {
    def compareTo [b >: List[a] <% Ordered[b]](y: b): int = y match {
      case y1: List[a] => compareLists(x, y1);
      case _ => -(y compareTo x)
    }
    private def compareLists(xs: List[a], ys: List[a]): int = {
      if (xs.isEmpty && ys.isEmpty) 0
      else if (xs.isEmpty) -1
      else if (ys.isEmpty) 1
      else {
        val s = xs.head compareTo ys.head;
        if (s != 0) s
        else compareLists(xs.tail, ys.tail)
      }
    }
  }
  def view[a <% Ordered[a]](x: List[a]): Ordered[List[a]] = list2ordered(x);
}

/** A trait representing an ordered collection of elements of type
 *  <code>a</code>. This class comes with two implementing case
 *  classes <code>scala.Nil</code> and <code>scala.::</code> that
 *  implement the abstract members <code>isEmpty</code>,
 *  <code>head</code> and <code>tail</code>.
 *
 *  @author  Martin Odersky and others
 *  @version 1.0, 16/07/2003
 */
sealed abstract class List[+a] extends Seq[a] {

  /** Returns true if the list does not contain any elements.
   *  @return true, iff the list is empty.
   */
  def isEmpty: Boolean;

  /** Returns this first element of the list.
   *  @return the first element of this list.
   *  @throws <code>java.lang.RuntimeException</code> if the list is empty.
   */
  def head: a;

  /** Returns this list without its first element.
   *  @return this list without its first element.
   *  @throws <code>java.lang.RuntimeException</code> if the list is empty.
   */
  def tail: List[a];

  /** Add an element <code>x</code> at the beginning of this list.
   *  <p/>
   *  Ex:<br/>
   *  <code>1 :: [2, 3] = [2, 3].::(1) = [1, 2, 3]</code>.
   *  @param x the element to append.
   *  @return the list with <code>x</code> appended at the beginning.
   */
  def ::[b >: a](x: b): List[b] =
    new scala.::(x, this);

  /** Returns a list resulting from the concatenation of the given
   *  list <code>prefix</code> and this list.
   *  <p/>
   *  Ex:<br/>
   *  <code>[1, 2] ::: [3, 4] = [3, 4].:::([1, 2]) = [1, 2, 3, 4]</code>.
   *  @param prefix the list to concatenate at the beginning of this list.
   *  @return the concatenation of the two lists.
   */
  def :::[b >: a](prefix: List[b]): List[b] = prefix match {
    case Nil => this
    case head :: tail => head :: (tail ::: this);
  }

  /** Reverse the given prefix and append the current list to that.
   *  This function is equivalent to an application of <code>reverse</code>
   *  on the prefix followed by a call to <code>:::</code>, but more
   *  efficient (and tail recursive).
   *  @param prefix the prefix to reverse and then prepend
   *  @return the concatenation of the reversed prefix and the current list.
   */
  def reverse_:::[b >: a](prefix: List[b]): List[b] = prefix match {
    case Nil => this
    case head :: tail => tail.reverse_:::(head :: this)//todo: remove type annotation
  }

  /** Returns the number of elements in the list.
   *
   *  @return the number of elements in the list.
   */
  def length: Int = {
    var xs = this;
    var len = 0;
    while (!xs.isEmpty) {
      len = len + 1;
      xs = xs.tail
    }
    len
  }

  /** Creates a list with all indices in the list. This is
   *  equivalent to a call to <code>List.range(0, xs.length)</code>.
   *
   *  @return a list of all indices in the list.
   */
  def indices: List[Int] = {
    def loop(i: Int, xs: List[a]): List[Int] = xs match {
      case Nil => Nil
      case _ :: ys => i :: loop(i + 1, ys)
    }
    loop(0, this)
  }

  /** Returns the elements in the list as an iterator
   *
   *  @return an iterator on the list elements.
   */
  def elements: Iterator[a] = new Iterator[a] {
    var current = List.this;
    def hasNext: Boolean = !current.isEmpty;
    def next: a =
      if (!hasNext)
        error("next on empty Iterator")
      else {
        val result = current.head; current = current.tail; result
      }
  }

  /** Transform this sequence into a list of all elements.
  *
  *  @return  a list which enumerates all elements of this sequence.
  */
  override def toList: List[a] = this;

  /** Returns the list without its last element.
   *
   *  @return the list without its last element.
   *  @throws <code>java.lang.RuntimeException</code> if the list is empty.
   */
  def init: List[a] = this match {
    case Nil => error("Nil.init")
    case _ :: Nil => Nil
    case head :: tail => head :: tail.init
  }

  /** Returns the last element of this list.
   *
   *  @return the last element of the list.
   *  @throws <code>java.lang.RuntimeException</code> if the list is empty.
   */
  def last: a = this match {
    case Nil => error("Nil.last")
    case last :: Nil => last
    case _ :: tail => tail.last
  }

  /** Returns the <code>n</code> first elements of this list.
   *
   *  @param n the number of elements to take.
   *  @return the <code>n</code> first elements of this list.
   */
  override def take(n: Int): List[a] =
    if (n == 0 || isEmpty) Nil
    else head :: (tail take (n-1));

  /** Returns the list without its <code>n</code> first elements.
   *
   *  @param n the number of elements to drop.
   *  @return the list without its <code>n</code> first elements.
   */
  override def drop(n: Int): List[a] =
    if (n == 0 || isEmpty) this
    else (tail drop (n-1));

  /** Returns the rightmost <code>n</code> elements from this list.
   *
   *  @param n the number of elements to take
   *  @return the suffix of length <code>n</code> of the list
   *  @throws <code>java.lang.RuntimeException</code> if the list is too short.
   */
  def takeRight(n: Int): List[a] = {
    def loop(lead: List[a], lag: List[a]): List[a] = lead match {
      case Nil => lag
      case _ :: tail => loop(tail, lag.tail)
    }
    loop(drop(n), this)
  }

  /** Returns the list wihout its rightmost <code>n</code> elements.
   *
   *  @param n the number of elements to take
   *  @return the suffix of length <code>n</code> of the list
   *  @throws <code>java.lang.RuntimeException</code> if the list is too short.
   */
  def dropRight(n: Int): List[a] = {
    def loop(lead: List[a], lag: List[a]): List[a] = lead match {
      case Nil => Nil
      case _ :: tail => lag.head :: loop(tail, lag.tail)
    }
    loop(drop(n), this)
  }

  /** Split the list at a given point and return the two parts thus
   *  created.
   *
   *  @param n the position at which to split
   *  @return a pair of lists composed of the first <code>n</code>
   *          elements, and the other elements.
   */
  def splitAt(n: Int): Pair[List[a], List[a]] =
    if (n == 0) Pair(Nil, this)
    else {
      val Pair(tail1, tail2) = tail splitAt (n-1);
      Pair(head :: tail1, tail2)
    }

  /** Returns the longest prefix of this list whose elements satisfy
   *  the predicate <code>p</code>.
   *
   *  @param p the test predicate.
   *  @return the longest prefix of this list whose elements satisfy
   *  the predicate <code>p</code>.
   */
  def takeWhile(p: a => Boolean): List[a] =
    if (isEmpty || !p(head)) Nil
    else head :: (tail takeWhile p);

  /** Returns the longest suffix of this list whose first element
   *  does not satisfy the predicate <code>p</code>.
   *
   *  @param p the test predicate.
   *  @return the longest suffix of the list whose first element
   *          does not satisfy the predicate <code>p</code>.
   */
  def dropWhile(p: a => Boolean): List[a] =
    if (isEmpty || !p(head)) this
    else tail dropWhile p;

  /** Returns the longest prefix of the list whose elements all satisfy
   *  the given predicate, and the rest of the list.
   *
   *  @param p the test predicate
   *  @return a pair consisting of the longest prefix of the list whose
   *  elements all satisfy <code>p</code>, and the rest of the list.
   */
  def span(p: a => Boolean): Pair[List[a], List[a]] = this match {
    case Nil => Pair(Nil, Nil)
    case head :: tail =>
      if (p(head)) {
        val Pair(tail1, tail2) = tail span p;
        Pair(head :: tail1, tail2)
      } else
        Pair(Nil, this)
  }

  /** Like <code>span</code> but with the predicate inverted.
   */
  def break(p: a => Boolean): Pair[List[a], List[a]] = span { x => !p(x) }

  /** Returns the <code>n</code>-th element of this list. The first element
   *  (head of the list) is at position 0.
   *
   *  @param n index of the element to return
   *  @return the element at position <code>n</code> in this list.
   *  @throws <code>java.lang.RuntimeException</code> if the list is too short.
   */
  def apply(n: Int): a = drop(n).head;

  /** Returns the list resulting from applying the given function <code>f</code> to each
   *  element of this list.
   *
   *  @param f function to apply to each element.
   *  @return <code>[f(a0), ..., f(an)]</code> if this list is <code>[a0, ..., an]</code>.
   */
  def map[b](f: a => b): List[b] = this match {
    case Nil => Nil
    case head :: tail => f(head) :: (tail map f)
  }

  /** Apply a function to all the elements of the list, and return the
   *  reversed list of results. This is equivalent to a call to <code>map</code>
   *  followed by a call to <code>reverse</code>, but more efficient.
   *
   *  @param f the function to apply to each elements.
   *  @return the reversed list of results.
   */
  def reverseMap[b](f: a => b): List[b] = {
    def loop(l: List[a], res: List[b]): List[b] = l match {
      case Nil => res
      case head :: tail => loop(tail, f(head) :: res)
    }
    loop(this, Nil)
  }

  /** Apply the given function <code>f</code> to each element of this list
   *  (while respecting the order of the elements).
   *
   *  @param f the treatment to apply to each element.
   */
  override def foreach(f: a => Unit): Unit = {
    def loop(xs: List[a]): Unit = xs match {
      case Nil => ()
      case head :: tail => f(head); loop(tail)
    }
    loop(this)
  }

  /** Returns all the elements of this list that satisfy the
   *  predicate <code>p</code>. The order of the elements is preserved.
   *
   *  @param p the redicate used to filter the list.
   *  @return the elements of this list satisfying <code>p</code>.
   */
  def filter(p: a => Boolean): List[a] = this match {
    case Nil => this
    case head :: tail =>
      if (p(head)) {
	val tail1 = tail filter p;
        if (tail eq tail1) this else head :: tail1
      } else tail filter p
  }

  /** Removes all elements of the list which satisfy the predicate
   *  <code>p</code>. This is like <code>filter</code> with the
   *  predicate inversed.
   *
   *  @param p the predicate to use to test elements
   *  @return the list without all elements which satisfy <code>p</code>
   */
  def remove(p: a => Boolean): List[a] = this match {
    case Nil => this
    case head :: tail =>
      if (p(head)) tail remove p else head :: (tail remove p)
  }

  /** Partition the list in two sub-lists according to a predicate.
   *
   *  @param p the predicate on which to partition
   *  @return a pair of lists: the list of all elements which satisfy
   *  <code>p</code> and the list of all elements which do not. The
   *  relative order of the elements in the sub-lists is the same as in
   *  the original list.
   */
  def partition(p: a => Boolean): Pair[List[a], List[a]] = this match {
    case Nil => Pair(Nil, Nil)
    case head :: tail =>
      val Pair(taily, tailn) = tail partition p;
      if (p(head)) Pair(head :: taily, tailn)
      else Pair(taily, head :: tailn)
  }

  /** Sort the list according to the comparison function
   *  <code>&lt;(e1: a, e2: a) =&gt; Boolean</code>,
   *  which should be true iff e1 is smaller than e2.
   *  Note: The current implementation is inefficent for
   *  already sorted lists.
   *
   *  @param lt the comparison function
   *  @return a list sorted according to the comparison function
   *          <code>&lt;(e1: a, e2: a) =&gt; Boolean</code>.
   */
  def sort(lt : (a,a) => Boolean): List[a] = {
    def sort_1(smaller: List[a], acc: List[a]): List[a] =
      smaller match {
        case Nil =>
          acc
        case List(x) =>
          x::acc
        case List(x, y) =>
          if (lt(x, y)) x::(y::acc) else y::x::acc
        case List(x, y, z) =>
          if (lt(x, y)) {
            if (lt(y, z)) x::y::z::acc
            else if (lt(x, z)) x::z::y::acc
            else z::x::y::acc
          } else if (lt(x, z)) y::x::z::acc
          else if (lt(z, y)) z::y::x::acc
          else y::z::x::acc
        case hd1::hd2::hd3::tail => {
          val List(x, y, z) = sort_1(hd1::hd2::hd3::Nil, Nil);
          val Pair(small, large) = tail.partition((e2) => lt(e2, y));
          sort_1(x::small, y::sort_1(z::large, acc))
        }
      }
    this match {
      case Nil =>
        this
      case List(x) =>
        this
      case List(x, y) =>
        if (lt(x, y)) this else y::x::Nil
      case List(x, y, z) =>
        if (lt(x, y)) {
          if (lt(y, z)) this
          else if (lt(x, z)) x::z::y::Nil
          else z::x::y::Nil
        } else if (lt(x, z)) y::x::z::Nil
        else if (lt(z, y)) z::y::x::Nil
        else y::z::x::Nil
      case hd1::hd2::hd3::tail => {
        val List(x, y, z) = sort_1(hd1::hd2::hd3::Nil, Nil);
        val Pair(small,large) =  tail.partition((e2) => lt(e2, y));
        sort_1(x::small, y::sort_1(z::large, Nil));
      }
    }
  }


  /** Count the number of elements in the list which satisfy a predicate.
   *
   *  @param p the predicate for which to count
   *  @return the number of elements satisfying the predicate <code>p</code>.
   */
  def count(p: a => Boolean): Int = this match {
    case Nil => 0
    case head :: tail => if (p(head)) 1 + (tail count p) else (tail count p)
  }

  /** Tests if the predicate <code>p</code> is satisfied by all elements
   *  in this list.
   *
   *  @param p the test predicate.
   *  @return True iff all elements of this list satisfy the predicate <code>p</code>.
   */
  override def forall(p: a => Boolean): Boolean =
    isEmpty || (p(head) && (tail forall p));

  /** Tests the existence in this list of an element that satisfies the predicate
   * <code>p</code>.
   *
   *  @param p the test predicate.
   *  @return true iff there exists an element in this list that satisfies
   *  the predicate <code>p</code>.
   */
  override def exists(p: a => Boolean): Boolean =
    !isEmpty && (p(head) || (tail exists p));

  /** Tests if the given value <code>elem</code> is a member of this
   *  iterable object.
   *
   *  @param elem element whose membership has to be tested.
   *  @return True iff there is an element of this list which is
   *  equal (w.r.t. <code>==</code>) to <code>elem</code>.
   */
  def contains(elem: Any): boolean = exists { x => x == elem }

  /** Find and return the first element of the list satisfying a
   *  predicate, if any.
   *
   *  @param p the predicate
   *  @return the first element in the list satisfying <code>p</code>,
   *  or <code>None</code> if none exists.
   */
  override def find(p: a => Boolean): Option[a] = this match {
    case Nil => None
    case head :: tail => if (p(head)) Some(head) else tail find p
  }

  /** Combines the elements of this list together using the binary
   *  operator <code>op</code>, from left to right, and starting with
   *  the value <code>z</code>.
   *
   *  @return <code>op(... (op(op(z,a0),a1) ...), an)</code> if the list
   *  is <code>[a0, a1, ..., an]</code>.
   */
  override def foldLeft[b](z: b)(f: (b, a) => b): b = this match {
    case Nil => z
    case x :: xs => xs.foldLeft[b](f(z, x))(f)
  }

  /** Combines the elements of this list together using the binary
   *  operator <code>op</code>, from rigth to left, and starting with
   *  the value <code>z</code>.
   *
   *  @return <code>a0 op (... op (an op z)...)</code> if the list
   *  is <code>[a0, a1, ..., an]</code>.
   */
  override def foldRight[b](z: b)(f: (a, b) => b): b = this match {
    case Nil => z
    case x :: xs => f(x, xs.foldRight(z)(f))
  }

  def reduceLeft[b >: a](f: (b, b) => b): b = this match {
    case Nil => error("Nil.reduceLeft")
    case x :: xs => ((xs: List[b]) foldLeft (x: b))(f)
  }

  def reduceRight[b >: a](f: (b, b) => b): b = this match {
    case Nil => error("Nil.reduceRight")
    case x :: Nil => x: b
    case x :: xs => f(x, xs reduceRight f)
  }

  /** Applies the given function <code>f</code> to each element of
   *  this list, then concatenates the results.
   *
   *  @param f the function to apply on each element.
   *  @return <code>f(a0) ::: ... ::: f(an)</code> if this list is
   *  <code>[a0, ..., an]</code>.
   */
  def flatMap[b](f: a => List[b]): List[b] = this match {
    case Nil => Nil
    case head :: tail => f(head) ::: (tail flatMap f)
  }

  /** Reverses the elements of this list.
   *  <p/>
   *  Ex: <br/>
   *  <code>[1, 2, 3] reverse = [3, 2, 1]</code>.
   *
   *  @return the elements of this list in reverse order.
   */
  def reverse: List[a] =
    foldLeft(Nil : List[a])((xs, x) => x :: xs);

  /** Returns a string representation of this list. The resulting string
   *  begins with the string <code>start</code> and is finished by the string
   *  <code>end</code>. Inside, the string representations of elements (w.r.t.
   *  the method <code>toString()</code>) are separated by the string
   *  <code>sep</code>.
   *  <p/>
   *  Ex: <br/>
   *  <code>List(1, 2, 3).mkString("(", "; ", ")") = "(1; 2; 3)"</code>
   *
   *  @param start starting string.
   *  @param sep separator string.
   *  @param end ending string.
   *  @return a string representation of this list.
   */
  def mkString(start: String, sep: String, end: String): String = this match {
    case Nil => start + end
    case last :: Nil => start + last + end
    case fst :: tail => start + fst + sep + tail.mkString("", sep, end)
  }

  override def toString() = mkString("List(", ",", ")");

  /** Returns a list formed from this list and the specified list
   *  <code>that</code> by associating each element of the former with
   *  the element at the same position in the latter.
   *
   *  @param <code>that</code> must have the same length as the self list.
   *  @return <code>[(a0,b0), ..., (an,bn)]</code> when
   *  <code>[a0, ..., an] zip [b0, ..., bn]</code> is invoked.
   */
  def zip[b](that: List[b]): List[Pair[a,b]] =
    if (this.isEmpty || that.isEmpty) Nil
    else Pair(this.head, that.head) :: this.tail.zip(that.tail);

  /** Returns a list formed from this list and the specified list
   *  <code>that</code> by associating each element of the former with
   *  the element at the same position in the latter.
   *
   *  @param <code>that</code> may have a different length as the self list.
   *  @param <code>thisElem</code> is used to fill up the resulting list if
   *  the self list is shorter than <code>that</code>
   *  @param <code>thatElem</code> is used to fill up the resulting list if
   *  <code>that</code> is shorter than the self list
   *  @return <code>[(a0,b0), ..., (an,bn), (elem,bn+1), ..., (elem,bm)]</code>
   *  when <code>[a0, ..., an] zip [b0, ..., bm]</code> is invoked where
   *  <code>m &gt; n</code>.
   */
  def zipAll[c >: a, b](that: List[b], thisElem: c, thatElem: b): List[Pair[c,b]] =
    if (this.isEmpty && that.isEmpty)
      Nil
    else if (this.isEmpty)
      List.make(that.length, thisElem) zip that
    else if (that.isEmpty)
      this zip List.make(this.length, thatElem)
    else
      Pair(this.head, that.head) :: this.tail.zipAll(that.tail, thisElem, thatElem);

  /** Computes the union of this list and the given list
   *  <code>that</code>.
   *
   *  @param that the list of elements to add to the list.
   *  @return a list without doubles containing the elements of this
   *  list and those of the given list <code>that</code>.
   */
  def union[b >: a](that: List[b]): List[b] = this match {
    case Nil => that
    case head :: tail =>
      if (that contains head) tail union that
      else head :: (tail union that)
  }

  /** Computes the difference between this list and the given list
   *  <code>that</code>.
   *
   *  @param that the list of elements to remove from this list.
   *  @return this list without the elements of the given list <code>that</code>.
   */
  def diff[b >: a](that: List[b]): List[b] = this match {
    case Nil => Nil
    case head :: tail =>
      if (that contains head) tail diff that
      else head :: (tail diff that)
  }

  /** Computes the intersection between this list and the given list
   *  <code>that</code>.
   *
   *  @param that the list to intersect.
   *  @return the list of elements contained both in this list and
   *          in the given list <code>that</code>.
   */
  def intersect[b >: a](that: List[b]): List[b] = filter(x => that contains x);

  /** Removes redundant elements from the list. Uses the method <code>==</code>
   *  to decide if two elements are identical.
   *
   *  @return the list without doubles.
   */
  def removeDuplicates: List[a] = this match {
    case Nil => this
    case head :: tail =>
      if (tail contains head) tail.removeDuplicates
      else head :: tail.removeDuplicates
  }
}

/** The empty list.
 *
 *  @author  Martin Odersky
 *  @version 1.0, 15/07/2003
 */
[SerialVersionUID(0 - 8256821097970055419L)]
case object Nil extends List[All] {
  def isEmpty = true;
  def head: All = error("head of empty list");
  def tail: List[All] = error("tail of empty list");
}

/** A non empty list characterized by a head and a tail.
 *
 *  @author  Martin Odersky
 *  @version 1.0, 15/07/2003
 */
[SerialVersionUID(0L - 8476791151983527571L)]
final case class ::[+b](hd: b, tl: List[b]) extends List[b] {
  def isEmpty: boolean = false;
  def head: b = hd;
  def tail: List[b] = tl;
}

