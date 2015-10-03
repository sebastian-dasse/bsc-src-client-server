package simple

import akka.actor.ActorSystem
import spray.http.{HttpEntity, MediaTypes}
import spray.routing.SimpleRoutingApp
import scala.concurrent.ExecutionContext.Implicits.global

object Router extends autowire.Server[String, upickle.default.Reader, upickle.default.Writer] {
  def read[Result: upickle.default.Reader](p: String) = upickle.default.read[Result](p)
  def write[Result: upickle.default.Writer](r: Result) = upickle.default.write(r)
}

object Server extends SimpleRoutingApp with Api {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    startServer(interface = "localhost", port = 8080) {
      get{
        pathSingleSlash{
          complete{
            HttpEntity(
              MediaTypes.`text/html`,
//              Page.skeleton.render
              if (loggedin) Page.loggedInSkeleton.render else Page.skeleton.render
//              Page.getPage
            )
          }
        } ~
          getFromResourceDirectory("")
      } ~
      get{
        path("contents"){
          complete{
            HttpEntity(
              MediaTypes.`text/html`,
              Page.skeleton.render      // TODO use another page to render
            )
          }
        } ~
          getFromResourceDirectory("")
      } ~
      post{
        path("ajax" / Segments){ s =>
          extract(_.request.entity.asString) { e =>
            complete {
              Router.route[Api](Server)(
                autowire.Core.Request(
                  s,
                  upickle.default.read[Map[String, String]](e)
                )
              )
            }
          }
        }
      }
    }
  }
  override def foo(str: String): MyFoo = {
    println(s"server> $str")
    MyFoo(str.toUpperCase, System.currentTimeMillis)
  }

  var friendsList = List(
    Friend("Alice", "Wonderland", "alice@wonderland.org"),
    Friend("Carla", "Columna", "carla.columna@kiddinx.de"),
    Friend("Bob", "Martin", "bobby@cleancode.com")
  )

  implicit class CaseInsensitiveString(s: String) {
    def containsIngoreCase(other: String) = s.toLowerCase.contains(other.toLowerCase)
  }

  override def searchFriends(searchStr: String): Seq[Friend] = {
    if (searchStr.isEmpty) Nil
    else friendsList.filter(f =>
      f.firstname.containsIngoreCase(searchStr) ||
      f.secondname.containsIngoreCase(searchStr)
    ).take(10)
  }

  override def addFriend(friend: Friend): Unit =
    friendsList = friend :: friendsList

  var userList = Seq[User](
    User("Alice", "secret")
  )
  var loggedin = false

  override def login(user: User): Boolean = {
    loggedin = userList.contains(user)
    val status = if (loggedin) "LOGGED IN" else "LOGGED OUT"
    println(s"server> NOW $status")
    println(s"server> $user")
    loggedin
  }

  override def logout(): Boolean = {
    loggedin = false
    loggedin
  }
}
