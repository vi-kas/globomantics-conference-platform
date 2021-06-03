package com.globomantics.conference

object MemoizedLearning /*extends App*/ {

  import com.github.t3hnar.bcrypt._

  def memo[X, Y](op: X => Y): X => Y = {
    val cache = scala.collection.mutable.Map[X, Y]()
    (x: X) => cache.getOrElseUpdate(x, op(x))
  }

  val simpleBcrypt: String => String = txt => txt.bcrypt

  val memoizedBcrypt: String => String = memo(txt => txt.bcrypt)

  val t1 = timed {
    memoizedBcrypt("foo")
  }

  val t2 = timed {
    memoizedBcrypt("foo")
  }

  println(t1)
  println(t2)

  /**
   * Function to measure time taken for evaluating a block of expression
   */
  def timed[T](code: => T) = {
    val start = System.nanoTime()
    val result = code
    val end = System.nanoTime()

    (result, (end - start)/100000)
  }
}
