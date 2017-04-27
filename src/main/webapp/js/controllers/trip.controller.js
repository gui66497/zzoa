'use strict';

//出差管理模块
indexModule.controller('tripController', function ($scope, $http, $timeout) {

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

    $http.post("rest/trip/page", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.trips = data.data;
            $scope.otherData = data.otherData;
            $scope.totalRecordCount = $scope.otherData[0];
            $scope.totalPage = $scope.otherData[1];

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

    //设置分页组件按钮的状态
    $scope.setPageButtonStatus = function() {
        if($scope.totalPage > $scope.req.paging.pageNo) {
            $("#nextPageLink").removeClass("disabled");
            $("#nextPage").attr("unable", false);
        } else {
            $("#nextPageLink").addClass("disabled");
            $("#nextPage").attr("unable", true);
        }
        if($scope.req.paging.pageNo == 1) {
            $("#previousPageLink").addClass("disabled");
            $("#previousPage").attr("unable", true);
        } else {
            $("#previousPageLink").removeClass("disabled");
            $("#previousPage").attr("unable", false);
        }
    };

    //上一页
    $scope.previousPage = function () {
        if($("#previousPage").attr('unable') == "false") {
            $scope.req.paging.pageNo = $scope.req.paging.pageNo - 1;
            refreshListView();
        }
    };

    //下一页
    $scope.nextPage = function () {
        if ($("#nextPage").attr('unable') == "false") {
            $scope.req.paging.pageNo = $scope.req.paging.pageNo + 1;
            refreshListView();
        }
    };


    //上一页
    $scope.authorityPreviousPage = function () {
        if($("#previousPage1").attr('unable') == "false") {
            $scope.authorityReq.paging.pageNo = $scope.authorityReq.paging.pageNo - 1;
            $scope.getAuthorityListByRoleId($scope.authorityReq.roleId);
        }
    };

    //下一页
    $scope.authorityNextPage = function () {
        if ($("#nextPage1").attr('unable') == "false") {
            $scope.authorityReq.paging.pageNo = $scope.authorityReq.paging.pageNo + 1;
            $scope.getAuthorityListByRoleId($scope.authorityReq.roleId);
        }
    };

    //设置分页组件按钮的状态
    $scope.setPageButtonStatus1 = function() {
        if($scope.authroityTotalPage > $scope.authorityReq.paging.pageNo) {
            $("#nextPageLink1").removeClass("disabled");
            $("#nextPage1").attr("unable", false);
        } else {
            $("#nextPageLink1").addClass("disabled");
            $("#nextPage1").attr("unable", true);
        }
        if($scope.authorityReq.paging.pageNo == 1) {
            $("#previousPageLink1").addClass("disabled");
            $("#previousPage1").attr("unable", true);
        } else {
            $("#previousPageLink1").removeClass("disabled");
            $("#previousPage1").attr("unable", false);
        }
    };


    //刷新列表页面
    var refreshListView = function () {
        $scope.getTripListBySystemId($scope.currentSystemId);
    };

});

//出差管理controller
