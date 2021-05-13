$(document).ready(function () {
    function addDistance(email) {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: "/api/distance/patients/signup",
            data: JSON.stringify({email:email}),
            success: function (result) {
                console.log(result);
            }
        });
    }

    $("#login_form").submit(function (event) {
        let data = JSON.stringify({
            name: $("#name").val(),
            birth: $("#birth").val(),
            ssn: $("#ssn").val(),
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
            url: "/api/patients",
            data: data,
            success: function (result) {
                if (result["isExisted"] == true) {
                    alert("Account already existed!");
                } else {
                    addDistance($("#email").val());
                    alert("Sign Up Successfully! Please log in with the registered account.");
                    window.location.href = "http://localhost:8080/view/patient_login.html";
                }
            }
        });
        event.preventDefault();
    });
});