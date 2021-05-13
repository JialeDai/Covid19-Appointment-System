$(document).ready(function () {
    refreshPatientList();

    function getChangePriorityBtnChecked(priority, id, checkValue) {
        let returnStr;
        if (checkValue == 1) {
            returnStr = "<div class=\"form-check form-check-inline\">\n" +
                "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio1\" name='" + id + "' value=\"1\" checked>\n" +
                "  <label class=\"form-check-label\" for=\"inlineRadio1\">1</label>\n" +
                "</div>\n" +
                "<div class=\"form-check form-check-inline\">\n" +
                "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio2\" name='" + id + "' value=\"2\">\n" +
                "  <label class=\"form-check-label\" for=\"inlineRadio2\">2</label>\n" +
                "</div>\n" +
                "<div class=\"form-check form-check-inline\">\n" +
                "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio3\" name='" + id + "' value=\"3\">\n" +
                "  <label class=\"form-check-label\" for=\"inlineRadio3\">3</label>\n" +
                "</div>";
        } else if (checkValue == 2) {
            returnStr = "<div class=\"form-check form-check-inline\">\n" +
                "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio1\" name='" + id + "' value=\"1\">\n" +
                "  <label class=\"form-check-label\" for=\"inlineRadio1\">1</label>\n" +
                "</div>\n" +
                "<div class=\"form-check form-check-inline\">\n" +
                "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio2\" name='" + id + "' value=\"2\" checked>\n" +
                "  <label class=\"form-check-label\" for=\"inlineRadio2\">2</label>\n" +
                "</div>\n" +
                "<div class=\"form-check form-check-inline\">\n" +
                "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio3\" name='" + id + "' value=\"3\">\n" +
                "  <label class=\"form-check-label\" for=\"inlineRadio3\">3</label>\n" +
                "</div>";
        } else if (checkValue == 3) {
            returnStr = "<div class=\"form-check form-check-inline\">\n" +
                "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio1\" name='" + id + "' value=\"1\">\n" +
                "  <label class=\"form-check-label\" for=\"inlineRadio1\">1</label>\n" +
                "</div>\n" +
                "<div class=\"form-check form-check-inline\">\n" +
                "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio2\" name='" + id + "' value=\"2\">\n" +
                "  <label class=\"form-check-label\" for=\"inlineRadio2\">2</label>\n" +
                "</div>\n" +
                "<div class=\"form-check form-check-inline\">\n" +
                "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio3\" name='" + id + "' value=\"3\" checked>\n" +
                "  <label class=\"form-check-label\" for=\"inlineRadio3\">3</label>\n" +
                "</div>";
        }
        return returnStr;
    }

    $("#table-body-patients").on("change", ":radio", function (event) {
        // alert($(this).attr("name")+"    "+$(this).val());
        let date = JSON.stringify({"id": $(this).attr("name"), "priority": $(this).val()})
        $.ajax({
            url: "/api/patients/priority",
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            data: date,
            success: function (result) {
                alert("success set the priority of patient (name:" + result[name] + ") to " + result["priority"]);
            }
        });
        event.preventDefault();
    });

    function getChangePriorityBtn(priority, id) {
        let returnStr;
        returnStr = "<div class=\"form-check form-check-inline\">\n" +
            "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio1\" name='" + id + "' value=\"1\">\n" +
            "  <label class=\"form-check-label\" for=\"inlineRadio1\">1</label>\n" +
            "</div>\n" +
            "<div class=\"form-check form-check-inline\">\n" +
            "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio2\" name='" + id + "' value=\"2\">\n" +
            "  <label class=\"form-check-label\" for=\"inlineRadio2\">2</label>\n" +
            "</div>\n" +
            "<div class=\"form-check form-check-inline\">\n" +
            "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio3\" name='" + id + "' value=\"3\">\n" +
            "  <label class=\"form-check-label\" for=\"inlineRadio3\">3</label>\n" +
            "</div>";
        return returnStr;
    }

    function processUrl(input) {
        const strArr = input.split("/");
        return "../upload/additional_info/" + strArr[strArr.length - 1];
    }

    function getFileName(input) {
        const strArr = input.split("/");
        return strArr[strArr.length - 1];
    }

    let additionInfo;

    function displayPatientInfo(id) {
        $.ajax({
           url: "/api/patients/"+id,
           method: "GET",
           success: function (result) {
               $("#email").val(result["email"]);
               $("#status").val(result["status"]);
               $("#birth").val(result["birth"]);
               $("#max-distance").val(result["maxDistance"]);
               $("#address").val(result["address"]);
           }
        });
    }

    $("#table-body-patients").on("click", "#btn-patient-id", function (event) {
        displayPatientInfo($(this).val());
        event.preventDefault();
    });


    function refreshPatientList() {
        $("#table-body-patients").empty();
        $.ajax({
                url: "/api/patients",
                method: "GET",
                success: function (result) {
                    for (let item of result) {
                        if (item["additionalInfo"] != null) {
                            const downloadUrl = processUrl(item["additionalInfo"]);
                            const downloadFileName = getFileName(item["additionalInfo"]);
                            additionInfo = "<a href = \"" + downloadUrl + "\" download=\"" + downloadFileName + "\">" + downloadFileName + "</a>";
                        } else {
                            additionInfo = "null"
                        }
                        if (item["priority"] != null) {
                            $("#table-body-patients").append("<tr style='alignment: center; text-align: center'> " +
                                "<td><button type='button' class='btn btn-outline-primary' id='btn-patient-id' value='" + item["id"] + "'>" + item["id"] + "</button></td>" +
                                "<td>" + item["name"] + "</td>" +
                                // "<td>" + item["address"] + "</td>" +
                                "<td>" + additionInfo + "</td>" +
                                "<td>" + getChangePriorityBtnChecked(item["priority"], item["id"], item["priority"]) + "</td>"
                            );
                        } else {
                            $("#table-body-patients").append("<tr style='alignment: center; text-align: center'> " +
                                "<td><button type='button' class='btn btn-outline-primary' id='btn-patient-id' value='" + item["id"] + "'>" + item["id"] + "</button></td>" +
                                "<td>" + item["name"] + "</td>" +
                                // "<td>" + item["address"] + "</td>" +
                                "<td>" + additionInfo + "</td>" +
                                "<td>" + getChangePriorityBtn(item["priority"], item["id"]) + "</td>"
                            );
                        }
                    }
                }
            }
        );
    }
});