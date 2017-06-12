package com.lxmt.stocks.analyzers

import com.lxmt.stocks.database.{PriceData, DataBaseService}
import com.lxmt.stocks.Util._

/**
 * Created by ravi on 23/03/2017.
 */
class TrendAnalyzer extends Analyzer{

  var storage = CircularBuffer[Double](TREND_ANAYZER_INTERVAL);
  override  def analyze(priceData:PriceData,analysisResult: AnalysisResult): AnalysisResult = {
    storage.add(priceData.close)
    val yValues = storage.getAll()
    val xValues = List.range(1,yValues.size) map(_.toDouble)
    val yAvg    = yValues.sum/storage.getSize()
    val xAvg    = (yValues.size+1) /2
    val num     = yValues.zip(xValues).foldLeft(0.0)(
      (res,zipped:(Double,Double)) => res + (zipped._1-yAvg)*(zipped._2-xAvg))
    val den     =  xValues.foldLeft(0.0)((acc,xi) => (xi-xAvg)*(xi-xAvg)+acc)
    val slope = num/den
    AnalysisResult(Map("SLOPE"->slope.toString))
 }

  override def name() = "TREND_ANALYZER"
  override def listFeatures() = List("SLOPE")
  override def positiveCondition = {

    p:AnalysisResult =>
      println("Slope:"+p.props.get("SLOPE"))
      p.props.getOrElse("SLOPE","0.0").toDouble > 5.0
  }

}



