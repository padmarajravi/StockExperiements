package com.lxmt.stocks.analyzers

import com.lxmt.stocks.database.{PriceData, DataBaseService}

/**
 * Created by ravi on 22/03/2017.
 */
trait Analyzer {
  def analyze(priceDetails:List[PriceData]):AnalysisResult
  def name():String
}

case class AnalysisResult(result: Boolean,props:Map[String,String])








