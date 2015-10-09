package simple

case class Friend(firstname: String, secondname: String, email: String) extends Ordered[Friend] {
  override def compare(other: Friend): Int = {
    import scala.math.Ordered.orderingToOrdered
    (firstname, secondname).compare((other.firstname, other.secondname))
  }
  def toPrettyString = s"$firstname $secondname <$email>"
}

/*case class User(name: String, password: String)*/

trait Api {
  def searchFriends(searchStr: String): Seq[Friend]
  def addFriend(friend: Friend): Unit
  def removeFriend(friend: Friend): Unit
  /*def login(user: User): Boolean
  def logout(): Boolean*/
}
