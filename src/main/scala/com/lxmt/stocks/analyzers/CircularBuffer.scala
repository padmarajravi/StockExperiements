package com.lxmt.stocks.analyzers

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by ravi on 24/05/2017.
 */
class CircularBuffer[T](size:Int) {
  val storage = ListBuffer[T]()
  def add(t:T) = {
    if(storage.size==size) {
      storage.remove(0)
    }
    storage+=t
  }

  def get(index:Int) = storage(index)
  def getAll()=storage.toList
  def getSize() = storage.size
}

object CircularBuffer {
  def apply[T](n:Int) = new CircularBuffer[T](n)
}


