package com.lxmt.stocks.downloader

import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by ravi on 7/05/2017.
 */
class DownloaderSpec extends FlatSpec with Matchers{

  "Downloader" should "download stock data " in {
    Downloader.getStockPrice("ABB","2017-05-01","2017-05-04").get.head.close should be (1422.0)
  }

}
