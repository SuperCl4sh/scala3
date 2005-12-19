/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2002, LAMP/EPFL                  **
**  __\ \/ /__/ __ |/ /__/ __ |                                         **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

// $Id$

package scala;

/** @meta class extends scala.AnyVal; */
public abstract class Double  extends AnyVal implements java.io.Serializable {

    public final double  value;

    public Double (double  value) {
        this.value = value;
    }

    public boolean equals(java.lang.Object other) {
        return other instanceof Double  && value == ((Double )other).value;
    }
    public int hashCode() {
        long bits = java.lang.Double.doubleToLongBits(value);
        return (int)(bits ^ (bits >>> 32));
    }
    public String toString() {
        return String.valueOf(value);
    }

    /** @meta method []scala.Byte; */
    public byte toByte() { return (byte)value; }

    /** @meta method []scala.Short; */
    public short toShort() { return (short)value; }

    /** @meta method []scala.Char; */
    public char toChar() { return (char)value; }

    /** @meta method []scala.Int; */
    public int toInt() { return (int)value; }

    /** @meta method []scala.Long; */
    public long toLong() { return (long)value; }

    /** @meta method []scala.Float; */
    public float toFloat() { return (float)value; }

    /** @meta method []scala.Double; */
    public double toDouble() { return (double)value; }

    /** @meta method (scala.Any)scala.Boolean; */
    public boolean $eq$eq  (java.lang.Object other) { return  equals(other); }
    /** @meta method (scala.Any)scala.Boolean; */
    public boolean $bang$eq(java.lang.Object other) { return !equals(other); }

    /** @meta method []scala.Double ; */
    public double  $plus      (            ) { return +value        ; }
    /** @meta method []scala.Double ; */
    public double  $minus     (            ) { return -value        ; }

    public String  $plus      (String  that) { return  value +  that; }

    public boolean $eq$eq     (double  that) { return  value == that; }
    public boolean $bang$eq   (double  that) { return  value != that; }
    public boolean $less      (double  that) { return  value <  that; }
    public boolean $greater   (double  that) { return  value >  that; }
    public boolean $less$eq   (double  that) { return  value <= that; }
    public boolean $greater$eq(double  that) { return  value >= that; }
    public double  $plus      (double  that) { return  value +  that; }
    public double  $minus     (double  that) { return  value -  that; }
    public double  $times     (double  that) { return  value *  that; }
    public double  $div       (double  that) { return  value /  that; }
    public double  $percent   (double  that) { return  value %  that; }

}
