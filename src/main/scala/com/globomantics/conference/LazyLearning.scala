package com.globomantics.conference

/**
 * To run:
 *  Uncomment the `extends App` part from object declaration.
 */
object LazyLearning /* extends App */ {

  def timed[T](code: T): (T, Long) = {

    val start  = System.nanoTime()
    val result = code
    val end    = System.nanoTime()

    (result, (end - start)/100000)
  }

  def timed2[T](code: => T): (T, Long) = {

    val start  = System.nanoTime()
    val result = code
    val end    = System.nanoTime()

    (result, (end - start)/100000)
  }

  val timedCallByValue: (Int, Long) =
    timed {
      Thread.sleep(2000)
      15
    }

  val timedCallByName: (Int, Long) =
    timed2 {
      Thread.sleep(2000)
      15
    }

  //  println(s"timedCallByValue: $timedCallByValue")

  println(s"timedCallByName: $timedCallByName")
}