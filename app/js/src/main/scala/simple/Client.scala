package simple

import scala.collection.immutable.ListMap
import org.scalajs.dom.html
import scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.nav

import simple.fragments._
import simple.fragments.friendsfrag.FriendsFrag

@JSExport
object Client extends Routing { // alternatively use an attribute like this: val routing = new Routing{ override ... }
  override val routes = ListMap(
    "/" -> HomeFrag,
    "/foo" -> FooFrag,
    "/bar" -> BarFrag,
    "/friends" -> FriendsFrag
  )
  override val defaultRoute = HomeFrag
  override val contentContainer = div(cls:="container").render

  @JSExport
  def main(container: html.Div) = {
    startRouting()

    val menu =
      nav(cls:="navbar navbar-default navbar-fixed-top",
        div(cls:="container",
          ul(cls:="nav nav-pills nav-justified",
            for ((route, frag) <- routes.toSeq) yield
              li( a(href:=s"#$route", frag.fragName) )
          )
        )
      ).render

    container.appendChild(
      div(
        menu,
        contentContainer,
        footer(cls:="footer",
          div(cls:="container",
            p("© 2015 Sebastian Dassé")
          )
        )
      ).render
    )
  }
}
