package scala.runtime.matching ;

object Address {
  def empty = new Address();
}

//import List.list2ordered;

/** Address holds the path in reverse Dewey notation
*/
class Address( l:Int* ) extends Ordered[Address] {

  private val list:List[Int] = l.toList;

  def compareTo [b >: Address <% Ordered[b]](y: b): int = y match {
    case o:Address => list.reverse.compareTo(o.list.reverse)
			    //(xs => List.view(xs)(Predef.int2ordered));
    case _ => -(y compareTo this)
  }

  def down: Address = new Address( ( 1 :: list ):_* );

  /** precond: p is nonempty */
  def right: Address = list match {
    case i :: rest => new Address( ((i+1) :: rest ):_* )
  }

  override def toString() = list.mkString("Address(",".",")");

}
