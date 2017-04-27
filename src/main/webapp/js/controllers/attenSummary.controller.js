'use strict';

//出差管理模块
indexModule.controller('attenSummaryController', function ($scope, $http, $timeout) {

    $scope.req = {};
    $scope.req.paging = {};
    $scope.req.paging.pageNo = 1;
    $scope.req.paging.pageSize = 5;
    $scope.totalRecordCount = 0;
    $scope.totalPage = 0;
    $scope.currentPage = 1;
    $scope.selected = [];
    $scope.times = 0;
    $scope.currentSystemId = -1;

    $http.post("rest/attenSummary/2016-01/list", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.summarys = data.data;

            //第一次进来默认选中第一个系统
            if ($scope.times == 0) {
                $("#systemUl").find("li:eq(0)").addClass("liSelected");
            }
            $scope.times += 1;
        }
        $scope.setPageButtonStatus();
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });
});
