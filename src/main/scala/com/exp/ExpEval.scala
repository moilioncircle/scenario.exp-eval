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
