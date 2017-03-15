package com.lxmt.stocks.controller

import com.lxmt.stocks.downloader.Downloader
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    val x= Downloader.ensureDataStorage()
    Ok(views.html.index(x))
  }
}