$(document).ready(function () {
    $("#login_form").submit(function (event) {
        let data = JSON.stringify({phone:$("#phone").val(),password:$("#password").val()});
        console.log(data)
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: "/api/providers/login",
            data: JSON.stringify({phone: $('#phone').val(), password: $('#password').val()}),
            success: function (result) {
                if (result && result["login_check"] == true) {
                    window.location.href = "http://localhost:8080/view/provider_info.html?id=" + result["loginInfo"]["id"];
                } else {
                    alert(result["login_check"]);
                }
            }
        });
        // alert(JSON.stringify({email : $('#email').val(), password : $('#password').val()}));
        event.preventDefault();
    });
});