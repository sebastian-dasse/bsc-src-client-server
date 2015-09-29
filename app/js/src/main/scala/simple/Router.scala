package simple

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.html.Element
import scala.collection.immutable.SortedMap
import scalatags.JsDom.all._


trait FragBase {
  val name: String
  def render: html.Element
}

object HomeFrag extends FragBase {
  override val name = "Home"
  override def render = div(cls:="jumobotron",
    h1(name)
  ).render
}

object FooFrag extends FragBase {
  override val name = "Foo"
  override def render = div(cls:="jumobotron",
    h1(name),
    p("some foo stuff")
  ).render
}

object BarFrag extends FragBase {
  override val name = "Bar"
  override def render = div(cls:="jumobotron",
    h1(name),
    p("some bar stuff")
  ).render
}


trait RouterBase {
  
  val routes: Map[String, FragBase]
  
  def getRoute = {
    val uri = dom.window.location.href
    val suffix = uri.split("/").last
    if (suffix.charAt(0) == '#') suffix
    else "/"
  }

  private def normalize(uri: String) = {
    val nosuffix = uri.split("#").head
    val prefix = if (nosuffix.last != '/') nosuffix + "/" else nosuffix
    prefix + "#/"
  }

  def doNormalize() = {
    val uri = dom.window.location.href
    dom.window.location.href = normalize(uri)
  }

  def setRoute(route: String): Unit = {
    val uri = dom.window.location.href
    val prefix = uri.split("#").head
//    val prefix = if (nosuffix.last != '/') nosuffix + "/" else nosuffix

    val res = routes.getOrElse(route, HomeFrag)
    val newSuff = res match {
      case HomeFrag => "/"
      case _ => route
    }
    val newUri = prefix + "#" + newSuff

//    val newUri = if (routes.contains(route))
//      prefix + "#" + route
//    else
//      prefix + "#/"
    println(s"router> $newUri")
    dom.window.location.href = newUri
    //    doRoute()
  }
}

//class Router extends RouterBase

object Router extends RouterBase {
  override val routes: SortedMap[String, FragBase] = SortedMap(
    "/" -> HomeFrag,
    "/foo" -> FooFrag,
    "/bar" -> BarFrag
  )
}