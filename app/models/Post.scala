package models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Post(
               id: Long,
               title: String,
               author: Long,
               publishDate: Date,
               description: String,
               content: String
               ) {
  def formatDate: String = {
    val fmt: SimpleDateFormat = new SimpleDateFormat("d, MMM, yyyy")
    fmt.format(publishDate)
  }
}

object PostsDB {
  private val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  private val posts = TableQuery[PostTableDef]
  implicit private val mapper = MappedColumnType.base[Date, Timestamp](
    d => new Timestamp(d.getTime),
    t => new Date(t.getTime)
  )

  def add(post: Post): Future[String] = {
    dbConfig.db.run(posts += post).map(res => "Post succesfully added").recover {
      case ex: Exception => ex.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(posts.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Post]] = {
    dbConfig.db.run(posts.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Post]] = {
    dbConfig.db.run(posts.result)
  }

  def take(n: Int): Future[Seq[Post]] = {
    dbConfig.db.run(posts.sortBy(_.publishDate).take(n).result)
  }
}

class PostTableDef(tag: Tag) extends Table[Post](tag, "post") {
  implicit val mapper = MappedColumnType.base[Date, Timestamp](
    d => new Timestamp(d.getTime),
    t => new Date(t.getTime)
  )

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def author = column[Long]("author")
  def publishDate = column[Date]("publish_date")
  def description = column[String]("description")
  def content = column[String]("content")

  def * = (id, title, author, publishDate, description, content) <> (Post.tupled, Post.unapply)
}

object DateMapper {
  val utilDate2SqlDate = MappedColumnType.base[java.util.Date, java.sql.Timestamp](
    { utilDate => new Timestamp(utilDate.getTime) },
    { timestamp => new Date(timestamp.getTime) }
  )
}
