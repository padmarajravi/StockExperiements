package com.lxmt.stocks.analyzers

import com.lxmt.stocks.database.{PriceData, DataBaseService}
import com.lxmt.stocks.Util._

/**
 * Created by ravi on 23/03/2017.
 */
class TrendAnalyzer(databaseService:DataBaseService) extends Analyzer{
  override def analyze(company: String,priceList:List[PriceData]): Map[String, Any] = {

    val prices  = priceList.takeRight(TREND_ANAYZER_INTERVAL)
    val yValues = prices.map(_.close)
    val xValues = List.range(1,yValues.size) map(_.toDouble)
    val yAvg    = yValues.sum/prices.size
    val xAvg    = (yValues.size+1) /2
    val num     = yValues.zip(xValues).foldLeft(0.0)(
                   (res,zipped:(Double,Double)) => res + (zipped._1-yAvg)*(zipped._2-xAvg))
    val den     =  xValues.foldLeft(0.0)((acc,xi) => (xi-xAvg)+acc)
    Map("SLOPE" -> num/den)
  }

}

