package com.lxmt.stocks.downloader

import java.io.File

import gremlin.scala._
import org.apache.commons.io.FileUtils
import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph
import org.joda.time.DateTime

import scala.io.Source
import scala.util.Try



/**
 * Created by ravi on 24/01/2017.
 */
object Downloader {

  def ensureDataStorage() {
    val graphPath="graphdata/";
    FileUtils.deleteDirectory(new File(graphPath))
    val lines              = Source.fromFile("resources/stocks.csv","utf-8").getLines
    val graph:Neo4jGraph   = Neo4jGraph.open("graphdata/")
    val name               = Key[String]("name")
    val close              = Key[Double]("close")
    val open               = Key[Double]("close")
    val high               = Key[Double]("close")
    val low                = Key[Double]("close")
    val date               = Key[String]("date")
    val dateFormat         = "yyyy-MM-dd"
    val currentDate        = new DateTime()
    val currDateStr        = currentDate.toString(dateFormat)


    def addStock(stock:String) = {
      if(!graph.V.has(name,stock).exists) graph + ("stock",name -> stock)
      println("Added "+stock)
    }

    def addStockPrice(stockName:String,downloadLine:String): Neo4jGraph =
    {
      val downloadLineValues=downloadLine.split(",")
      val price = graph +("price",
        date  -> downloadLineValues(0).toString,
        open  -> downloadLineValues(1).toString.toDouble,
        high  -> downloadLineValues(2).toString.toDouble,
        low   -> downloadLineValues(3).toString.toDouble,
        close -> downloadLineValues(4).toString.toDouble
        )
      val stockNode=graph.V.has(name,stockName).head()
      stockNode --- s"hasPrice${downloadLineValues(0).toString}" --> price
      println("Created:"+stockName+s"--hasPrice${downloadLineValues(0).toString}-->"+downloadLineValues(4).toString)
      graph
    }

    def getStockPrice(stock:String):List[String] ={
      val endDate = currentDate.toString(dateFormat)
      val startDate=currentDate.minusDays(15).toString(dateFormat)
      val url=
        s"""https://www.quandl.com/api/v3/datasets/NSE/${stock}.csv?start_date=${startDate}&end_date=${endDate}&api_key=-F8rNzaGiRs1tshijzFU"""
      println("Accessing url:"+url)
      Source.fromURL(url).getLines().toList.tail
    }


    val x =for{
      stockName     <- lines
      node          =  addStock(stockName)
      dataList      <- Try(getStockPrice(stockName)).toOption.getOrElse(List())
      graph         =  addStockPrice(stockName,dataList)
    } yield stockName

    x.foreach(println(_))
    println(graph.V.has(name,"ABB").exists())
    println("Done")
    graph.close()
  }



}
