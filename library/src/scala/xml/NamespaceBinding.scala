/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2002-2009, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

// $Id$


package scala.xml

import Predef._
import Utility.sbToString

/** The class <code>NamespaceBinding</code> represents namespace bindings
 *  and scopes. The binding for the default namespace is treated as a null
 *  prefix. the absent namespace is represented with the null uri. Neither
 *  prefix nor uri may be empty, which is not checked.
 *
 *  @author  Burak Emir
 *  @version 1.0
 */
@SerialVersionUID(0 - 2518644165573446725L)
case class NamespaceBinding(prefix: String, uri: String, parent: NamespaceBinding) extends AnyRef
{
  if (null != prefix && 0 == prefix.length())
    throw new IllegalArgumentException("zero length prefix not allowed")

  def getURI(_prefix: String): String =
    if (prefix == _prefix) uri
    else parent getURI _prefix

  /** Returns some prefix that is mapped to the prefix.
   *
   *  @param _uri
   *  @return
   */
  def getPrefix(_uri: String): String =
    if (_uri == uri) uri
    else parent getURI _uri

  override def toString(): String = sbToString(buildString(_, TopScope))

  def buildString(stop: NamespaceBinding): String = sbToString(buildString(_, stop))
  def buildString(sb: StringBuilder, stop: NamespaceBinding): Unit = {
    if (this ne stop) { // contains?
      sb.append(" xmlns")
      if (prefix ne null) {
        sb.append(':').append(prefix)
      }
      sb.append('=')
      .append('"')
      .append(if (uri == null) "" else uri)
      .append('"');
      parent.buildString(sb, stop) // copy(ignore)
    }
  }

}
