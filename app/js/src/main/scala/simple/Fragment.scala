package simple

import org.scalajs.dom.html

trait Fragment {
  val fragName: String
  def render: html.Element
}
