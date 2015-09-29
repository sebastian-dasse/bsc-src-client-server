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
    "simple.Client().main(document.getElementById('contents'))"
  val skeleton =
    html(
      head(
        //title("Client-Server Base Project"),
        meta(httpEquiv:="Content-Type", content:="text/html; charset=UTF-8"),
        
        /* for development */
        script(`type`:="text/javascript", src:="/client-fastopt.js"),
        //script(`type`:="text/javascript", src:="//localhost:12345/workbench.js"),
        
        /* for production */
        // script(src:="/client-opt.js"),
        
        link(
          rel:="stylesheet",
          `type`:="text/css",
          href:="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"
          //href:="https://cdnjs.cloudflare.com/ajax/libs/pure/0.5.0/pure-min.css"
        )
      ),
      body(
        onload:=boot,
//        div(
//          logoutArea
//        ),
        div(id:="contents"),

        /* for development */
        script(`type`:="text/javascript", src:="//localhost:12345/workbench.js")
      )
    )

  val loggedInSkeleton =
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
      loggedInSkeleton.render

}
