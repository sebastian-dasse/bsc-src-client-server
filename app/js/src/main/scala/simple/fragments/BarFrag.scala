package simple.fragments

import scalatags.JsDom.all._

import simple.Fragment


object BarFrag extends Fragment {
  override val fragName = "Bar"
  override def render =
    div(cls:="container",
      div(cls:="page-header",
        h1(fragName)
      ),
      p("some bar stuff")
    ).render
}
