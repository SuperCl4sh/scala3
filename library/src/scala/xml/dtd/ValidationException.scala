package scala.xml.dtd ;

case class ValidationException( e:String ) extends Exception( e );

object MakeValidationException {
  def fromFixedAttribute( k: String, value: String, actual: String ) =
    ValidationException("value of attribute " + k + " FIXED to \""+value+"\", but document tries \""+actual+"\"");

  def fromNonEmptyElement() = {
    new ValidationException("element should be *empty*");
  }
  def fromUndefinedElement( label:String ) =
    new ValidationException("element \""+ label +"\" not allowed here");

  def fromUndefinedAttribute( key:String ) =
    new ValidationException("attribute " + key +" not allowed here" );

  def fromMissingAttribute( allKeys:scala.collection.Set[String] ) = {
    val sb = new StringBuffer();
    sb.append("missing value for REQUIRED attribute");
    if( allKeys.size > 1 ) sb.append('s');
    val it = allKeys.elements;
    while (it.hasNext) {
      sb.append('\'').append(it.next).append('\'')
    }
    new ValidationException(sb.toString());
  }

  def fromMissingAttribute( key: String, tpe: String ) = {
    new ValidationException("missing value for REQUIRED attribute "+key+" of type "+tpe);
  }

}
