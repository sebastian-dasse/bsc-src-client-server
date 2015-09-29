package simple

import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html.Element

import scalatags.JsDom.all._
import scalajs.concurrent.JSExecutionContext.Implicits.runNow
import org.scalajs.dom
import dom.html
// import dom.ext.Ajax
import scalajs.js.annotation.JSExport
import autowire._
// import upickle.default._

object Ajaxer extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer] {
  override def doCall(req: Request) = {
    dom.ext.Ajax.post(
      url = "/ajax/" + req.path.mkString("/"),
      data = upickle.default.write(req.args)
    ).map(_.responseText)
  }

  def read[Result: upickle.default.Reader](p: String) = upickle.default.read[Result](p)
  def write[Result: upickle.default.Writer](r: Result) = upickle.default.write(r)
}

@JSExport
object Client extends {

  /*def getRoute = {
    val uri = dom.window.location.href
    val suffix = uri.split("/").last
    if (suffix.charAt(0) == '#') suffix
    else "home"
  }

  trait MyPage {
    def render(container: html.Div): Unit
  }

  object Home extends MyPage {
    def render(container: html.Div) = {
      container.appendChild(
        div(cls:="jumobotron",
          h1("Home")
        ).render
      )
    }
  }

  object Foo extends MyPage {
    def render(container: html.Div) = {
      container.appendChild(
        div(cls:="jumobotron",
          h1("Foo")
        ).render
      )
    }
  }

  object Bar extends MyPage {
    def render(container: html.Div) = {
      container.appendChild(
        div(cls:="jumobotron",
          h1("Bar")
        ).render
      )
    }
  }

  val routes = Map(
    "/" -> Home,
    "#foo" -> Foo,
    "#bar" -> Bar
  )




  def doRoute() = {
    println(getRoute)
    routes.getOrElse(getRoute, Home)
  }

  def setRoute(route: String): Unit = {
    val uri = dom.window.location.href
    val nosuffix = uri.split("#").head
    val prefix = if (nosuffix.last != '/') nosuffix + "/" else nosuffix
    val newUri = if (routes.contains(route))
      prefix + route
    else
      prefix /*+ "/"*/
    println(newUri)
    dom.window.location.href = newUri
//    doRoute()
  }*/


//  object MyFrag extends FragBase{
//    override def render: Element = div().render
//  }
//
//  val myRouter = new RouterBase {
//    override val routes = Map(
//      "" -> new FragBase{
//        override def render: Element = div().render
//      },
//      "" -> MyFrag
//    )
//  }

  val router = Router


  @JSExport
  def main(container: html.Div) = {

//    val res = ul((
//      for ((route, frag) <- Router.routes) yield tr(
//        ""
//      )
//    ).toSeq)
//    Router.routes.map(r => tr(r._1))

    val contents = div("client-contents").render
    val menu = div(id:="my-nav", cls:="navbar navbar-default",
      ul(cls:="nav nav-pills", (
        for ((route, frag) <- router.routes) yield li(
          a(
            href:=s"#$route",
            frag.name,
            onclick:={ () => {
              println(s"$frag.name clicked")
              contents.innerHTML = ""
              contents.appendChild(frag.render)
            } }
          )
        )
        ).toSeq)
    ).render

    container.appendChild(
      div(
        menu,
        contents,
        p("some stuff...")
      ).render
    )
//    dom.document.onkeyup = (evt: dom.KeyboardEvent) =>  { println(evt.keyCode) }

//    doRoute.render(container)
  }

  /*@JSExport
  def main(container: html.Div) = {

    println(">> " + getRoute)
    val inputBox = input.render
    val outputBox = ul.render
    def update() = Ajaxer[Api].list(inputBox.value).call().foreach{ data =>
      outputBox.innerHTML = ""
      for (FileData(name, size) <- data) {
        outputBox.appendChild(
          li(
              b(name), "- ", size, " bytes"
          ).render
        )
      }
    }
    inputBox.onkeyup = (e: dom.Event) => update()
    update()

    val fooInput = input(
      placeholder:="your input"
    ).render
    val fooOutput = div().render
    def foo() = Ajaxer[Api].foo(fooInput.value).call().foreach{ data => data match {
      case MyFoo(contents, time) => fooOutput.textContent = contents + " - " + time
    }}
    fooInput.onkeyup = (d: dom.Event) => {
      println(fooInput.value)
      foo()
    }

    val friendsInput = input(
      placeholder:="search for your friends"
    ).render
    val friendsOutput = ul.render
    def updateFriends() = Ajaxer[Api].friends(friendsInput.value).call().foreach{ data =>
      friendsOutput.innerHTML = ""
      friendsOutput.appendChild((
        for (Friend(firstname, secondname, email) <- data) yield li(
          b(firstname), s"$secondname - $email"
        )
      ).render)
    }
    friendsInput.onkeyup = (e: dom.Event) => {
      updateFriends()
    }

    val addFriendInput = input(
      placeholder:="add a friend"
    ).render
    def addFriend() = Ajaxer[Api].addFriend(addFriendInput.value).call().foreach{ data =>
      val res = if (data) "success" else "failure"
      println(res)
    }
    addFriendInput.onkeyup = (e: dom.KeyboardEvent) => e.keyCode match {
      case KeyCode.enter =>
        addFriend()
        addFriendInput.value = ""
      case _ => ()
    }

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
          h1("File Search"),
          inputBox,
          outputBox
        ),
        div(cls:="jumobotron",
          h1("Foo"),
          fooInput,
          fooOutput
        ),
        div(cls:="jumobotron",
          h1("Friends Search"),
          friendsInput,
          friendsOutput
        ),
        div(cls:="jumobotron",
          h1("Add Friends"),
          addFriendInput
        ),
        div(cls:="jumobotron",
          h1("Login"),
          loginInput,
          loginOutput
        )
      ).render
    )
  }*/
}
