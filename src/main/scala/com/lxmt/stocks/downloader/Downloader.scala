package com.lxmt.stocks.downloader

import com.lxmt.stocks.database.DataBaseServiceFactory
import com.lxmt.stocks.database.DataBaseServiceFactory.DataBaseType
import com.lxmt.stocks.database.DataBaseServiceFactory._
import org.joda.time.DateTime
import scala.io.Source




/**
 * Created by ravi on 24/01/2017.
 */
object Downloader {

  def ensureDataStorage():List[String] = {
    val graphPath="target/graphdata";
    val lines              = Source.fromFile("/Users/ravi/StockExperiements/src/main/resources/stocks.csv","utf-8").getLines()
    val dateFormat         = "yyyy-MM-dd"
    val currentDate        = new DateTime()
    val currDateStr        = currentDate.toString(dateFormat)
    val databaseService    = DataBaseServiceFactory.getDataBase(DataBaseType.Neo4j)

    databaseService.initiate()

    val transaction=lines.map(f => databaseService.createEntity(Map("name"-> f)))

   // println(transaction.toList)

    def getStockPrice(stock:String):List[String] ={
      val endDate = currentDate.toString(dateFormat)
      val startDate=currentDate.minusDays(15).toString(dateFormat)
      val url=
        s"""https://www.quandl.com/api/v3/datasets/NSE/${stock}.csv?start_date=${startDate}&end_date=${endDate}&api_key=-F8rNzaGiRs1tshijzFU"""
      println("Accessing url:"+url)
      Source.fromURL(url).getLines().toList.tail
    }

    val result=databaseService.getEntity("name","ABB")
    println(result.size)
    result.map( f => f.entityId)
  }

  def main(args: Array[String]) {

  }

}
