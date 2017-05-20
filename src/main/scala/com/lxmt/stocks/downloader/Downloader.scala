package com.lxmt.stocks.downloader


import com.lxmt.stocks.database.{DataBaseService, DataBaseServiceFactory}
import com.lxmt.stocks.database.DataBaseServiceFactory.DataBaseType
import com.lxmt.stocks.Util._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.io.Source
import scala.util.Try



/**
 * Created by ravi on 24/01/2017.
 */
object Downloader {


  val dateFormat = "yyyy-MM-dd"
  val nameLabel = "name"
  val priceLabel = "close"
  val priceRelationLabel = "hasPrice"
  val dataStorageWindow = 15


  case class DownloadedData(company:String,date:DateTime,open:Double,high:Double,low:Double,close:Double)

  def getStockPrice(stock: String, startDate: String,endDate:String): Option[List[DownloadedData]] = {
    val url =
      s"""https://www.quandl.com/api/v3/datasets/NSE/${stock}.csv?start_date=${startDate}&end_date=${endDate}&api_key=-F8rNzaGiRs1tshijzFU"""
    println("Accessing url:" + url)
    Try(Source.fromURL(url).getLines().toList.tail.map { p =>
      val priceRowSplit = p.split(",")
      val date = DateTimeFormat.forPattern(dateFormat).parseDateTime(priceRowSplit(0))
      DownloadedData(stock, date, priceRowSplit(1).toDouble, priceRowSplit(2).toDouble, priceRowSplit(2).toDouble, priceRowSplit(4).toDouble)
    }).toOption
  }

  def ensureDataStorage(databaseService:DataBaseService,stockList:Iterator[String] ):List[String] = {

    try {
      val downloadStartDate = databaseService.getLastSyncDate().toDownloadStartDateStr
      println("Last Sync Date:"+downloadStartDate)
      val x = for {
        stock       <- stockList
        stockEntity = databaseService.createCompanyEntity(stock, Map(nameLabel -> stock))
        stockData   <- getStockPrice(stock, downloadStartDate,currentDateString).getOrElse(List())
        priceEntity = databaseService.createPriceEntity(stockEntity, stockData)
      } yield priceEntity

      println("Inserted " + x.size + " relations")

      val result = databaseService.getLatestPriceForCompany("ABB","8KMILES")
      result.map(_.toString)
    }
    catch {
      case e: Exception => {
        databaseService.shutDown()
        List()
      }
    }
  }




}
