package com.lxmt.stocks.analyzers

import com.lxmt.stocks.database.PriceData
import org.joda.time.DateTime
import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by ravi on 20/05/2017.
 */
class TrendAnalyzerSpec extends FlatSpec with Matchers{

  "TrendAnalyzerSpec" should "infer positve trend" in {
    val priceList = List(
     4
    ,3
    ,5
    ,5
    ,8
    ,10
    ,10
    ,9
    ,12
    ,11
    ,14
    ,13
    ,16
    ,15).map(new PriceData("1","X",new DateTime(),0.0,0.0,0.0,_))

    val analyzer = new TrendAnalyzer()

    analyzer.positiveCondition(priceList.foldLeft[AnalysisResult](new AnalysisResult(Map()))((a,b) => analyzer.analyze(b,a))) should be (true)
}

}
