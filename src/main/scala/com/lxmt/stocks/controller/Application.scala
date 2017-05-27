package com.lxmt.stocks.controller

import com.lxmt.stocks.analyzers.{AnalysisRegistry, AnalysisResult, TrendAnalyzer, Analyzer}
import com.lxmt.stocks.database.DataBaseServiceFactory
import com.lxmt.stocks.database.DataBaseServiceFactory.DataBaseType
import com.lxmt.stocks.downloader.Downloader
import play.api.mvc._
import play.libs.Json

import scala.io.Source

class Application extends Controller {

  def index = Action {
    val lines = Source.fromFile("/Users/ravi/StockExperiements/src/main/resources/stocks_small", "utf-8").getLines().toList
    val analysisRegistry = AnalysisRegistry(TrendAnalyzer)
    val databaseService = DataBaseServiceFactory.getDataBase(DataBaseType.Neo4j)
    Downloader.ensureDataStorage(databaseService,lines)
    val stockAndAnalysisResults = for{
      stockName      <- lines
      priceList      = databaseService.getLastNDayPrices(stockName,14)
      analysisResult = priceList.foldLeft[AnalysisResult](new AnalysisResult(Map()))((a,b)=> analysisRegistry.computeAnalysisResults(b,a) )
    } yield (stockName,analysisResult)

    println("Results:"+stockAndAnalysisResults)
    val positiveResults = stockAndAnalysisResults.toMap
                          .mapValues(p => (p,analysisRegistry.getAllAnalyzers().filter(_.positiveCondition(p))))
                          .filter(!_._2._2.isEmpty).toList


    databaseService.shutDown()
    Ok(views.html.index(positiveResults.map(_._1)))
    Ok(views.html.index(positiveResults.map(_._1)))
  }


  def plot(symbol:String) = Action {
    // println("symbol:"+symbol)
    val databaseService = DataBaseServiceFactory.getDataBase(DataBaseType.Neo4j)
    val priceList = databaseService.getLastNDayPrices(symbol,14)
    databaseService.shutDown()
    val jsonResult = priceList.map(_.toJson()).mkString("[",",","]")
    println(jsonResult)
    Ok(views.html.plot(jsonResult))
  }

}