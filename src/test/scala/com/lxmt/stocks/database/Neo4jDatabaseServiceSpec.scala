package com.lxmt.stocks.database

import com.lxmt.stocks.database.neo4j.Neo4jDatabaseService
import com.lxmt.stocks.downloader.Downloader.DownloadedData
import org.scalatest.{Matchers, FlatSpec}
import com.lxmt.stocks.Util._

/**
 * Created by ravi on 7/05/2017.
 */
class Neo4jDatabaseServiceSpec extends FlatSpec with Matchers{

  "Neo4jDatabaseService" should "return latest price of a stock" in {
    val dbService = new Neo4jDatabaseService("test")
    dbService.clearDataBase()
    val company= dbService.createCompanyEntity("ABB",Map("name" -> "ABB"))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-01".toDateTime,0.0,0.0,0.0,1.0))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-02".toDateTime,0.0,0.0,0.0,2.0))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-03".toDateTime,0.0,0.0,0.0,3.0))
    dbService.shutDown()
    dbService.getLatestPriceForCompany("ABB").head.close should be (3.0)
    dbService.shutDown()
  }

  "Neo4jDatabaseService" should "return the last date for which data was synced" in {
    val dbService = new Neo4jDatabaseService("test")
    dbService.clearDataBase()
    val company= dbService.createCompanyEntity("ABB",Map("name" -> "ABB"))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-01".toDateTime,0.0,0.0,0.0,1.0))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-02".toDateTime,0.0,0.0,0.0,2.0))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-03".toDateTime,0.0,0.0,0.0,3.0))
    dbService.shutDown()
    dbService.getLastSyncDate() should be ("2017-05-03")
    dbService.shutDown()
  }

  "Neo4jDatabaseService" should "return 0000-00-00 for empty directory" in {
    val dbService = new Neo4jDatabaseService("test")
    dbService.clearDataBase()
    dbService.getLastSyncDate() should be ("0000-00-00")
    dbService.shutDown()
  }

  "Neo4jDatabaseService" should "return last 5 day prices for a stock" in {
    val dbService = new Neo4jDatabaseService("test")
    dbService.clearDataBase()
    val company= dbService.createCompanyEntity("ABB",Map("name" -> "ABB"))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-01".toDateTime,0.0,0.0,0.0,1.0))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-02".toDateTime,0.0,0.0,0.0,2.0))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-03".toDateTime,0.0,0.0,0.0,3.0))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-04".toDateTime,0.0,0.0,0.0,4.0))
    dbService.createPriceEntity(company,new DownloadedData("ABB","2017-05-05".toDateTime,0.0,0.0,0.0,5.0))
    dbService.shutDown()
    dbService.getLastNDayPrices("ABB",5).size should be (5)
    dbService.shutDown()
}



}
