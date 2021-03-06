package simple.fragments

import scalatags.JsDom.all._

import simple.Fragment


object HomeFrag extends Fragment {
  override val fragName = "Home"
  override def render =
    div(cls:="container",
      div(cls:="page-header",
        h1(fragName)
      ),
      p("Welcome!")
    ).render
}
