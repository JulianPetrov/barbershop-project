$(document).ready(function () {

/*    $("#createAppointmentDate").change(function () {
        let date = $(this).val();
        let s = '<option value=' + '00:00' + '>Select from available times</option>';
        if (date !== null) {
            $.ajax({
                url: '/salon/available-times',
                data: {"date": date},
                success: function (result) {
                    result = JSON.parse(result);
                    for (let i = 0; i < result.length; i++) {
                        s += '<option value="' + result[i] + '">' + result[i] + '</option>';
                    }
                    $('#createAppointmentTime').html(s);
                }
            });
        }
    });*/


    $("#createAppointmentForm").find("#createAppointmentDate, #createAppointmentEmployee, #createAppointmentServicesList")
        .change(function (event) {
        event.preventDefault();
        ajaxGetAvailableTimes();
    });


    function ajaxGetAvailableTimes(){
        let queryParams = getQueryParamsFromCreateAppointmentForm();
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: '/salon/available-times/?' + queryParams,
            dataType: 'json',
            delay: 250,
            success: function (result) {
                let s = '<option value="-1">Select from available times</option>';
                for (let i = 0; i < result.length; i++) {
                    s += '<option value="' + result[i] + '">' + result[i] + '</option>';
                }
                $('#createAppointmentTime').html(s);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function getQueryParamsFromCreateAppointmentForm() {
        let formData = [{
            "date": $('#createAppointmentDate').val(),
            "employeeId": $('#createAppointmentEmployee').val(),
            "serviceId": $('#createAppointmentServicesList').val()
        }];

        let filledProps = formData.map(el => {
            return Object.keys(el).reduce((newObj, key) => {
                const value = el[key];
                if (value !== null && value !== '' && value !== 'All' && value !== "-1") {
                    newObj[key] = value;
                }
                return newObj;
            }, {});
        });
        filledProps = filledProps[0];
        return makeParams(filledProps);
    }

    function makeParams(params) {
        return params ? '&' + Object.keys(params).map((key) => ([key, params[key]].map(encodeURIComponent).join("="))).join("&") : ''
    }


    $("#city").select2({
        theme: "classic",
        ajax: {
            url: '/salon/cities',
            dataType: 'json',
            delay: 250,
            processResults: function (response) {
                return {
                    results: response
                };
            },
            cache: true
        }
    });

    $('.multiple-select-dropdown').select2();

    $(".workday-placeholder").select2({
        placeholder: "Select workdays"
    });
});