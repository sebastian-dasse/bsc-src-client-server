//package simple
//
//import org.scalajs.dom
//import org.scalajs.dom.html
//import scalatags.JsDom.all._
//import scala.concurrent.{Future, Promise}
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.util.{Failure, Success, Try}
//import utest._
//
//import scala.collection.immutable.ListMap
//
//object RoutingTest extends TestSuite {
//
//  object MockClient extends Routing{
//
//    object Foo extends Fragment {
//      override val fragName: String = "Foo"
//      override def render: html.Element = div(id:=fragName).render
//    }
//
//    object Bar extends Fragment {
//      override val fragName: String = "Bar"
//      override def render: html.Element = div(id:=fragName).render
//    }
//
//    override val routes: ListMap[String, Fragment] = ListMap(
//    "/foo" -> Foo,
//    "/bar" -> Bar
//    )
//    override val defaultRoute: Fragment = Foo
//    override val contentContainer: html.Element = div().render
//  }
//
//  val tests = TestSuite{
//    "router" - {
//      dom.window.location.hash = "/bar"
//      /*// does not work - yields always true
//      dom.setTimeout(() => {
//        assert(MockClient.contentContainer.children.length == 12)
//        assert(MockClient.contentContainer.firstElementChild.id == "Bar")
//      } , 200)*/
//
//      // try  -->  http://stackoverflow.com/questions/30746810/how-do-i-test-scala-js-programs-for-side-effects-that-happen-asynchronously-usin
//    }
//  }
//}
//
