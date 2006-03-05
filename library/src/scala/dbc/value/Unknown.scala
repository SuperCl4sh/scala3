/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2006, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |                                         **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

// $Id$


package scala.dbc.value;


abstract class Unknown extends Value {

	val dataType: datatype.Unknown;

	def sqlString = error ("An 'ANY' value cannot be represented.");

}

object UnknownType {

	def view (obj:value.Unknown): Object = obj.nativeValue;

}
