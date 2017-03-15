package com.lxmt.stocks.database

/**
 * Created by ravi on 14/03/2017.
 */
trait DataBaseService {

  def isDbPresent():Boolean
  def clearDataBase():Boolean
  def createEntity(propMap:Map[String,Object]):StorageEntity
  def createRelation(entity1:StorageEntity,entity2:StorageEntity,relation:String)
  def getEntity(propName:String,propValue:Object):List[StorageEntity]
  def initiate()
  def shutDown()

}

case class StorageEntity(entityId:String)


