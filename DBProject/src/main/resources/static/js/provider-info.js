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

        function refreshUploadedAppointment() {
            $("#table-body-uploaded-appointment").empty();
            $.ajax({
                url: "/api/providers/appointment/" + id,
                method: "GET",
                success: function (result) {
                    for (let item of result) {
                        $("#table-body-uploaded-appointment").append("<tr style='alignment: center; text-align: center'> " +
                            "<td>" + item["id"] + "</td>" +
                            "<td>" + item["provider"] + "</td>" +
                            "<td>" + item["start_time"] + "</td>" +
                            "<td>" + item["end_time"] + "</td>" +
                            "<td>" + item["patient"] + "</td>" +
                            "<td>" + item["status"] + "</td>" +
                            " </tr>");
                    }
                }
            });
        }

        function getActionBtn(id, status) {
            let returnStr;
            if (status == "absent") {
                returnStr = "<div class=\"form-check form-check-inline\">\n" +
                    "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio1\" name='" + id + "' value='absent' checked>\n" +
                    "  <label class=\"form-check-label\" for=\"inlineRadio1\">absent</label>\n" +
                    "</div>\n" +
                    "<div class=\"form-check form-check-inline\">\n" +
                    "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio2\" name='" + id + "' value='completed'>\n" +
                    "  <label class=\"form-check-label\" for=\"inlineRadio2\">completed</label>\n" +
                    "</div>"
            } else if (status == "completed") {
                returnStr = "<div class=\"form-check form-check-inline\">\n" +
                    "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio1\" name='" + id + "' value='absent'>\n" +
                    "  <label class=\"form-check-label\" for=\"inlineRadio1\">absent</label>\n" +
                    "</div>\n" +
                    "<div class=\"form-check form-check-inline\">\n" +
                    "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio2\" name='" + id + "' value='completed' checked>\n" +
                    "  <label class=\"form-check-label\" for=\"inlineRadio2\">completed</label>\n" +
                    "</div>"
            } else {
                returnStr = "<div class=\"form-check form-check-inline\">\n" +
                    "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio1\" name='" + id + "' value='absent'>\n" +
                    "  <label class=\"form-check-label\" for=\"inlineRadio1\">absent</label>\n" +
                    "</div>\n" +
                    "<div class=\"form-check form-check-inline\">\n" +
                    "  <input class=\"form-check-input\" type=\"radio\" id=\"inlineRadio2\" name='" + id + "' value='completed'>\n" +
                    "  <label class=\"form-check-label\" for=\"inlineRadio2\">completed</label>\n" +
                    "</div>"
            }
            return returnStr;
        }

        function refreshExpiredAppointment() {
            $("#table-body-expired-appointment").empty();
            $.ajax({
                url: "/api/providers/appointment/expired/" + id,
                method: "GET",
                success: function (result) {
                    for (let item of result) {
                        $("#table-body-expired-appointment").append("<tr style='alignment: center; text-align: center'> " +
                            "<td>" + item["id"] + "</td>" +
                            "<td>" + item["provider"] + "</td>" +
                            "<td>" + item["start_time"] + "</td>" +
                            "<td>" + item["end_time"] + "</td>" +
                            "<td>" + item["patient"] + "</td>" +
                            "<td>" + item["status"] + "</td>" +
                            "<td>" + getActionBtn(item["id"], item["status"]) + "</td>" +
                            " </tr>");
                    }
                }
            });
        }

        $("#table-body-expired-appointment").on("change", ":radio", function (event) {
            // alert($(this).attr("name")+"    "+$(this).val());
            let date = JSON.stringify({"id": $(this).attr("name"), "status": $(this).val()})
            $.ajax({
                url: "/api/providers/appointment",
                method: "PATCH",
                headers: {"Content-Type": "application/json"},
                data: date,
                success: function (result) {
                    alert("success set the appointment status");
                }
            });
            event.preventDefault();
        });

        // get provider id from url and fill the corresponding field
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
            url: "/api/providers/" + id,
            success: function (result) {
                $(".logo").text("Name:" + result.name + " | Phone:" + result.phone + " (provider)");
                $("#InputName").val(result.name);
                $("#InputAddress").val(result["address"]);
                $("#InputPhone").val(result.phone);
                $("#InputPassword").val(result.password);
            }
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

        $("#appointment").click(function (event) {
            $(".profile").hide();
            $(".appointment").show();
            $(".scheduler").hide();
            if ($("#table-body-history-appointment").length < 1) {
                $("#no-appointment-history-message").show();
            } else {
                $("#no-appointment-history-message").hide();
                refreshUploadedAppointment();
            }
            refreshUploadedAppointment();
            refreshExpiredAppointment();
        });

        function updateDistance() {
            $.ajax({
                url: "/api/distance/providers/address/update",
                method: "PATCH",
                headers: {"Content-Type": "application/json"},
                data: JSON.stringify({"id": id}),
                success: function (result) {
                    console.log(result);
                }
            });
        }

        let isAddressChanged = false;
        $("#InputAddress").change(function () {
            isAddressChanged = true;
            console.log(isAddressChanged);
        });

        $("#btn-update").click(function (event) {
            $.ajax({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                type: "PUT",
                url: "/api/providers/" + id,
                data: JSON.stringify({
                    name: $('#InputName').val(),
                    address: $('#InputAddress').val(),
                    phone: $('#InputPhone').val(),
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


        let startTime;
        let endTime;

        function checkTime(start, end) {
            start = start.split(":");
            end = end.split(":");
            return (parseInt(start[0]) * 60 + parseInt(start[1])) < (parseInt(end[0]) * 60 + parseInt(end[1]));
        }

        function appointmentUploadReady() {
            if ($("#start").val() != "" && $("#end").val() != "" && $("#date").val() != "") {
                $("#btn-confirm-appointment").attr("disabled", false);
            } else {
                $("#btn-confirm-appointment").attr("disabled", true);
            }
        }


        $("#start").change(function () {
            console.log("start: " + $("#start").val());
            startTime = Date.parse($("start").val());
            appointmentUploadReady();
        });

        $("#end").change(function () {
            console.log(checkTime($("#start").val(), $("#end").val()));
            if (startTime == null) {
                alert("Please select start time first.");
                $("#end").val("");
            } else if (!checkTime($("#start").val(), $("#end").val())) {
                alert("End time must be later than start time.")
                $("#end").val("");
            } else {
                console.log("end: " + $("#end").val());
                endTime = Date.parse($("#end").val());
            }
            appointmentUploadReady()
        });

        $("#date").change(function () {
            appointmentUploadReady();
        });

        function emptyAppointmentField() {
            $("#date").val("");
            $("#start").val("");
            $("#end").val("");
        }

        $("#btn-confirm-appointment").click(function (event) {
            let data = JSON.stringify({
                id: id,
                date: $("#date").val(),
                startTime: $("#start").val(),
                endTime: $("#end").val()
            });
            $.ajax({
                url: "/api/providers/appointment",
                method: "POST",
                headers: {"Content-Type": "application/json"},
                data: data,
                success: function (result) {
                    alert("New appointment has been uploaded successfully!");
                    refreshUploadedAppointment();
                }
            });
            emptyAppointmentField();
            appointmentUploadReady();
            event.preventDefault();
        });

        $("#non-expired-appointment-filter").change(function (event) {
            let filterOption = $("#non-expired-appointment-filter").find("option:selected").text();
            if (filterOption == "assigned status") {
                refreshUploadedAppointmentFilterByStatus("assigned");
            } else if (filterOption == "released status") {
                refreshUploadedAppointmentFilterByStatus("released");
            } else if (filterOption == "recently released") {
                refreshUploadedAppointmentOrderByTime()
            } else {
                refreshUploadedAppointment();
            }
            event.preventDefault();
        });

        function refreshUploadedAppointmentOrderByTime(status) {
            $("#table-body-uploaded-appointment").empty();
            $.ajax({
                url: "/api/providers/appointment/recent/" + id,
                method: "GET",
                success: function (result) {
                    for (let item of result) {
                        $("#table-body-uploaded-appointment").append("<tr style='alignment: center; text-align: center'> " +
                            "<td>" + item["id"] + "</td>" +
                            "<td>" + item["provider"] + "</td>" +
                            "<td>" + item["start_time"] + "</td>" +
                            "<td>" + item["end_time"] + "</td>" +
                            "<td>" + item["patient"] + "</td>" +
                            "<td>" + item["status"] + "</td>" +
                            " </tr>");
                    }
                }
            });
        }

        function refreshUploadedAppointmentFilterByStatus(status) {
            $("#table-body-uploaded-appointment").empty();
            $.ajax({
                url: "/api/providers/appointment/" + status + "/" + id,
                method: "GET",
                success: function (result) {
                    for (let item of result) {
                        $("#table-body-uploaded-appointment").append("<tr style='alignment: center; text-align: center'> " +
                            "<td>" + item["id"] + "</td>" +
                            "<td>" + item["provider"] + "</td>" +
                            "<td>" + item["start_time"] + "</td>" +
                            "<td>" + item["end_time"] + "</td>" +
                            "<td>" + item["patient"] + "</td>" +
                            "<td>" + item["status"] + "</td>" +
                            " </tr>");
                    }
                }
            });
        }
    }
);