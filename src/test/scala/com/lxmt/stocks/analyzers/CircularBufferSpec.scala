package com.lxmt.stocks.analyzers

import org.scalatest.{Matchers, FlatSpec}

import scala.collection.immutable.List

/**
 * Created by ravi on 3/06/2017.
 */
class CircularBufferSpec extends FlatSpec with Matchers{
  {
    "Circular Buffer " should "always have the last n elements inserted to it" in {
      val buffer = CircularBuffer[Int](5)
      buffer.add(1)
      buffer.add(2)
      buffer.add(3)
      buffer.add(4)
      buffer.add(5)
      buffer.add(6)
      buffer.add(7)
      buffer.add(8)
      buffer.getAll() should be (List(4,5,6,7,8))

    }
  }
}
