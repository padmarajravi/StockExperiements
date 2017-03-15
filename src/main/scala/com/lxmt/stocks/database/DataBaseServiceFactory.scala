package com.lxmt.stocks.database

import com.lxmt.stocks.database.neo4j.Neo4jDatabaseService

/**
 * Created by ravi on 14/03/2017.
 */
object DataBaseServiceFactory {

  object DataBaseType extends Enumeration {
    type DataBaseType = Value
    val Neo4j = Value("Neo4j")
  }

  import DataBaseType._

  def getDataBase(dataBaseType: DataBaseType): DataBaseService = dataBaseType match {
    case _ => Neo4jDatabaseService
  }
}