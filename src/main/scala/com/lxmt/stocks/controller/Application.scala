package com.lxmt.stocks.controller

import com.lxmt.stocks.analyzers.{AnalysisResult, TrendAnalyzer, Analyzer}
import com.lxmt.stocks.database.DataBaseServiceFactory
import com.lxmt.stocks.database.DataBaseServiceFactory.DataBaseType
import com.lxmt.stocks.downloader.Downloader
import play.api.mvc._

import scala.io.Source

class Application extends Controller {

  def index = Action {
    val lines = Source.fromFile("/Users/ravi/StockExperiements/src/main/resources/stocks_small", "utf-8").getLines()
    val analyzers:List[Analyzer] = List(TrendAnalyzer)
    val databaseService = DataBaseServiceFactory.getDataBase(DataBaseType.Neo4j)
    Downloader.ensureDataStorage(databaseService,lines)
    val stockAndAnalysisResults = for{
      stockName <- lines
      priceData = databaseService.getLastNDayPrices(stockName,14)
      analyzer  <- analyzers
      result    = analyzer.analyze(priceData)
    } yield (stockName,result)
    val postiveResults = stockAndAnalysisResults.filter(p => p._2.result).map(x => x._1).toList
    Ok(views.html.index(postiveResults))
  }
}