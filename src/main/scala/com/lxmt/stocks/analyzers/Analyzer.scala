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

class AnalysisRegistry(regList:List[Analyzer]) {

def computeAnalysisResults(priceData:PriceData,analysisResult: AnalysisResult):AnalysisResult = {
    regList.map(_.analyze(priceData,analysisResult)).reduce((a,b)=> AnalysisResult(a.props++b.props))
  }

def getAllAnalyzers()=regList

}

object AnalysisRegistry{
  def apply(analyzers: Analyzer*)=new AnalysisRegistry(analyzers.toList)
}

case class AnalysisResult(props:Map[String,String])











