package com.exp

import org.scalatest._

/**
 * Created by leon on 15-7-25.
 */
class TestExpEval extends FunSuite {

  import ExpEval._

  test("exp-eval") {
    var exprs = """
                  |a = 3
                  |b = a + 1
                  |c = b * 2
                  |d = 0
                  |e = c / d
                """.stripMargin
    assert(eval(parse(exprs)).toString === "Map(e -> NaN, a -> 3.0, b -> 4.0, c -> 8.0, d -> 0.0)")

    exprs = """
              |a = b
              |b = c
              |c = a
            """.stripMargin
    assert(eval(parse(exprs)).toString === "Map(a -> NaN, b -> NaN, c -> NaN)")

    exprs = """a = a
              |b = 2
              |c = a + b
              |d = c + b""".stripMargin
    assert(eval(parse(exprs)).toString === "Map(a -> NaN, b -> 2.0, c -> NaN, d -> NaN)")

    exprs = """a = b
              |b = a""".stripMargin
    assert(eval(parse(exprs)).toString === "Map(a -> NaN, b -> NaN)")

    exprs = """a = a + b
              |b = 0""".stripMargin
    assert(eval(parse(exprs)).toString === "Map(a -> NaN, b -> 0.0)")

    exprs = """a = c + d
              |b = 0""".stripMargin
    assert(eval(parse(exprs)).toString === "Map(a -> NaN, b -> 0.0)")

    exprs = """a = b
              |b = c
              |c = c""".stripMargin
    assert(eval(parse(exprs)).toString === "Map(a -> NaN, b -> NaN, c -> NaN)")

    exprs = """a = b
              |b = c
              |c = c
              |c = 1""".stripMargin
    assert(eval(parse(exprs)).toString === "Map(a -> 1.0, b -> 1.0, c -> 1.0)")

    exprs = """a = b
              |b = c
              |c = 1
              |c = c""".stripMargin
    assert(eval(parse(exprs)).toString === "Map(a -> NaN, b -> NaN, c -> NaN)")
  }
}
