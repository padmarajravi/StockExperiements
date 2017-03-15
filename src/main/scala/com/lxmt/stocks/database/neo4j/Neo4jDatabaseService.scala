package com.lxmt.stocks.database.neo4j

import java.io.File

import com.lxmt.stocks.database.{StorageEntity, DataBaseService}
import org.apache.commons.io.FileUtils
import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import gremlin.scala._


/**
 * Created by ravi on 14/03/2017.
 */
object Neo4jDatabaseService extends DataBaseService {

  val dbPath="target/stocks"
  var graph:ScalaGraph=null


  override def clearDataBase(): Boolean = {
    FileUtils.deleteDirectory(new File(dbPath))
    true
  }

  override def createEntity(propMap: Map[String, Object]): StorageEntity = {
    StorageEntity(getGraphDb().addVertex(propMap).id().toString)
    }

  override def createRelation(entity1: StorageEntity, entity2: StorageEntity , relation:String): Unit = {
    getGraphDb().V(entity1.entityId).toList()(0) --- relation --> getGraphDb().V(entity2.entityId).toList()(0)
  }

  override def isDbPresent(): Boolean = FileUtils.directoryContains(new File("target"),new File("stocks"))


  override def initiate(): Unit = {
    FileUtils.deleteDirectory(new File(dbPath))
    graph = Neo4jGraph.open(dbPath).asScala
  }

  def getGraphDb() = {
    if (graph==null) initiate()
    graph
  }


  override def shutDown(): Unit = {

    FileUtils.deleteDirectory(new File(dbPath))
  }

  override def getEntity(propName: String, propValue: Object): List[StorageEntity] =
    {
      val key = propValue.getClass.toString match {
        case "java.lang.String"  => Key[String](propName)
        case _ => Key[String](propName)
      }
      println(getGraphDb().V.toList().size)
      getGraphDb().V.has(key,propValue.toString).toList().map(f => StorageEntity(f.id().toString))

    }
}
