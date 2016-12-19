function onSubmitBtnClick() {
    var title = $("#post-title").val();
    var text = $("#post-content").val();
    var description = $("#post-short-description").val();

    $.ajax({
        url:"add-post",
        type:"POST",
        data: JSON.stringify({ "title": title, "content": text, "description": description }),
        contentType:"application/json; charset=utf-8",
        dataType:"json"
    });
}