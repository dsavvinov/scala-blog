package posts

import org.scalajs.jquery.{JQueryAjaxSettings, jQuery => $}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
class PostSubmit {
  @JSExport
  def submitPost(): Unit = {
    var titleVal = $("#post-title").`val`();
    var text = $("#post-content").`val`();
    var descriptionVal = $("#post-short-description").`val`();

    $.ajax(js.Dynamic.literal(
      url = "add-post",
      `type` = "POST",
      data = js.JSON.stringify(js.Dynamic.literal(
        title = titleVal,
        content = text,
        description = descriptionVal
      )),
      contentType = "application/json; charset=utf-8",
      dataType = "json"
    ).asInstanceOf[JQueryAjaxSettings]);
  }
}
