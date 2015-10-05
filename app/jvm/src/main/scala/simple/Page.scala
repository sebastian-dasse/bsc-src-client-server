package simple

import scalatags.Text.all._

object Page {
//  val loginArea = div(/*form(*/)
//  val logoutArea = div(/*form(*/
//    button(id:="btn-logout", "Logout")
//  )
//  val logout =
//    "simple.Client().logout(document.getElementById('btn-logout'))"
  val boot =
    "simple.Client().main(document.getElementById('content'))"
  val skeleton =
    html(
      head(
        scalatags.Text.tags2.title("Client-Server Project"),
        meta(httpEquiv:="Content-Type", content:="text/html; charset=UTF-8"),

        /* for development */
        script(src:="/client-fastopt.js"),
//        script(src:="/client-fastopt.js.map"),
        script(src:="//localhost:12345/workbench.js"),
        
        /* for production */
        // script(src:="/client-opt.js"),

        link(rel:="stylesheet", href:="style.css"),
        link(rel:="stylesheet", href:="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"),
        script(src:="https://code.jquery.com/jquery-2.1.4.min.js"),
        script(src:="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js")
      ),
      body(
        onload:=boot,
//        div(
//          logoutArea
//        ),
        div(id:="content")//,

        /* for development */
//        script(src:="//localhost:12345/workbench.js")
      )
    )

  /*val loggedInSkeleton =
    html(
      head(),
      body(
        h1("Logged in")
      )
    )

  def getPage: String =
    if (!Server.loggedin)
      skeleton.render
    else
      loggedInSkeleton.render*/

}
