'use strict';

//table controller
indexModule.controller('tableController', function ($scope, $http, NgTableParams) {

    var self = this;
    var data = [{name: "Moroni1", age: 1}, {name: "gu", age: 2},
        {name: "Moroni2", age: 3}, {name: "gu2", age: 4},
        {name: "Moroni1", age: 5}, {name: "gu4", age: 6},
        {name: "Moroni3", age: 7}, {name: "gu5", age: 8},
        {name: "Moroni4", age: 9}, {name: "gu6", age: 10}];
    //self.tableParams = new NgTableParams({}, {dataset: data});

    self.tableParams = new NgTableParams({'count': 5}, {dataset: data});

});

//table controller
indexModule.controller('cccController', function ($scope, $http, NgTableParams) {

    // function to submit the form after all validation has occurred
    $scope.submitForm = function(isValid) {

        // check to make sure the form is completely valid
        if (isValid) {
            alert('our form is amazing');
        }

    };

});