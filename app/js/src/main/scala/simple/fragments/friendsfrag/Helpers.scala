package simple.fragments.friendsfrag

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html


object Helpers extends Helpers

trait Helpers {
  def clearOnEscape(in: html.Input)(evt: dom.KeyboardEvent) = evt.keyCode match {
    case KeyCode.escape => in.value = ""
    case _ => ()
  }

  def flashMessageAt(msgDisplay: html.Element)(msg: String): Unit = {
    msgDisplay.textContent = msg
    val msgContainer = msgDisplay.parentElement
    msgContainer.classList.remove("hidden")
    dom.setTimeout(() => {
      msgContainer.classList.add("hidden")
      msgDisplay.textContent = ""
    }, 3000)
  }

  def checkInput(in: html.Input, pred: String => Boolean): Boolean = {
    val ok = pred(in.value)
    if (!ok) {
      flashHighlight(in)
    }
    ok
  }

  def flashHighlight(elm: html.Input): Unit = {
    elm.classList.add("error")
    once(elm, "keyup", _ => elm.classList.remove("error"))
  }

  /// adapted from:  http://stackoverflow.com/questions/27245005/arguments-callee-in-scala-js
  def once(elm: html.Element, tpe: String, callback: Function1[dom.Event, Any]): Unit = {
    lazy val cb: scalajs.js.Function1[dom.Event, Any] = { (evt: dom.Event) =>
      evt.target.removeEventListener(evt.`type`, cb)
      callback(evt)
    }
    tpe.split(" ").foreach(item => elm.addEventListener(item, cb))
  }

  val validEmail = """\w+@\w+\.\w+""".r

  def isValidEmail(str: String) = str match {
    case validEmail() => true
    case _ => false
  }
}
