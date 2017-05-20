package com.lxmt.stocks.database.neo4j

import java.io.File

import com.lxmt.stocks.database.{PriceData, Company, DataBaseService}
import com.lxmt.stocks.downloader.Downloader.DownloadedData
import com.lxmt.stocks.Util._
import org.apache.commons.io.FileUtils
import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph
import org.apache.tinkerpop.gremlin.process.traversal.Path
import gremlin.scala._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import collection.JavaConversions._


/**
 * Created by ravi on 14/03/2017.
 */
object Neo4jDatabaseService extends DataBaseService {

  val dbPath="/Users/ravi/StockData"
  var graph:ScalaGraph=null
  val priceRelation = "hasPriceOn"
  val nameKey = Key[String]("name")
  val typeKey =Key[String]("type")
  val dateKey = Key[String]("date")
  val relType = Key[String]("relname")


  override def clearDataBase(): Boolean = {
    FileUtils.deleteDirectory(new File(dbPath))
    graph=null
    true
  }

  override def createCompanyEntity(name:String,propMap: Map[String, String]): Company = {
    propMap+"name"-> name
    val existingNodes=getGraphDb().V.has(nameKey,name).toList()
    if(existingNodes.size>0)
    Company(existingNodes.head.id().toString,name)
    else
    Company(getGraphDb().addVertex("company",propMap).id().toString,name)
  }

  override def createPriceEntity(entity1: Company,priceData: DownloadedData): Boolean = {
    val relLabel=priceRelation+"_"+priceData.date.toString("yyyy-MM-dd")
    val existingNode=getGraphDb().V().has(nameKey,entity1.name).toList().head
    if(existingNode.outE(relLabel).exists())
      {
        false
      }
    else
      {
        val priceNode=getGraphDb().addVertex("price",Map("type" -> "price","company"-> priceData.company,"close" -> priceData.close,"date" -> priceData.date.toString(DATE_FORMAT)))
        existingNode --- (relLabel,relType -> relLabel) --> priceNode
        true
      }
  }


  def getGraphDb() = {
    if (graph==null) {
      graph = Neo4jGraph.open(dbPath).asScala

    }

  }


  override def shutDown(): Unit = {
    println("Shutting down")
    graph.close()
    graph=null
  }

  override def getCompanies(propName: String, propValue: Object): List[Company] =
    {
      val key = propValue.getClass.toString match {
        case "java.lang.String"  => Key[String](propName)
        case _ => Key[String](propName)
      }
      getGraphDb().V.has(key,propValue.toString).toList().map(f => Company(f.id().toString,f.property[String]("name").toString))

    }

  override def getLatestPriceForCompany(companyNames:String*):List[PriceData] = {
    companyNames.map { companyName =>
      val key = Key[String]("name")
      val priceEdges = getGraphDb().V.has(key, companyName).outE().filter { p =>
        p.label().startsWith(priceRelation)
      }
      val maxVertex = priceEdges.inV().toList().maxBy(_.value[String]("date"))
      new PriceData(maxVertex.id().toString, maxVertex.value[String]("company"), maxVertex.value[String]("date").toDateTime, 0.0, 0.0, 0.0, maxVertex.value[Double]("close"))
    }.toList
  }

  override def getLastNDayPrices(companyName: String,numberOfIntervals:Int): List[PriceData] = {
    val relations         = getGraphDb().V().has(nameKey,companyName).outE().filter(_.label().startsWith(priceRelation))
    val relationStrings   = relations.label().toList().sortWith(_ > _)
    val lastNDayrelations = relationStrings.take(numberOfIntervals)
    val resultList = relations.filter(p => lastNDayrelations.contains(p.label())).outV().map{ v =>
      new PriceData(v.id().toString,v.property[String]("name").value(),DateTime.parse(v.property[String]("date").value()),v.property[Double]("price").value(),0.0,0.0,0.0)
    }.toList()
    resultList.sortWith( (p,q) => p.dateTime.isBefore(q.dateTime))
  }

  override def getLastSyncDate() = {
    val vertexList=getGraphDb().V().has(typeKey,"price").toList().map(v => v.value[String]("date"))
    if(!vertexList.isEmpty) vertexList.max else "0000-00-00"
  }
}
