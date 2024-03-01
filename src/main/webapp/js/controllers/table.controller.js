'use strict';

//table controller
indexModule.controller('tableController',['$scope', '$http', 'NgTableParams', function ($scope, $http, NgTableParams) {

    var self = this;

    $scope.form = {};

    $scope.submitA = function (form) {
        console.log(form);
    };
}]);
