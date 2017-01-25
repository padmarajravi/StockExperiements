package com.ls.stocks.downloader

import scala.io.Source


/**
 * Created by ravi on 24/01/2017.
 */
object Downloader {

  def main(args: Array[String]) {
    val lines = Source.fromFile("resources/stocks.csv","utf-8").getLines
    lines.foreach(println(_))

  }

}
