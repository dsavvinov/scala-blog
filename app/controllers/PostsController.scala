package controllers

import java.util.Date
import javax.inject.{Inject, Singleton}

import models.{Post, PostsDB, UsersDAO}
import play.api.libs.json.JsValue
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class PostsController @Inject() extends Controller {
  def addPost = Action.async { request =>
    val body: JsValue = request.body.asJson.get
    val title: String = (body \ "title").as[String]
    val content: String = (body \ "content").as[String]
    val description: String = (body \ "description").as[String]

    val post = Post(0, title, 1, new Date(), description, content)
    PostsDB.add(post) map {
      result =>
        print(result)
        Redirect(routes.HomeController.index())
    }
  }

  def showPost(id: Long) = Action.async { implicit request =>
    PostsDB.get(id) flatMap {
      case Some(post) =>
        UsersDAO.get(post.id) map {
          case Some(user) =>
            Ok(views.html.main(views.html.posts.post(post, user.name)))
          case None => NotFound(<h1>User not found</h1>)
        }
      case None => Future { NotFound(<h1>Post not found</h1>) }
    }
  }
}
