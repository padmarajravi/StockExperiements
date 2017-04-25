package com.lxmt.stocks.database.neo4j

import java.io.File

import com.lxmt.stocks.database.{PriceData, Company, DataBaseService}
import com.lxmt.stocks.downloader.Downloader.DownloadedData
import com.lxmt.stocks.Util._
import org.apache.commons.io.FileUtils
import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph
import gremlin.scala._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat



/**
 * Created by ravi on 14/03/2017.
 */
object Neo4jDatabaseService extends DataBaseService {

  val dbPath="target/stocks"
  var graph:ScalaGraph=null
  val priceRelation = "hasPriceOn"
  val nameKey = Key[String]("name")


  override def clearDataBase(): Boolean = {
    FileUtils.deleteDirectory(new File(dbPath))
    true
  }

  override def createCompanyEntity(name:String,propMap: Map[String, Any]): Company = {
    propMap+"name"-> name
    val existingNodes=getGraphDb().V.has(nameKey,name).toList()
    if(existingNodes.size>0)
    Company(existingNodes.head.id().toString,name)
    else
    Company(getGraphDb().addVertex(propMap).id().toString,name)
  }

  override def createPriceEntity(entity1: Company,priceData: DownloadedData): Boolean = {
    val relLabel=priceRelation+"_"+priceData.date.toString("yyyy-MM-dd")
    val existinNode=getGraphDb().V(entity1.entityId).toList().head
    if(existinNode.outE(relLabel).toList().size==0)
      {
        val priceNode=getGraphDb().addVertex(Map("type" -> "price","company"-> priceData.company,"close" -> priceData.close,"date" -> priceData.date.toString(DATE_FORMAT)))
        existinNode --- priceRelation+"_"+priceData.date.toString("yyyy-MM-dd") --> priceNode
        true
      }
    else
      {
        false
      }
  }

  override def isDbPresent(): Boolean = FileUtils.directoryContains(new File("target"),new File("stocks"))


  override def initiate(): Unit = {
    if(graph==null)
      {
        FileUtils.deleteDirectory(new File(dbPath))
        graph = Neo4jGraph.open(dbPath).asScala
      }
  }

  def getGraphDb() = {
    if (graph==null) {
      initiate()
    }
    graph
  }


  override def shutDown(): Unit = {
    getGraphDb().close()
  }

  override def getCompanies(propName: String, propValue: Object): List[Company] =
    {
      val key = propValue.getClass.toString match {
        case "java.lang.String"  => Key[String](propName)
        case _ => Key[String](propName)
      }
      getGraphDb().V.has(key,propValue.toString).toList().map(f => Company(f.id().toString,f.property[String]("name").toString))

    }

  override def getLatestPriceForCompany(companyName:String):PriceData = {
    val key = Key[String]("name")
    val priceEdges = getGraphDb().V.has(key,companyName).outE().filter{ p =>
     p.label().startsWith(priceRelation)
    }
    val maxValue = priceEdges.inV().value[String]("date").toList().max
    val propertiesList = priceEdges.filter(_.label().equals(maxValue)).inV().propertyMap("company","close","date").toList()
    val result = propertiesList.map(x => new PriceData(x.get("company").toString,x.get("company").toString,DateTime.parse(x.get("date").toString),x.get("price").toString.toDouble,0.0,0.0,0.0))
    result.head
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
}
