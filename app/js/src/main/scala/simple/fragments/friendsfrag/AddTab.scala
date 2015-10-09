package simple.fragments.friendsfrag

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html
import scalatags.JsDom.all._
import autowire._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

import simple._


object AddTab extends Fragment with Autofocus with Helpers {
  override val fragName: String = "Add"

  private var impl = new AddTabImpl

  override def render: html.Element = {
    impl = new AddTabImpl
    impl.render
  }

  override def autofocus() =
    dom.setTimeout(() => impl.firstnameInput.focus(), 20)


  private class AddTabImpl {

    private val cfg = Seq(tpe:="text", cls:="form-control")
    val firstnameInput =  input(cfg, id:="input-firstname",  placeholder:="First name").render
    val secondnameInput = input(cfg, id:="input-secondname", placeholder:="Second name").render
    val emailInput =      input(cfg, id:="input-email",      placeholder:="Email").render

    val successDisplay = b.render

    def addFriend(): Unit = {
      val friend = Friend(
        firstnameInput.value,
        secondnameInput.value,
        emailInput.value
      )
      Ajaxer[Api].addFriend(friend).call().foreach{ _ =>
        flashMessageAt(successDisplay)(s"${friend.toPrettyString} successfully added")
      }
    }

    def render: html.Element = {
      val inputs = Seq(
        firstnameInput,
        secondnameInput,
        emailInput
      )
      def clearInputs() = inputs.foreach(_.value = "")

      def validInput(): Boolean = List(
        checkInput(firstnameInput, !_.isEmpty),
        checkInput(emailInput, isValidEmail)
      ).reduceLeft(_ && _)

      def submit() = if (validInput()) {
        addFriend()
        clearInputs()
        firstnameInput.focus()
      }

      inputs.foreach(in => in.onkeyup = clearOnEscape(in) _)

      div(cls:="tab-pane", id:="add-tab",
        form(cls:="form-horizontal",
          onclick:={ (evt: dom.Event) => evt.preventDefault() },
          div(cls:="form-group",
            label(`for`:="input-firstname", cls:="col-sm-2 control-label", "First name"),
            div(cls:="col-sm-10",
              firstnameInput
            )
          ),
          div(cls:="form-group",
            label(`for`:="input-secondname", cls:="col-sm-2 control-label", "Second name"),
            div(cls:="col-sm-10",
              secondnameInput
            )
          ),
          div(cls:="form-group",
            label(`for`:="input-email", cls:="col-sm-2 control-label", "Email"),
            div(cls:="col-sm-10",
              emailInput
            )
          ),
          div(cls:="form-group",
            div(cls:="col-sm-offset-2 col-sm-1",
              button(tpe:="button", cls:="btn btn-default", "Add",
                onclick:={ (evt: dom.Event) => submit() }
              )
            ),
            div(cls:="hidden col-sm-8 alert alert-success",
              successDisplay
            )
          ),
          onkeyup:={ (evt: dom.KeyboardEvent) => evt.keyCode match {
            case KeyCode.enter => submit()
            case _ => ()
          } }
        )
      ).render
    }
  }
}
