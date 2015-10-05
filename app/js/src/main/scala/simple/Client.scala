package simple

import scala.collection.immutable.{ListMap, SortedMap}
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.ext.KeyCode
import scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.nav
import autowire._
import scalajs.concurrent.JSExecutionContext.Implicits.runNow

import simple.fragments._

@JSExport
object Client extends Routing { // alternatively: val routing = new Routing{ override ... }
  override val routes = ListMap(
    "/" -> HomeFrag,
    "/foo" -> FooFrag,
    "/bar" -> BarFrag,
    "/friends" -> FriendsFrag/*,
    "/friends0" -> FriendsFrag0*/
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
            (for ((route, frag) <- routes) yield li(
              a(href := s"#$route", frag.fragName)
            )).toSeq
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

  /*@JSExport
  def main2(container: html.Div) = {

    var loggedin = false
    val loginInput = input.render
    val loginOutput = div.render
    def login() = Ajaxer[Api].login(User(loginInput.value, "secret")).call().foreach{ data =>
      println(data)
      loggedin = data
      loginOutput.textContent = if (data) "logged in" else "not logged in"
      /*if (data) */ dom.location.reload(true)
    }
    loginInput.onkeyup = (e: dom.KeyboardEvent) => e.keyCode match {
      case KeyCode.enter =>
        login()
      case _ => ()
    }

    def logout() = Ajaxer[Api].logout().call().foreach{ data =>
      loggedin = data
    }

    val loginArea = div/*form*/(
      onsubmit:={ (evt: dom.Event) => evt.preventDefault() },
      button(
        "Login",
        onclick:={ (evt: dom.Event) =>
          login()
          loggedin = true
          println("loggin in")
        }
      )
    )
    val logoutArea = div/*form*/(
      onsubmit:={ (evt: dom.Event) => evt.preventDefault() },
      button(
        "Logout",
        onclick:={ (evt: dom.Event) =>
          logout()
          loggedin = false
          println("loggin out")
        }
      )
    )

    container.appendChild(
      div(
        if (loggedin) logoutArea else loginArea,
        div(cls:="jumbotron",
          h1("Login"),
          loginInput,
          loginOutput
        )
      ).render
    )
  }*/

}
