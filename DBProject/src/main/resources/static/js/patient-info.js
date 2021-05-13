$(document).ready(function () {
    let isWeekDaySelected = false;
    let isTimeSlotSelected = false;
    let weekDayArr = ["sun", "mon", "tue", "wen", "thu", "fri", "sat"];
    let weekDay = -1;
    let timeSlot = -1;
    let cellId = "";

    // process the url of the additional_info
    function processUrl(input) {
        const strArr = input.split("/");
        return "../upload/additional_info/" + strArr[strArr.length - 1];
    }

    function refreshCurrentAppointment() {
        $("#table-body-current-appointment").empty();
        $.ajax({
            url: "/api/patients/appointment/current/" + id,
            method: "GET",
            success: function (result) {
                for (let item of result) {
                    switch (item["status"]) {
                        case "assigned":
                            actionType = "<button style='alignment: center' value=\"" + item["id"] + "\" type=\"button\" class=\"btn btn-light\" id=\"btn-action-accept\">accept</button>" +
                                "<button style='alignment: center' value=\"" + item["id"] + "\" type=\"button\" class=\"btn btn-light\" id=\"btn-action-decline\">decline</button>";
                            break;
                        case "accepted":
                            actionType = "<button style='alignment: center' value=\"" + item["id"] + "\" type=\"button\" class=\"btn btn-light\" id=\"btn-action-cancel\" onclick=''>cancel</button>";
                            break;
                    }
                    $("#table-body-current-appointment").append("<tr style='alignment: center; text-align: center'> " +
                        "<td>" + item["id"] + "</td>" +
                        "<td>" + item["patient"] + "</td>" +
                        "<td>" + item["provider"] + "</td>" +
                        "<td>" + item["distance"] + "</td>" +
                        "<td>" + item["start_time"] + "</td>" +
                        "<td>" + item["end_time"] + "</td>" +
                        "<td>" + item["status"] + "</td>" +
                        "<td>" + actionType + "</td>" +
                        " </tr>");
                }
            }
        });
    }

    function refreshHistoryAppointment() {
        $("#table-body-history-appointment").empty();
        $.ajax({
            url: "/api/patients/appointment/log/" + id,
            method: "GET",
            success: function (result) {
                for (let item of result) {
                    switch (item["status"]) {
                        case "assigned":
                            actionType = "<button style='alignment: center' value=\"" + item["id"] + "\" type=\"button\" class=\"btn btn-light\" id=\"btn-action-accept\">accept</button>" +
                                "<button style='alignment: center' value=\"" + item["id"] + "\" type=\"button\" class=\"btn btn-light\" id=\"btn-action-decline\">decline</button>";
                            break;
                        case "accepted":
                            actionType = "<button style='alignment: center' value=\"" + item["id"] + "\" type=\"button\" class=\"btn btn-light\" id=\"btn-action-cancel\" onclick=''>cancel</button>";
                            break;
                    }
                    $("#table-body-history-appointment").append("<tr style='alignment: center; text-align: center'> " +
                        "<td>" + item["provider"] + "</td>" +
                        "<td>" + item["patient"] + "</td>" +
                        "<td>" + item["start_time"] + "</td>" +
                        "<td>" + item["end_time"] + "</td>" +
                        "<td>" + item["action"] + "</td>" +
                        "<td>" + item["log_time"] + "</td>" +
                        " </tr>");
                }
            }
        });
    }

    function updateAppointmentLog(action) {
        $.ajax({
            url: "/patients/appointment/log",
            method: "POST",
            headers: {"Content-Type": "application/json"},
            data: {"id": $(this).val(), "action": action, "patient_id": id},
            success: function (result) {
                alert(this.data)
            }
        });
    }

    function getFileName(input) {
        const strArr = input.split("/");
        return strArr[strArr.length - 1];
    }

    $(".scheduler").hide();
    $(".appointment").hide();
    $(".profile").show();
    const url = location.search;
    const theRequest = new Object();
    if (url.indexOf("?") != -1) {
        const str = url.substr(1);
        for (let i = 0; i < str.length; i++) {
            theRequest[str.split("=")[0]] = str.split("=")[1];
        }
    }
    const id = theRequest["id"];
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "GET",
        url: "/api/patients/" + id,
        success: function (result) {
            $(".logo").text("EMAIL:" + result.email + " | NAME:" + result.name + " (patient)");
            $("#InputName").val(result.name);
            $("#InputEmail").val(result.email);
            $("#InputSSN").val(result["ssn"]);
            $("#InputBirth").val(result["birth"]);
            $("#InputAddress").val(result["address"]);
            $("#InputPhone").val(result.phone);
            if (result["additionalInfo"] != null) {
                const downloadUrl = processUrl(result["additionalInfo"]);
                const downloadFileName = getFileName(result["additionalInfo"]);
                $("#additional_info_file_download").append("<a href = \"" + downloadUrl + "\" download=\"" + downloadFileName + "\">" + downloadFileName + "</a>");
            }

            if (result["maxDistance"] != null) {
                $("#InputMaxDistance").val(result["maxDistance"]);
            } else {
                $("#InputMaxDistance").val(-1);
            }
            $("#InputPassword").val(result.password);
            if (result.priority != null) {
                $("#DisplayPriority").val(result.priority);
            }
            $("#DisplayStatus").val(result.status);
        }
    });

    $("#btn-upload").click(function (event) {
        const data = new FormData($("#upload-form")[0]);
        console.log(data);
        $.ajax({
            url: "/api/file",
            data: data,
            type: "POST",
            processData: false,
            contentType: false,
            dataType: "json",
            success: function (result) {
                if (result.url != null) {
                    const downloadUrl = processUrl(result.url);
                    const downloadFileName = getFileName(result.url);
                    $("#additional_info_file_download").empty()
                    $("#additional_info_file_download").append("<a href = \"" + downloadUrl + "\" download=\"" + downloadFileName + "\">" + downloadFileName + "</a>");
                }
            }
        });
        event.preventDefault();
    });

    $("#log-out").click(function (event) {
        window.location.href = "http://localhost:8080/view/index.html"
    })

    $("#profile").click(function (event) {
        $(".profile").show();
        $(".appointment").hide();
        $(".scheduler").hide();
    });

    $("#scheduler").click(function (event) {
        $(".profile").hide();
        $(".appointment").hide();
        $(".scheduler").show();
        $.ajax({
            url: "/api/patients/schedule/" + id,
            method: "GET",
            success: function (result) {
                console.log(result);
                for (let i = 0; i < result.length; i++) {
                    cellId = weekDayArr[result[i]["dayOfTheWeek"]] + "-" + result[i]["startTime"];
                    $("#" + cellId).text("available");
                }
            }
        });
    });

    let actionType = "";
    $("#appointment").click(function (event) {
        $(".profile").hide();
        $(".appointment").show();
        $(".scheduler").hide();
        if ($("#table-body-history-appointment").length < 1) {
            $("#no-appointment-history-message").show();
        } else {
            $("#no-appointment-history-message").hide();
            refreshHistoryAppointment();
        }
        refreshCurrentAppointment();
    });

    let isAddressChanged = false;
    $("#address").change(function () {
        isAddressChanged = true;
    });

    function updateDistance() {
        $.ajax({
            url: "/api/distance/patients/address/update",
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            data: JSON.stringify({"id": id}),
            success: function (result) {
                console.log(result);
            }
        });
    }

    $("#btn-update").click(function (event) {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "PUT",
            url: "/api/patients/" + id,
            data: JSON.stringify({
                name: $('#InputName').val(),
                email: $('#InputEmail').val(),
                ssn: $('#InputSSN').val(),
                birth: $('#InputBirth').val(),
                address: $('#InputAddress').val(),
                phone: $('#InputPhone').val(),
                additional_info: $('#InputAdditionalInfo').val(),
                max_distance: $('#InputMaxDistance').val(),
                password: $('#InputPassword').val()
            }),
            success: function (result) {
                if (isAddressChanged) updateDistance();
                alert("Updating profile successfully!");
            }
        });
        event.preventDefault();
    });

    $("#select-week-day").change(function () {
        isWeekDaySelected = true;
        weekDay = $("#select-week-day").val();
        if (isTimeSlotSelected && isWeekDaySelected) {
            $("#btn-confirm-schedule").attr("disabled", false);
            $("#btn-cancel-schedule").attr("disabled", false);
        }
    });

    $("#select-time-slot").change(function () {
        isTimeSlotSelected = true;
        timeSlot = $("#select-time-slot").val();
        if (isTimeSlotSelected && isWeekDaySelected) {
            $("#btn-confirm-schedule").attr("disabled", false);
            $("#btn-cancel-schedule").attr("disabled", false);
        }
    });

    $("#btn-confirm-schedule").click(function (event) {
        cellId = weekDayArr[weekDay] + "-" + timeSlot;
        if ($("#" + cellId).text() != "available") {
            $("#" + cellId).text("available");
            $.ajax({
                url: "/api/patients/schedule/add",
                method: "POST",
                headers: {"Content-Type": "application/json"},
                data: JSON.stringify({"patientId": id, "startTime": timeSlot, "dayOfTheWeek": weekDay}),
                success: function (result) {
                    alert("new schedule confirmed");
                }
            });
        } else {
            alert("You have already select that time slot!");
        }

        event.preventDefault();
    });

    $("#btn-cancel-schedule").click(function (event) {
        cellId = weekDayArr[weekDay] + "-" + timeSlot;
        if ($("#" + cellId).text() == "available") {
            $("#" + cellId).text("");
            $.ajax({
                url: "/api/patients/schedule/delete",
                method: "POST",
                headers: {"Content-Type": "application/json"},
                data: JSON.stringify({"patientId": id, "startTime": timeSlot, "dayOfTheWeek": weekDay}),
                success: function () {
                    alert("the selected time slot has be cancelled successfully");
                }
            });
        } else {
            alert("The time slot you select is not available!");
        }
        event.preventDefault();
    });

    $("#current-appointment-table").on("click", "#btn-action-cancel", function (event) {
        $.ajax({
            url: "/api/patients/appointment/current/cancel",
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            data: JSON.stringify({"id": $(this).val()}),
            success: function () {
                refreshCurrentAppointment();
            }
        });
        $.ajax({
            url: "/api/patients/appointment/log",
            method: "POST",
            headers: {"Content-Type": "application/json"},
            data: JSON.stringify({"id": $(this).val(), "action": "cancelled", "patient_id": id}),
            success: function (result) {
                refreshHistoryAppointment()
            }
        });
        event.preventDefault();
    });

    $("#current-appointment-table").on("click", "#btn-action-accept", function (event) {
        $.ajax({
            url: "/api/patients/appointment/current/accept",
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            data: JSON.stringify({"id": $(this).val(), "patient_id": id}),
            success: function () {
                refreshCurrentAppointment();
                // sent notification to patient
                let settings = {
                    "url": "/notification/patient/confirmation",
                    "method": "POST",
                    "timeout": 0,
                    "headers": {
                        "Content-Type": "application/json"
                    },
                    "data": JSON.stringify({
                        "id": id,
                        "patient_id": $(this).val()
                    }),
                };

                $.ajax(settings).done(function (response) {
                    console.log(response);
                });
            }
        });
        $.ajax({
            url: "/api/patients/appointment/log",
            method: "POST",
            headers: {"Content-Type": "application/json"},
            data: JSON.stringify({"id": $(this).val(), "action": "accepted", "patient_id": id}),
            success: function (result) {
                refreshHistoryAppointment();
            }
        });
        event.preventDefault();
    });

    $("#current-appointment-table").on("click", "#btn-action-decline", function (event) {
        $.ajax({
            url: "/api/patients/appointment/current/decline",
            method: "PATCH",
            headers: {"Content-Type": "application/json"},
            data: JSON.stringify({"id": $(this).val()}),
            success: function () {
                refreshCurrentAppointment();
            }
        });
        $.ajax({
            url: "/api/patients/appointment/log",
            method: "POST",
            headers: {"Content-Type": "application/json"},
            data: JSON.stringify({"id": $(this).val(), "action": "declined", "patient_id": id}),
            success: function (result) {
                refreshHistoryAppointment()
            }
        });
        event.preventDefault();
    });
});