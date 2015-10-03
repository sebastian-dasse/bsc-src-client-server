package simple.fragments

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.ext.KeyCode
import scala.collection.immutable.ListMap
import scalatags.JsDom.all._
import autowire._
import scalajs.concurrent.JSExecutionContext.Implicits.runNow

import simple._


object FriendsFrag extends Fragment {
  override val fragName = "Friends"

  override def render = {
    import Inner._
    addInputs.foreach(in => in.onkeyup = clearOnEscape(in) _)
    div(
      div(cls:="container",
        div(cls:="page-header",
          h1(fragName)
        ),
        div(
          ul(cls:="nav nav-tabs",
            for ((name, tab) <- tabs.toSeq) yield li(
              a(name, onclick:={ () => selectTab(tab) })
            )
          ),
          div(cls:="tab-content",
            tabs.values.toSeq
          )
        )
      )
    ).render
  }


  private object Inner {




  def clearOnEscape(in: html.Input)(evt: dom.KeyboardEvent) = evt.keyCode match {
    case KeyCode.escape => in.value = ""
    case _ => ()
  }


  val tabs = ListMap(
    "Search" -> searchTab,
    "Add" -> addTab
  )

  val searchTab = div(cls:="tab-pane active",
    searchInput,
    table(cls:="table",
      friendsDisplay
    )
  ).render

  val addTab = div(cls:="tab-pane",
    addInput
  ).render

  def selectTab = selectTabFrom(tabs.values) _

  def selectTabFrom(tabs: Iterable[html.Element])(tab: html.Element) =
    tabs.foreach{ t =>
      if (t == tab)
        t.classList.add("active")
      else
        t.classList.remove("active")
    }


  val searchInput: html.Input = input(
    placeholder:="Just start typing...",
    onkeyup:={ (evt: dom.KeyboardEvent) => {
      clearOnEscape(searchInput)(evt)
      updateFriendsDisplay()
    }}
  ).render

  def updateFriendsDisplay() = Ajaxer[Api].searchFriends(searchInput.value).call().foreach{ friends =>
    friendsDisplay.innerHTML = ""
    friendsDisplay.appendChild((
    for (Friend(firstname, secondname, email) <- friends) yield tr(
    th(firstname), td(secondname), td(a(href:=s"mailto:$email", email))
    )
    ).render)
  }

  val friendsDisplay = tbody.render


  val addInput = div(
    addInputs,
    onkeyup:={ (evt: dom.KeyboardEvent) => evt.keyCode match {
      case KeyCode.enter =>
        addFriend()
//        selectTab(searchTab)
//        searchInput.value = addFirstnameInput.value
//        addInputs.foreach(_.value = "")
      case _ => ()
    } }
  ).render

  val addInputs = Seq(
    addFirstnameInput,
    addSecondnameInput,
    addEmailInput
  )

  val addFirstnameInput = input(
    placeholder:="First name"
  ).render

  val addSecondnameInput = input(
    placeholder:="Second name"
  ).render

  val addEmailInput = input(
    placeholder:="Email"
  ).render

  def addFriend(): Unit = Ajaxer[Api].addFriend(
    Friend(
      addFirstnameInput.value,
      addSecondnameInput.value,
      addEmailInput.value
    )
  ).call().foreach{ _ =>
    selectTab(searchTab)
    searchInput.value = addFirstnameInput.value
    addInputs.foreach(_.value = "")
    updateFriendsDisplay()
  }
  }

//  override def render = {
//    val searchInput = input(
//      placeholder:="Just start typing..."
//    ).render
//    val friendsDisplay = tbody.render
//    def updateFriendsDisplay() = Ajaxer[Api].searchFriends(searchInput.value).call().foreach{ resultingFriends =>
//      friendsDisplay.innerHTML = ""
//      friendsDisplay.appendChild((
//        for (Friend(firstname, secondname, email) <- resultingFriends) yield tr(
//          th(firstname), td(secondname), td(a(href:=s"mailto:$email", email))
//        )
//      ).render)
//    }
//    searchInput.onkeyup = (evt: dom.KeyboardEvent) => {
//      clearOnEscape(searchInput)(evt)
//      updateFriendsDisplay()
//    }
//
//    val addFirstnameInput = input(
//      placeholder:="First name"
//    ).render
//    val addSecondnameInput = input(
//      placeholder:="Second name"
//    ).render
//    val addEmailInput = input(
//      placeholder:="Email"
//    ).render
//    val addInputs = Seq(
//      addFirstnameInput,
//      addSecondnameInput,
//      addEmailInput
//    )
//    addInputs.foreach(in => in.onkeyup = clearOnEscape(in) _)
//    val addInput = div(addInputs).render
//
//    val searchTab = div(cls:="tab-pane active",
//      searchInput,
//      table(cls:="table",
//        friendsDisplay
//      )
//    ).render
//    val addTab = div(cls:="tab-pane",
//      addInput
//    ).render
//    val tabs = ListMap(
//      "Search" -> searchTab,
//      "Add" -> addTab
//    )
//
//    def selectTab = selectTabFrom(tabs.values) _
//
//    def addFriend() = Ajaxer[Api].addFriend(
//      Friend(
//        addFirstnameInput.value,
//        addSecondnameInput.value,
//        addEmailInput.value
//      )
//    ).call().foreach{ _ =>
//      selectTab(searchTab)
//      searchInput.value = addFirstnameInput.value
//      addInputs.foreach(_.value = "")
//      updateFriendsDisplay()
//    }
//    addInput.onkeyup = (e: dom.KeyboardEvent) => e.keyCode match {
//      case KeyCode.enter =>
//        addFriend()
////        selectTab(searchTab)
////        searchInput.value = addFirstnameInput.value
////        addInputs.foreach(_.value = "")
//      case _ => ()
//    }
//
//    div(
//      div(cls:="container",
//        div(cls:="page-header",
//          h1(fragName)
//        ),
//        div(
//          ul(cls:="nav nav-tabs",
//            for ((name, tab) <- tabs.toSeq) yield li(
//              a(name, onclick:={ () => selectTab(tab) })
//            )
//          ),
//          div(cls:="tab-content",
//            tabs.values.toSeq
//          )
//        )
//      )
//    ).render
//  }

//  private def clearOnEscape(in: html.Input)(evt: dom.KeyboardEvent) = evt.keyCode match {
//    case KeyCode.escape => in.value = ""
//    case _ => ()
//  }
//
//  private def selectTabFrom(tabs: Iterable[html.Element])(tab: html.Element) =
//    tabs.foreach(t =>
//      if (t == tab)
//        t.classList.add("active")
//      else
//        t.classList.remove("active")
//    )
}
