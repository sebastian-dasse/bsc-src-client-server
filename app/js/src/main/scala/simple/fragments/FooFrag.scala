package simple.fragments

import scalatags.JsDom.all._

import simple.Fragment


object FooFrag extends Fragment {
  override val fragName = "Foo"
  override def render = div(cls:="container",
    div(cls:="page-header",
      h1(fragName)
    ),
    p("some foo stuff")
  ).render
}
