package simple

import scala.collection.immutable.ListMap
import org.scalajs.dom
import org.scalajs.dom.html

trait Routing {
  val routes: ListMap[String, Fragment] // the routes in insertion order
  val defaultRoute: Fragment
  val contentsContainer: html.Element

  private var _route = ""

  def route: String = {
    val hash = dom.window.location.hash
    if (hash.isEmpty) "/" else hash.tail.split("#").head
  }

  def updateRoute(): Unit = if (route != _route) {
    _route = route
    val frag = routes.getOrElse(_route, defaultRoute)
    if (frag == defaultRoute) {
      dom.window.location.hash = "#/"
    }
    contentsContainer.innerHTML = ""
    contentsContainer.appendChild(frag.render)
  }
  
  def startRouting(): Unit = {
    dom.setInterval(() => updateRoute(), 100)
    updateRoute()
  }
}
