'use strict';

/*
var module = angular.module('timestamp-marker-example', []);
module.factory('timestampMarker', [function() {
    var timestampMarker = {
        request: function(config) {
            console.log("########### REQUEST");
            config.requestTimestamp = new Date().getTime();
            return config;
        },
        response: function(response) {
            console.log("########### RESPONSE");
            response.config.responseTimestamp = new Date().getTime();
            return response;
        }
    };
    return timestampMarker;
}]);
module.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('timestampMarker');
}]);

module.controller('ExampleController', ['$scope', '$http', function($scope, $http) {
    $scope.requestTime = '[waiting]';
    $http.get('https://api.github.com/users/naorye/repos').then(function(response) {
        var time = response.config.responseTimestamp - response.config.requestTimestamp;
        $scope.requestTime = (time / 1000);
    });
}]);*/
