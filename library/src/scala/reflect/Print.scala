/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2002-2005, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |                                         **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

// $Id$

package scala.reflect;

object Print extends Function1[Any, String] {

  def apply (any: Any): String = {
    if (any.isInstanceOf[TypedCode[Any]])
      apply(any.asInstanceOf[TypedCode[Any]])
    else if (any.isInstanceOf[Code])
      apply(any.asInstanceOf[Code])
    else if (any.isInstanceOf[Symbol])
      apply(any.asInstanceOf[Symbol])
    else if (any.isInstanceOf[Type])
      apply(any.asInstanceOf[Type])
    else "UnknownAny"
  }

  def apply (typedCode: TypedCode[Any]): String =
    Print(typedCode.code);

  def apply (code: Code): String = code match {
    case reflect.Ident(sym) => Print(sym)
    case reflect.Select(qual, sym) => "(" + Print(qual) + "." + Print(sym) + ")"
    case reflect.Literal(value) => "(value: " + value.toString + ")"
    case reflect.Apply(fun, args) => Print(fun) + args.map(Print).mkString("(", ", ", ")")
    case reflect.TypeApply(fun, args) => Print(fun) + args.map(Print).mkString("[", ", ", "]")
    case reflect.Function(params, body) => "(" + params.map(Print).mkString("(", ", ", ")") + " => " + Print(body) + ")"
    case reflect.This(sym) => "(" + Print(sym) + ".this)"
    case reflect.Block(stats, expr) => (stats ::: List(expr)).map(Print).mkString("{", ";\n", "}")
    case reflect.New(clazz) => "(new " + Print(clazz) + ")"
    case _ => "???"
  }

  def apply (symbol: Symbol): String = symbol match {
    case reflect.Class(name) => "(class: " + name + ")"
    case reflect.Method(name, datatype) => "(method: " + name + ")" //+ ": " + datatype
    case reflect.Field(name, datatype) => "(field: " + name + ")" //+ ": " + datatype
    case reflect.TypeField(name, datatype) => "(typefield: " + name + ")" //+ ": " + datatype
    case reflect.LocalValue(owner, name, datatype) => "(lvalue: " + name + ")" //+ ": " + datatype
    case reflect.LocalMethod(owner, name, datatype) => "(lmethod: " + name + ")" //+ ": " + datatype
    case reflect.NoSymbol => "NoSymbol"
    case reflect.RootSymbol => "RootSymbol"
    case _ => "???"
  }

  def apply (datatype: Type): String = datatype match {
    case reflect.NoPrefix => "NoPrefix"
    case reflect.NoType => "NoType"
    case reflect.NamedType(name) => "(named: " + name + ")"
    case reflect.PrefixedType(prefix, symbol) => "(" + Print(prefix) + "." + Print(symbol) + ")"
    case reflect.SingleType(prefix, symbol) => "(" + Print(prefix) + "." + Print(symbol) + ")"
    case reflect.ThisType(clazz) => "(" + Print(clazz) + ".this.type)"
    case reflect.AppliedType(datatype, args) => Print(datatype) + args.map(Print).mkString("[", ", ", "]")
    case reflect.TypeBounds(lo, hi) => "[" + Print(lo) + " ... " + Print(hi) + "]"
    case reflect.MethodType(formals, resultType) => formals.map(Print).mkString("(", ", ", ")") + " => " + Print(resultType)
    case reflect.PolyType(typeParams, typeBounds, resultType) =>
      (List.map2(typeParams, typeBounds)
        ((tp, tb) => "[" + Print(tb._1) + " :> " + Print(tp) + " :> " + Print(tb._2) + "]")).
          mkString("[", ", ", "]") + " -> " + Print(resultType)
    case _ => "???"
  }

}
