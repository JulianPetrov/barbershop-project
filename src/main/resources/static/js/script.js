$(document).ready(function () {

    $("#carBrands").change(function () {
        var brandName = $(this).val();
        var s = '<option value=' + -1 + '>Select Car Model</option>';
        if (brandName !== null) {
            $.ajax({
                url: '/car-listing/car-models',
                data: {"brandName": brandName},
                success: function (result) {
                    var result = JSON.parse(result);
                    for (var i = 0; i < result.length; i++) {
                        s += '<option value="' + result[i] + '">' + result[i] + '</option>';
                    }
                    $('#carModels').html(s);
                }
            });
        }
    });

    $("#city").select2({
        theme: "classic",
        ajax: {
            url: '/car-listing/cities',
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

    $("#searchForm").submit(function (event) {
        event.preventDefault();
        ajaxSearch();
    });

    $("#searchForm").find("#carBrands, #carModels, #modification, #carTypes" +
        ", #fuelTypes, #transmissionTypes, #colours, #bhpMin, #bhpMax, #priceMin" +
        ", #priceMax, #productionYearMin, #mileageMax, #euroCategory").change(function (event) {
        event.preventDefault();
        ajaxSearchCount();
    });

    function ajaxSearchCount(){
        var queryParams = getQueryParamsFromSearchForm();
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: '/car-listing/count/?page=0&size=1000' + queryParams,
            dataType: 'json',
            delay: 250,
            success: function (result) {
                result = JSON.parse(result);
                $('#searchSubmit').html('Search (' + result + ' results)');
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function ajaxSearch() {
        var queryParams = getQueryParamsFromSearchForm();
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: '/car-listing/?page=0&size=1000' + queryParams,
            dataType: 'json',
            delay: 250,
            success: function (result) {
                window.location = '/car-listing/?page=0&size=1000' + queryParams;
            },
            error: function (e) {
                window.location = '/car-listing/?page=0&size=1000' + queryParams;
            }
        });

    }

    function getQueryParamsFromSearchForm() {
        var formData = [{
            "carBrand": $('#carBrands').find(":selected").val(),
            "carModel": $('#carModels').find(":selected").val(),
            "modification": $('#modification').val(),
            "carType": $('#carTypes').find(":selected").val(),
            "fuelType": $('#fuelTypes').find(":selected").val(),
            "transmissionType": $('#transmissionTypes').find(":selected").val(),
            "colour": $('#colours').find(":selected").val(),
            "bhpMin": $('#bhpMin').val(),
            "bhpMax": $('#bhpMax').val(),
            "priceMin": $('#priceMin').val(),
            "priceMax": $('#priceMax').val(),
            "productionYearMin": $('#productionYearMin').val(),
            "mileageMax": $('#mileageMax').val(),
            "euroCategory": $('#euroCategory').find(":selected").val()
        }];

        var filledProps = formData.map(el => {
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
});