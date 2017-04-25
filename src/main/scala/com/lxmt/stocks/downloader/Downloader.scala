package com.lxmt.stocks.downloader

import java.io.IOException

import com.lxmt.stocks.database.DataBaseServiceFactory
import com.lxmt.stocks.database.DataBaseServiceFactory.DataBaseType
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.io.Source
import scala.util.Try


/**
 * Created by ravi on 24/01/2017.
 */
object Downloader {

  def ensureDataStorage():List[String] = {
    val graphPath="target/graphdata";
    val lines              = Source.fromFile("/Users/ravi/StockExperiements/src/main/resources/stocks_small","utf-8").getLines()
    val dateFormat         = "yyyy-MM-dd"
    val currentDate        = new DateTime()
    val currDateStr        = currentDate.toString(dateFormat)
    val databaseService    = DataBaseServiceFactory.getDataBase(DataBaseType.Neo4j)
    val nameLabel          = "name"
    val priceLabel         = "close"
    val priceRelationLabel = "hasPrice"
    val dataStorageWindow  = 15

    databaseService.initiate()


    def getStockPrice(stock:String):Option[List[DownloadedData]] ={
      val endDate = currentDate.toString(dateFormat)
      val startDate=currentDate.minusDays(dataStorageWindow).toString(dateFormat)
      val url=
        s"""https://www.quandl.com/api/v3/datasets/NSE/${stock}.csv?start_date=${startDate}&end_date=${endDate}&api_key=-F8rNzaGiRs1tshijzFU"""
      println("Accessing url:"+url)
      Try(Source.fromURL(url).getLines().toList.tail.map{ p =>
          val priceRowSplit = p.split(",")
          val date          =DateTimeFormat.forPattern(dateFormat).parseDateTime(priceRowSplit(0))
          DownloadedData(stock,date,priceRowSplit(1).toDouble,priceRowSplit(1).toDouble,priceRowSplit(1).toDouble,priceRowSplit(1).toDouble)
        }).toOption
      }




    val x= for {
      stock                 <- lines
      stockEntity           =  databaseService.createCompanyEntity(stock,Map(nameLabel-> stock))
      stockData             <- getStockPrice(stock).getOrElse(List())
      priceEntity           =  databaseService.createPriceEntity(stockEntity,stockData)
    } yield priceEntity

    println("Inserted "+x.size+" relations")

    val result = databaseService.getCompanies("name","8KMILES")
    println("Search result size :"+result.toList.size)
    result.map(_.toString)
  }

  case class DownloadedData(company:String,date:DateTime,open:Double,high:Double,low:Double,close:Double)

  def main(args: Array[String]) {

  }

}
