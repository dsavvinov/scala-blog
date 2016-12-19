package models

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.lifted.{TableQuery, Tag}
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class User(
               id: Long,
               name: String,
               login: String,
               password: String
               ) { }

object UsersDAO {
  private val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  private val users = TableQuery[UsersTableDef]

  def add(user: User): Future[String] = {
    dbConfig.db.run(users += user).map(res => "User successfully added").recover {
      case ex: Exception => ex.getMessage
    }
  }

  def get(id: Long): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.id === id).result.headOption)
  }

  def checkCreditentials(login: String, pass: String) : Future[Option[User]] = {
    dbConfig.db.run(users.filter(user => user.login === login && user.password === pass).result.headOption)
  }
}

class UsersTableDef(tag: Tag) extends Table[User](tag, "user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def login = column[String]("login")
  def password = column[String]("password")

  def * = (id, name, login, password) <> (User.tupled, User.unapply)
}