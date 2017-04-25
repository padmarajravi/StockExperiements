package com.lxmt.stocks.analyzers

import com.lxmt.stocks.database.{PriceData, DataBaseService}

/**
 * Created by ravi on 22/03/2017.
 */
trait Analyzer {
  def analyze(company:String,priceDetails:List[PriceData]):Map[String,Any]
}




