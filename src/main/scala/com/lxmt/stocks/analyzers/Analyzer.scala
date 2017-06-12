package com.lxmt.stocks.analyzers

import com.lxmt.stocks.database.{PriceData, DataBaseService}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by ravi on 22/03/2017.
 */
trait Analyzer {
  def analyze(priceData:PriceData,analysisResult: AnalysisResult):AnalysisResult
  def name():String
  def listFeatures():List[String]
  def positiveCondition: AnalysisResult => Boolean
}

class AnalysisRegistry(stockName:String,regIList:List[Analyzer]) {

def computeAnalysisResults(priceData:PriceData,analysisResult: AnalysisResult):AnalysisResult = {
  regIList.map(_.analyze(priceData,analysisResult)).reduce((a,b)=> AnalysisResult(a.props++b.props))
  }



}

object AnalysisRegistry{
  var regList = new ListBuffer[Analyzer]
  def registerAnalyzers(analyzer: Analyzer*) = {
    regList++=analyzer.toList
 }
  def getAnalysisContextFor(stockName:String) = new AnalysisRegistry(stockName,regList.map(_.getClass.newInstance()).toList)
  def getAllAnalyzers()=regList.toList
}

case class AnalysisResult(props:Map[String,String])











