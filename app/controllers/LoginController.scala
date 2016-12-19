package controllers

import javax.inject.{Inject, Singleton}

import models.{User, UsersDAO}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.JsValue
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class LoginController @Inject() (homeController: HomeController) extends Controller {

  def tryLogin = Action.async { implicit request =>
    val loginForm = Form(
      mapping(
        "login" -> text,
        "password" -> text
      )(LoginData.apply)(LoginData.unapply)
    )

    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(Redirect(routes.HomeController.index()))
      },
      loginData => {
        val login = loginData.login
        val pass = loginData.password
        UsersDAO.checkCreditentials(login, pass) map {
          case Some(user) => Redirect(routes.HomeController.index()).withSession("user" -> user.id.toString)
          case None => Redirect(routes.HomeController.index())
        }
      }
    )
  }

  def logout = Action { implicit request =>
    Redirect(routes.HomeController.index()).withNewSession
  }

  def registration = Action {
    Ok(views.html.main(views.html.creditentials.registration()))
  }

  def register = Action { implicit request =>
    val registerForm = Form(
      mapping(
        "username" -> text,
        "login" -> text,
        "password" -> text
      )(RegistrationData.apply)(RegistrationData.unapply)
    )

    registerForm.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.main(views.html.creditentials.registration(Fail(formWithErrors.errors.toString()))))
      },
      registrationData => {
        val name = registrationData.name
        val login = registrationData.login
        val pass = registrationData.password

        UsersDAO.add(User(0, name, login, pass))
        Ok(views.html.main(views.html.creditentials.registration(Success())))
      }
    )
  }

}

sealed class RegistrationResult
case class Fail(message: String) extends RegistrationResult
case class Success() extends RegistrationResult

case class LoginData(login: String, password: String)

case class RegistrationData(name: String, login: String, password: String)