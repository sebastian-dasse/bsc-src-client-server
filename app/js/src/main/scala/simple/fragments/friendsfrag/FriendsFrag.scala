package simple.fragments.friendsfrag

import org.scalajs.dom
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.nav

import simple._

/// inspiration for boostrap-tabs from:  https://gist.github.com/mnewt/4228037
object FriendsFrag extends Fragment {
  override val fragName = "Friends"

  override def render = {
    val tabs = Seq(SearchTab, AddTab)
    div(cls:="container",
      div(cls:="page-header",
        h1(fragName)
      ),
      div(
        nav(
          ul(cls:="nav nav-tabs", "data-tabs".attr:="tabs",
            for (tab <- tabs) yield
              li(cls:=( if (tab == SearchTab) "active" else "" ),
                a(href:=s"#${tab.fragName.toLowerCase}-tab", "data-toggle".attr:="tab", tab.fragName,
                  onclick:={ (evt: dom.Event) =>
                    evt.preventDefault()
                    tab.autofocus()
                  }
                )
              )
          )
        ),
        div(cls:="container tab-content",
          tabs.map(_.render)
        )
      )
    ).render
  }

  /// more declarative alternative - closer to html
  /*override def render = {
    def tabOnclick(a: Autofocus) = (evt: dom.Event) => {
      evt.preventDefault()
      a.autofocus()
    }
    div(cls:="container",
      div(cls:="page-header",
        h1(fragName)
      ),
      div(
        nav(
          ul(cls:="nav nav-tabs", "data-tabs".attr:="tabs",
            li(cls:="active",
              a(href:="#search-tab", "data-toggle".attr:="tab", "Search",
                onclick:=tabOnclick(SearchTab)
              )
            ),
            li(
              a(href:="#add-tab", "data-toggle".attr:="tab", "Add",
                onclick:=tabOnclick(AddTab)
              )
            )
          )
        ),
        div(cls:="container tab-content",
          SearchTab.render,
          AddTab.render
        )
      )
    ).render
  }*/
}
