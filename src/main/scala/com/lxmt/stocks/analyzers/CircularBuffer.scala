package com.lxmt.stocks.analyzers

import scala.collection.mutable

/**
 * Created by ravi on 24/05/2017.
 */
class CircularBuffer[T](size:Int) {
  val storage = mutable.MutableList[T]()
  def add(t:T) = {
    if(storage.size==size) {
      storage.drop(0)
    }
    storage+=t
  }

  def get(index:Int) = storage.get(index)
  def getAll()=storage.toList
  def getSize() = storage.size
}

object CircularBuffer {
  def apply[T](n:Int) = new CircularBuffer[T](n)
}


