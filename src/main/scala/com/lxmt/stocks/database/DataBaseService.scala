package com.lxmt.stocks.database

import com.lxmt.stocks.downloader.Downloader.DownloadedData
import org.joda.time.DateTime
import play.api.libs.json.Json

/**
 * Created by ravi on 14/03/2017.
 */
trait DataBaseService {
  def clearDataBase():Boolean
  def createCompanyEntity(name:String,propMap:Map[String,String]):Company
  def createPriceEntity(companyEntity:Company,downloadedData: DownloadedData):Boolean
  def getCompanies(propName:String,propValue:Object):List[Company]
  def getLatestPriceForCompany(companyName:String*):List[PriceData]
  def getLastNDayPrices(companyName:String,numberOfIntervals:Int):List[PriceData]
  def getLastSyncDate():String
  def shutDown()
}

case class Company(id:String,name:String)
case class PriceData(id:String,company:String,dateTime: DateTime,open:Double,high:Double,low:Double,close:Double) {
  implicit val jsonFormat = Json.format[PriceData]
  def toJson()={
    s"""{"no":${id},"company":\"${company}\","date":\"${dateTime.toString("yyyy-MM-dd")}\","close":${close}}"""
  }
}


