package com.exp

import scala.util.{Failure, Success, Try}

/**
 * Created by leon on 15-7-25.
 */
object ExpEval {

  def parse(rawInput: String): Map[String, Exp] = {
    rawInput.split("\n").map(parseLine).filterNot(_ == None).map(_.get).toMap
  }

  def parseLine(line: String): Option[(String, Exp)] = {
    line.split(" ").filter(_.trim != "").toList match {
      case (name: String) :: "=" :: (exp: List[String]) => Some((name, parseExp(name, exp.mkString(" "))))
      case Nil => None
      case e => throw new IllegalArgumentException(s"parse error $e")
    }
  }

  def parseExp(name: String, exp: String): Exp = {
    exp.split(" ").filter(_.trim != "").toList match {
      case List(left: String, "+", right: String) => Plus(parseExp(name, left), parseExp(name, right))
      case List(left: String, "-", right: String) => Minus(parseExp(name, left), parseExp(name, right))
      case List(left: String, "*", right: String) => Times(parseExp(name, left), parseExp(name, right))
      case List(left: String, "/", right: String) => Divide(parseExp(name, left), parseExp(name, right))
      case List(value: String) => if (isDouble(value)) Literal(value.toDouble) else if (name == value) Literal(Double.NaN) else Ref(value)
      case Nil => Literal(Double.NaN)
      case e => throw new IllegalArgumentException(s"parse error $e")
    }
  }

  def isDouble(value: String): Boolean = {
    Try(value.toDouble) match {
      case Success(v) => true
      case Failure(e) => false
    }
  }

  def eval(map: Map[String, Exp]): Map[String, Double] = {
    map.map(e => (e._1 -> evalExp(e._2, map)))
  }

  /**
   * the method you need to implementation
   * @param exp
   * @param map
   * @return
   */
  def evalExp(exp: Exp, map: Map[String, Exp]): Double = {
    Double.NaN
  }

}

abstract class Exp

case class Literal(v: Double) extends Exp

case class Plus(l: Exp, r: Exp) extends Exp

case class Minus(l: Exp, r: Exp) extends Exp

case class Times(l: Exp, r: Exp) extends Exp

case class Divide(l: Exp, r: Exp) extends Exp

case class Ref(name: String) extends Exp
