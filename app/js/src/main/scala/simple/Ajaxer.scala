package simple

import org.scalajs.dom
import scalajs.concurrent.JSExecutionContext.Implicits.runNow

object Ajaxer extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer] {
  override def doCall(req: Request) = {
    dom.ext.Ajax.post(
      url = "/ajax/" + req.path.mkString("/"),
      data = upickle.default.write(req.args)
    ).map(_.responseText)
  }

  def read[Result: upickle.default.Reader](p: String) = upickle.default.read[Result](p)
  def write[Result: upickle.default.Writer](r: Result) = upickle.default.write(r)
}
