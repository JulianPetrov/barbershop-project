$(document).ready(function () {

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