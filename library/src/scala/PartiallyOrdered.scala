/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-04, LAMP/EPFL               **
**  __\ \/ /__/ __ |/ /__/ __ |                                         **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
** $Id:PartiallyOrdered.scala 5359 2005-12-16 16:33:49 +0100 (Fri, 16 Dec 2005) dubochet $
\*                                                                      */

package scala;


/** A trait for partially ordered data.
 *
 *  @author  Martin Odersky
 *  @version 1.0, 23/04/2004
 */
trait PartiallyOrdered[+a] {

  /** Result of comparing `this' with operand `that'.
   *  Returns `None' if operands are not comparable.
   *  If operands are comparable, returns `Some(x)' where
   *  <code>x &lt; 0</code>    iff    <code>this &lt; that</code>
   *  <code>x == 0</code>   iff    <code>this == that</code>
   *  <code>x &gt; 0</code>    iff    <code>this &gt; that</code>
   */
  def tryCompareTo [b >: a <% PartiallyOrdered[b]](that: b): Option[int];

  def <  [b >: a <% PartiallyOrdered[b]](that: b): boolean =
    (this tryCompareTo that) match {
      case Some(x) if x < 0 => true
      case _ => false
    }
  def >  [b >: a <% PartiallyOrdered[b]](that: b): boolean =
    (this tryCompareTo that) match {
      case Some(x) if x > 0 => true
      case _ => false
    }
  def <= [b >: a <% PartiallyOrdered[b]](that: b): boolean =
    (this tryCompareTo that) match {
      case Some(x) if x <= 0 => true
      case _ => false
    }
  def >= [b >: a <% PartiallyOrdered[b]](that: b): boolean =
    (this tryCompareTo that) match {
      case Some(x) if x >= 0 => true
      case _ => false
    }
}
