package simple

case class FileData(name: String, size: Long)

case class MyFoo(contents: String, id: Long)

case class Friend(firstname: String, secondname: String, email: String)

case class User(name: String, password: String)


trait Api {
  def list(path: String): Seq[FileData]
  def foo(str: String): MyFoo
  def friends(searchStr: String): Seq[Friend]
  def addFriend(name: String): Boolean
  def login(user: User): Boolean
  def logout(): Boolean
}
