package com.lxmt.stocks

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}


/**
 * Created by ravi on 22/03/2017.
 */
object Util {

  val DATE_FORMAT                            = "yyyy-MM-dd"
  val NAME_LABEL                             = "name"
  val PRICE_LABEL                            = "close"
  val TREND_ANAYZER_INTERVAL                 = 14
  val DATA_STORAGE_WINDOW                    = 14
  val currentDateString                      = new DateTime().toString(DATE_FORMAT)
  val lastDateForStorageString               = new DateTime().minusDays(DATA_STORAGE_WINDOW).toString(DATE_FORMAT)


  implicit class UtilMethods(s:String){

     def toDateTime                     = DateTimeFormat.forPattern(DATE_FORMAT).parseDateTime(s)
     def toDownloadStartDateStr         = if(s<lastDateForStorageString) lastDateForStorageString
                                          else toDateTime.plusDays(1).toString(DATE_FORMAT)

   }

}
