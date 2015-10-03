package simple

case class MyFoo(contents: String, id: Long)
case class Friend(firstname: String, secondname: String, email: String)
case class User(name: String, password: String)

trait Api {
  def foo(str: String): MyFoo
  def searchFriends(searchStr: String): Seq[Friend]
  def addFriend(friend: Friend): Unit
  def login(user: User): Boolean
  def logout(): Boolean
}



import org.scalajs.dom.html
import scalatags.JsDom.all._

trait Frag {
  def render: html.Element
}

object MyFrag1 extends Frag {

  override def render = {
    val foo = div
    def bar() = ()
    div(
      foo,
      onclick:={ () => bar() }
    ).render
  }
}

object MyFrag2 extends Frag {

  override def render = {
    div(
      foo,
      onclick:={ () => bar() }
    ).render
  }

  private val foo = div
  private def bar() = ()
}

object MyFrag3 extends Frag {

  override def render = {
    import ImplDetail._
    div(
      foo,
      onclick:={ () => bar() }
    ).render
  }

  private object ImplDetail {
    val foo = div
    def bar() = ()
  }
}
