package simple.fragments

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.ext.KeyCode
import scala.collection.immutable.ListMap
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.nav
import autowire._
import scalajs.concurrent.JSExecutionContext.Implicits.runNow

import simple._


object FriendsFrag extends Fragment {
  override val fragName = "Friends"

  override def render = {
    import Details._
    addInputs.foreach(in => in.onkeyup = clearOnEscape(in) _)
    dom.setTimeout(() => searchInput.focus(), 10)

    div(
      div(cls:="container",
        div(cls:="page-header",
          h1(fragName)
        ),
        div(
          ul(cls:="nav nav-tabs", "data-tabs".attr:="tabs",
//            for ((name, tab) <- tabs.toSeq) yield li(
            for ((name, Tab(tabElm, autofocus)) <- tabs.toSeq) yield {
              val lili = li(
                cls := (if (name == "Search") "active" else ""),
                //              a(name, onclick:={ () => selectTab(tab) })
                a(
                  "data-toggle".attr := "tab",
                  name,
                  onclick := { () => {
                    //                  selectTab(tabElm)
                    //                  autofocus.focus()
                  }
                })
                //              aaa
                //                selectTab(tabElm, aaa)
                //                autofocus.focus()
                //              }

                //              aaa}
              ).render
              lili.onclick = (evt: dom.Event) => {
                println("clicked")
                selectTab(tabElm, lili)
                autofocus.focus()
              }
              lili
            }
          ),
          div(cls:="tab-content",
            tabs.values.toSeq.map(_.elm)
          )
        )
      )
    ).render
  }

  private object Details {
    case class Tab (elm: html.Element, autofocus: html.Input = input.render)

    val tabs: ListMap[String, Tab] = ListMap(
      "Search" -> Tab(searchTab, autofocus = searchInput),
      "Add" -> Tab(addTab, autofocus = firstnameInput)
    )
//    for ((nm, Tab(tb, autofocus)) <- tabs) {
//      a(nm, onclick:={ () => {selectTab(tb); autofocus.focus()} })
//    }

    val tabs__xxx: ListMap[String, html.Element] = ListMap(
      "Search" -> searchTab,
      "Add" -> addTab
    )
    lazy val searchTab = nav(cls:="tab-pane active",
      searchInput,
      table(cls:="table",
        friendsDisplay
      )
    ).render
    lazy val addTab = div(cls:="tab-pane",
      addForm
    ).render

    def selectTab = selectTabFrom(tabs.values) _

//    def selectTabFrom(tabs: Iterable[html.Element])(tab: html.Element) = tabs.foreach{ t =>
    def selectTabFrom(tabs: Iterable[Tab])(tabElm: html.Element, tab: html.LI) = tabs.foreach{ t =>
      if (t.elm == tabElm) {
        tabElm.classList.add("active")
        tab.classList.add("active")
        t.autofocus.focus()
      } else
        t.elm.classList.remove("active")
//        t.classList.remove("active")
    }

    lazy val searchInput: html.Input = input(
      placeholder:="Just start typing...",
      onkeyup:={ (evt: dom.KeyboardEvent) => {
        clearOnEscape(searchInput)(evt)
        updateFriendsDisplay()
      }}
    ).render
    lazy val friendsDisplay = tbody.render

    def updateFriendsDisplay() = Ajaxer[Api].searchFriends(searchInput.value).call().foreach{ friends =>
      friendsDisplay.innerHTML = ""
      friendsDisplay.appendChild((
        for (Friend(firstname, secondname, email) <- friends) yield tr(
          th(firstname), td(secondname), td(a(href:=s"mailto:$email", email))
        )
      ).render)
    }


    lazy val addForm = div(
      addInputs,
      onkeyup:={ (evt: dom.KeyboardEvent) => evt.keyCode match {
        case KeyCode.enter =>
          def validInput: Boolean = List(
              checkInput(firstnameInput, !_.isEmpty),
              checkInput(emailInput, isValidEmail(_))
            ).reduceLeft(_ && _)
          if (validInput) {
            addFriend()
            addInputs.foreach(_.value = "")
            firstnameInput.focus()
          }
        case _ => ()
      } }
    ).render

    lazy val addInputs = Seq(
      firstnameInput,
      secondnameInput,
      emailInput
    )

    lazy val firstnameInput = input(
      placeholder:="First name"
    ).render

    lazy val secondnameInput = input(
      placeholder:="Second name"
    ).render

    lazy val emailInput = input(
      placeholder:="Email",
      id:="email"
    ).render

    def addFriend(): Unit = {
      val friend = Friend(
        firstnameInput.value,
        secondnameInput.value,
        emailInput.value
      )
      Ajaxer[Api].addFriend(friend).call().foreach{ _ =>
        flashMessageAt(addForm)(s"${friend.toPrettyString} successfully added")
//        updateFriendsDisplay()
      }
    }


    def isValidEmail(str: String) = {
      val validEmail = """\w+@\w+\.\w+""".r
      str match {
        case validEmail() => true
        case _ => false
      }
    }

    def flashMessageAt(elm: html.Element)(msg: String): Unit = {
      val msgNode = div(cls:="container", p(cls:="label label-success", padding:=10.px, msg)).render
      elm.appendChild(msgNode)
      dom.setTimeout(() => elm.removeChild(msgNode), 3000)
    }

    def flashHighlight(elm: html.Input): Unit = {
      elm.classList.add("error")
      dom.setTimeout(() => { elm.classList.remove("error")}, 2000)
    }

    def checkInput(in: html.Input, pred: String => Boolean): Boolean = {
      val ok = pred(in.value)
      if (!ok) {
        flashHighlight(in)
      }
      ok
    }

    def clearOnEscape(in: html.Input)(evt: dom.KeyboardEvent) = evt.keyCode match {
      case KeyCode.escape => in.value = ""
      case _ => ()
    }
  }
}
