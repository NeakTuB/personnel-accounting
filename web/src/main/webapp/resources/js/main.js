let language_url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/English.json";

function show_preloader() {
  $("#preloader_malc").show();
}

function hide_preloader() {
  $("#preloader_malc").hide();
}

$(document).ready(function () {

  if (localStorage.getItem("lang") != null && window.location.href.indexOf("logout") > -1
    && window.location.href.indexOf("lang") <= -1) {
    window.location.replace('?logout' + '&lang=' + localStorage.getItem("lang"));
    switchTableLang(localStorage.getItem("lang"));
  } else if (localStorage.getItem("lang") != null && window.location.href.indexOf("error") > -1
    && window.location.href.indexOf("lang") <= -1) {
    window.location.replace('?error' + '&lang=' + localStorage.getItem("lang"));
    switchTableLang(localStorage.getItem("lang"));
  } else if (localStorage.getItem("lang") != null && window.location.href.indexOf("lang") <= -1) {
    window.location.replace('?lang=' + localStorage.getItem("lang"));
    switchTableLang(localStorage.getItem("lang"));
  } else switchTableLang(localStorage.getItem("lang"));

  if (localStorage.getItem("imageUrl") != null) {
    $("#navbar_image").attr("src", localStorage.getItem("imageUrl"));
    $("#navbar_name").text(localStorage.getItem("name"));
    $("#main_menu_name").text(localStorage.getItem("name"));
  }
  else {
    $.ajax({
      type: "GET",
      headers: {"Authorization": sessionStorage.getItem('tokenData')},
      contentType: "application/json",
      url: "/api/employee/profile/get_profile_data",
      cache: false,
      timeout: 600000,
      success: function (data) {
        let image = "http://localhost:8080/api/downloadFile/" + data.profile.image.id.toString();
        $("#navbar_image").attr("src", image);
        localStorage.setItem("imageUrl", image);
        localStorage.setItem("name", data.name);
        $("#navbar_name").text(data.name);
        $("#main_menu_name").text(data.name);
      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        $('.alert').append(`<div class="alert alert-danger"role="alert">500 Error!</div>`);
      }
    });
  }

  $("#en_lang").click(function () {
    window.location.replace('?lang=' + $('#en_lang').val());
    localStorage.setItem("lang", "en");
    switchTableLang(localStorage.getItem("lang"));
  })

  $(".sidebar_element").click(function () {
    $(".sidebar_element").removeClass('sidebar_bold_element');
    $(this).addClass('sidebar_bold_element');
  })

  $("#ru_lang").click(function () {
    window.location.replace('?lang=' + $('#ru_lang').val());
    localStorage.setItem("lang", "ru");
    switchTableLang(localStorage.getItem("lang"));
  })

  $("#logoutBtn").click(function () {
    sessionStorage.removeItem('tokenData');
  })
});

function switchTableLang(lang) {
  lang == "ru"
    ? language_url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
    : language_url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/English.json";
}

function get_message(lang, property) {
  let message = null;
  $.ajax({
    type: "GET",
    headers: {"Authorization": sessionStorage.getItem('tokenData')},
    contentType: "application/json",
    url: "/api/messages/" + lang + "/" + property,
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      message = data;
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
  return message;
}