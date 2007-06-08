/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2002-2007, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

// $Id$

package scala.runtime

final class RichInt(start: Int) extends Proxy with Ordered[Int] {

  // Proxy
  def self: Any = start

  // Ordered[Int]
  def compare(that: Int): Int = if (start < that) -1 else if (start > that) 1 else 0

  /** See <code>Iterator.range</code>. */
  def until(end: Int): Range = Iterator.range(start, end)

  /** See <code>Iterator.range</code>. */
  def until(end: Int, step: Int): Range = Iterator.range(start, end, step)

  /** like <code>until</code>, but includes the last index */
  def to(end: Int) = until(end + 1)

  def min(that: Int): Int = if (start < that) start else that
  def max(that: Int): Int = if (start > that) start else that
  def abs: Int = if (start < 0) -start else start

}
