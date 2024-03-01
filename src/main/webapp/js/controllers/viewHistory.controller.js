'use strict';

//attendance controller
indexModule.controller('viewHistoryController', ['$scope', '$filter', '$http', function ($scope,$filter, $http) {
    var actId=window.location.search.split("=")[1];
    $http.post("rest/common/"+actId+"/getHisInfo", {}).success(function (data) {
        if (data != null && data != undefined) {
            if (data.resultCode == "RESULT_SUCCESS") {
                $scope.viewHistory = data.data;
            }
        }
    });

    $scope.formatter = function (time){
        if(time==null){
            return "";
        }else {
            return DateToString3(new Date(time));
        }
    }

}]);
