package controllers

import javax.inject._

import models.{PostsDB, User, UsersDAO}
import play.api.mvc._
import play.twirl.api.HtmlFormat

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action.async { request =>
    val maybeUserStringID: Option[String] = request.session.get("user")
    val user: Future[Option[User]] = if (maybeUserStringID.isDefined) {
      val stringId = maybeUserStringID.get
      val id = stringId.toLong
      UsersDAO.get(id)
    } else Future.successful(None)

    for {
      posts <- PostsDB.take(10)
      maybeUsers <- Future sequence posts.map { post => UsersDAO.get(post.author) }
      loggedUser <- user
      ; users = maybeUsers.filter(_.isDefined).map(_.get)
      ; previews = posts.zip(users)
            .map { case (p, u) => views.html.posts.post_preview(p, u.name) }
      ; previewsWithPager = previews :+ views.html.navigation.pager()
    } yield Ok(views.html.main(HtmlFormat.fill(previewsWithPager.toList), loggedUser))
  }
}
