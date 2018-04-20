package com.lxmt.stocks.database.mysql

import java.sql.{Connection, DriverManager}

import com.lxmt.stocks.database.{Company, DataBaseService, PriceData}
import com.lxmt.stocks.downloader.Downloader
import com.lxmt.stocks.Util._

class MySqlDatabaseService extends DataBaseService{

  val url = "jdbc:mysql://localhost/mysql"
  val driver = "com.mysql.jdbc.Driver"
  val username = "stocks"
  val password = "password"
  var connection:Connection = _
  def getConnection:Connection = {
    if (connection==null){
      try{
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
      }
      catch{
        case e:Exception => e.printStackTrace()
      }
    }
    connection
  }

  override def clearDataBase(): Boolean = {
    val st = getConnection.createStatement()
    st.execute("DELETE FROM STOCKS")
    st.execute("DELETE FROM COMPANIES")
    st.close()
  }

  override def createCompanyEntity(name: String, propMap: Map[String, String]): Company = {
    val st = getConnection.createStatement()
    val res    = st.executeQuery(s"""SELECT * FROM COMPANIES WHERE NAME=${name}""")
    if(!res.next())
      {
        st.execute(s"INSERT INTO TABLE COMPANIES(company_symbol,company_name) VALUES(${name}),${propMap.get(NAME_LABEL)}")
      }


  }

  override def createPriceEntity(companyEntity: Company, downloadedData: Downloader.DownloadedData): Boolean = ???

  override def getCompanies(propName: String, propValue: Object): List[Company] = ???

  override def getLatestPriceForCompany(companyName: String*): List[PriceData] = ???

  override def getLastNDayPrices(companyName: String, numberOfIntervals: Int): List[PriceData] = ???

  override def getLastSyncDate(): String = ???

  override def shutDown(): Unit = getConnection.close()
}
