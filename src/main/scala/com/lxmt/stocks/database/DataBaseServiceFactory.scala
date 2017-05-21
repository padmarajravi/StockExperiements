package com.lxmt.stocks.database

import com.lxmt.stocks.database.DataBaseServiceFactory.DataBaseType.DataBaseType
import com.lxmt.stocks.database.neo4j.Neo4jDatabaseService

/**
 * Created by ravi on 14/03/2017.
 */
object DataBaseServiceFactory {

  val serviceMap = scala.collection.mutable.Map[String,DataBaseService]()

  object DataBaseType extends Enumeration {
    type DataBaseType = Value
    val Neo4j = Value("Neo4j")
  }

  import DataBaseType._

  def getDataBase(dataBaseType: DataBaseType): DataBaseService = {
      if(!serviceMap.contains(dataBaseType.toString)) {
        println("Inserting new")
      dataBaseType match {
        case DataBaseType.Neo4j => serviceMap += (DataBaseType.Neo4j.toString -> new Neo4jDatabaseService("/Users/ravi/StockData"))

      }
    }

    serviceMap.get(dataBaseType.toString).get
  }
}