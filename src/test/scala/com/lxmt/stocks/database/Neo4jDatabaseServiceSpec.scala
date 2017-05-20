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
    Neo4jDatabaseService.clearDataBase()
    val company= Neo4jDatabaseService.createCompanyEntity("ABB",Map("name" -> "ABB"))
    Neo4jDatabaseService.createPriceEntity(company,new DownloadedData("ABB","2017-05-01".toDateTime,0.0,0.0,0.0,1.0))
    Neo4jDatabaseService.createPriceEntity(company,new DownloadedData("ABB","2017-05-02".toDateTime,0.0,0.0,0.0,2.0))
    Neo4jDatabaseService.createPriceEntity(company,new DownloadedData("ABB","2017-05-03".toDateTime,0.0,0.0,0.0,3.0))
    Neo4jDatabaseService.getLatestPriceForCompany("ABB").head.close should be (3.0)
  }

  "Neo4jDatabaseService" should "return the last date for which data was synced" in {
    Neo4jDatabaseService.clearDataBase()
    val company= Neo4jDatabaseService.createCompanyEntity("ABB",Map("name" -> "ABB"))
    Neo4jDatabaseService.createPriceEntity(company,new DownloadedData("ABB","2017-05-01".toDateTime,0.0,0.0,0.0,1.0))
    Neo4jDatabaseService.createPriceEntity(company,new DownloadedData("ABB","2017-05-02".toDateTime,0.0,0.0,0.0,2.0))
    Neo4jDatabaseService.createPriceEntity(company,new DownloadedData("ABB","2017-05-03".toDateTime,0.0,0.0,0.0,3.0))
    Neo4jDatabaseService.shutDown()
    Neo4jDatabaseService.getLastSyncDate() should be ("2017-05-03")
  }

  "Neo4jDatabaseService" should "return 0000-00-00 for empty directory" in {
     Neo4jDatabaseService.clearDataBase()
     Neo4jDatabaseService.getLastSyncDate() should be ("0000-00-00")
  }

}
