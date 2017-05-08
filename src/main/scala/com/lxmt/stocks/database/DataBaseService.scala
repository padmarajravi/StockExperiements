package com.lxmt.stocks.database

import com.lxmt.stocks.downloader.Downloader.DownloadedData
import org.joda.time.DateTime

/**
 * Created by ravi on 14/03/2017.
 */
trait DataBaseService {

  def isDbPresent():Boolean
  def clearDataBase():Boolean
  def createCompanyEntity(name:String,propMap:Map[String,String]):Company
  def createPriceEntity(companyEntity:Company,downloadedData: DownloadedData):Boolean
  def getCompanies(propName:String,propValue:Object):List[Company]
  def getLatestPriceForCompany(companyName:String*):List[PriceData]
  def getLastNDayPrices(companyName:String,numberOfIntervals:Int):List[PriceData]
  def getLastSyncDate():String
  def initiate()
  def shutDown()
}

case class Company(id:String,name:String)
case class PriceData(id:String,company:String,dateTime: DateTime,open:Double,high:Double,low:Double,close:Double)


