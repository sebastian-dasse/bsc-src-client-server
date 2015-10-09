package simple

import scala.collection.immutable.ListMap
import org.scalajs.dom
import org.scalajs.dom.html

/**
 * Provides client-side routing. Therefore `routes`, `defaultRoute` and
 * `contentContainer` need to be overridden. Initially call `startRouting()` once.
 */
trait Routing {
  val routes: ListMap[String, Fragment] // the routes in insertion order
  val defaultRoute: Fragment
  val contentContainer: html.Element

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
    contentContainer.innerHTML = ""
    contentContainer.appendChild(frag.render)
  }

  private[this] lazy val _start = {
    dom.setInterval(() => updateRoute(), 100)
    updateRoute()
    dom.console.info("Routing started")
  }

  def startRouting(): Unit = {
    _start
  }
}
