'use strict';

//出差管理模块
indexModule.controller('calendarController', function ($scope, $http, $timeout) {

    $scope.req = {};
    $scope.req.paging = {};
    $scope.req.paging.pageNo = 1;
    $scope.req.paging.pageSize = 5;
    $scope.totalRecordCount = 0;
    $scope.totalPage = 0;
    $scope.currentPage = 1;
    $scope.selected = [];
    $scope.userInfo = {};
    $scope.times = 0;
    $scope.currentSystemId = -1;
    $scope.authReq = {};

    $http.post("rest/calendar/2017-04/list", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.calendars = data.data;

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
    
    $scope.setHoliday = function (index) {
        $http.put("rest/calendar/"+$scope.calendars[index].curDay+"/toHoliday", null).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "设置成功！");
                } else {
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }
});
