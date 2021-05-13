$(document).ready(function () {
    $("#login_form").submit(function (event) {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: "/api/patients/login",
            data: JSON.stringify({email: $('#email').val(), password: $('#password').val()}),
            success: function (result) {
                if (result && result["login_check"] == true) {
                    // alert(JSON.stringify(result["loginInfo"]));
                    window.location.href = "http://localhost:8080/view/patient_info.html?id=" + result["loginInfo"]["id"];
                } else {
                    alert(result["login_check"]);
                }
            }
        });
        // alert(JSON.stringify({email : $('#email').val(), password : $('#password').val()}));
        event.preventDefault();
    });
    // $("#sign-up").click(function (event) {
    //
    // })
});