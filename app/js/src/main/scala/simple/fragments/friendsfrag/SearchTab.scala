package simple.fragments.friendsfrag

import org.scalajs.dom
import org.scalajs.dom.html
import scalatags.JsDom.all._
import autowire._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

import simple._


object SearchTab extends Fragment with Autofocus with Helpers {
  override val fragName: String = "Search"

  /*
  /// 1. alternative - Tab forgets state
  override def render: Element = new SearchTabImpl().render
  private object SearchTabImpl {...}
  /// 2. alternative - Tab remembers state
  override def render: Element = SearchTabImpl.render
  private class SearchTabImpl {...}
  */

  private var impl = new SearchTabImpl

  override def render: html.Element = {
    impl = new SearchTabImpl
    impl.render
  }

  override def autofocus() =
    dom.setTimeout(() => impl.searchInput.focus(), 10)


  private class SearchTabImpl {

    lazy val searchInput: html.Input =
      input(tpe:="text", cls:="form-control", placeholder := "Just start typing...",
        onkeyup:={ (evt: dom.KeyboardEvent) => {
          clearOnEscape(searchInput)(evt)
          updateFriendsDisplay()
        } }
      ).render

    val friendsDisplay = tbody.render

    def updateFriendsDisplay(): Unit = Ajaxer[Api].searchFriends(searchInput.value).call().foreach { friends =>
      friendsDisplay.innerHTML = ""
      friendsDisplay.appendChild((
        for (f <- friends) yield {
          val deletBtn = button(cls:="hidden btn btn-default btn-xs", "X", onclick:={ () => removeFriend(f) }).render
          tr(
            th(f.firstname),
            td(f.secondname),
            td(a(href := s"mailto:${f.email}", f.email)),
            td(deletBtn),
            onmouseover:={ () => deletBtn.classList.remove("hidden")},
            onmouseout:={ () => deletBtn.classList.add("hidden")}
          )
        }
      ).render)
    }

    def removeFriend(friend: Friend) = Ajaxer[Api].removeFriend(friend).call().foreach{ _ =>
      updateFriendsDisplay()
    }

    def render: html.Element =
      div(cls := "tab-pane active", id := "search-tab",
        searchInput,
        div(cls := "container",
          table(cls := "table table-hover",
            friendsDisplay
          )
        )
      ).render
  }
}
