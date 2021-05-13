$(document).ready(function () {
    function addDistance(phone) {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: "/api/distance/providers/signup",
            data: JSON.stringify({phone:phone}),
            success: function (result) {
                console.log(result);
            }
        });
    }

    $("#login_form").submit(function (event) {
        let data = JSON.stringify({
            name: $("#name").val(),
            phone: $("#phone").val(),
            email: $("#email").val(),
            password: $("#password").val(),
            address: $("#address").val()
        });
        // console.log(data);
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: "/api/providers",
            data: data,
            success: function (result) {
                if(result["isExisted"] == true) {
                    alert("Account already existed!");
                } else {
                    addDistance($("#phone").val());
                    alert("Sign Up Successfully! Please log in with the registered account.");
                    window.location.href = "http://localhost:8080/view/provider_login.html";
                }
            }
        });
        event.preventDefault();
    });
});