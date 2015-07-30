package com.moilioncircle

import scala.util.{Failure, Success, Try}

/**
 * Created by leon on 15-7-25.
 */
object ExpEval {

  /**
   * *************************************
   * replace ??? with your implementation.
   * *************************************
   * for example, the expressions are :
   *    a = 1 + b
   *    b = 2
   *
   * if you debug at evalExp, you will get something like following
   *
   * times 1:
   * evalExp(exp , map) = 3.0
   * exp = Plus(Literal(1), Ref("b"))
   * map = Map("a" -> Plus(Literal(1), Ref("b")), "b" -> Literal(2))
   *
   * times 2:
   * evalExp(exp , map) = 2.0
   * exp = Literal(2)
   * map = Map("a" -> Plus(Literal(1), Ref("b")), "b" -> Literal(2))
   *
   * @param exp the current expression.
   * @param map all expressions. map.key is Ref.name, map.value is ref-Exp
   * @return the value of current expression.
   */

  def evalExp(exp: Exp, map: Map[String, Exp]): Double = exp match{
      case Literal(x) => x
      case Ref(x) => {if (map.exists(_._1 == x)) evalExp(map.find(_._1 == x).get._2,map.-(x)) else Double.NaN}
      case Plus(x,y)=>evalExp(x,map)+evalExp(y,map)
      case Minus(x,y)=>evalExp(x,map)-evalExp(y,map)
      case Times(x,y)=>evalExp(x,map)*evalExp(y,map)
      case Divide(x,y)=>{if (evalExp(y,map)==0) Double.NaN else evalExp(x,map)/evalExp(y,map)}
    }

  def parse(rawInput: String): Map[String, Exp] = {
    rawInput.split("\n").map(parseLine).filterNot(_ == None).map(_.get).toMap
  }

  def parseLine(line: String): Option[(String, Exp)] = {
    line.split(" ").map(_.trim).filter(_ != "").toList match {
      case (name: String) :: "=" :: (exp: List[String]) => Some((name, parseExp(name, exp.mkString(" "))))
      case Nil => None
      case e => throw new IllegalArgumentException(s"parse error $e")
    }
  }

  def parseExp(name: String, exp: String): Exp = {
    exp.split(" ").map(_.trim).filter(_ != "").toList match {
      case List(left: String, "+", right: String) => Plus(parseRef(name, left), parseRef(name, right))
      case List(left: String, "-", right: String) => Minus(parseRef(name, left), parseRef(name, right))
      case List(left: String, "*", right: String) => Times(parseRef(name, left), parseRef(name, right))
      case List(left: String, "/", right: String) => Divide(parseRef(name, left), parseRef(name, right))
      case List(value: String) => parseRef(name, value)
      case Nil => Literal(Double.NaN)
      case e => throw new IllegalArgumentException(s"parse error $e")
    }
  }

  def parseRef(name: String, value: String): Exp = {
    if (isDouble(value)) Literal(value.toDouble) else if (name == value) Literal(Double.NaN) else Ref(value)
  }

  def isDouble(value: String): Boolean = {
    Try(value.toDouble) match {
      case Success(v) => true
      case Failure(e) => false
    }
  }

  def eval(map: Map[String, Exp]): Map[String, Double] = {
    map.map {
      case (name, exp) => (name -> evalExp(exp, map))
    }
  }

}

abstract class Exp

case class Literal(v: Double) extends Exp

case class Plus(l: Exp, r: Exp) extends Exp

case class Minus(l: Exp, r: Exp) extends Exp

case class Times(l: Exp, r: Exp) extends Exp

case class Divide(l: Exp, r: Exp) extends Exp

case class Ref(name: String) extends Exp
