'use strict';

//attendance controller
indexModule.controller('attendanceController', function ($scope, $http) {

    $scope.req = {};
    $scope.req.paging = {};
    $scope.req.paging.pageNo = 1;
    $scope.req.paging.pageSize = 500;
    $scope.totalRecordCount = 0;
    $scope.totalPage = 0;
    $scope.currentPage = 1;
    $scope.selected = [];
    $scope.formData = {};
    $scope.times = 0;
    $scope.currentSystemId = -1;


    /**
     * 上传
     */
    $scope.upload = function () {
        var files = document.getElementsByName('file')[1].files;
        if (files.length < 1) {
            showOkModal("提示信息", "请先选择文件！");
            return;
        }
        $http({
            method: 'POST',
            url: 'rest/attendance/upload',
            headers: {
                'Content-Type': undefined
            },
            data: {
                file: files[0]
            },
            transformRequest: function (data) {
                var formData = new FormData();
                formData.append('file', data.file);
                return formData;
            },
        }).success(function (data) {
            //请求成功
            showOkModal('提示', data.message);
        }).error(function (err, status) {
            console.log(err);
        });
    };



    $http.post("rest/attendance/page", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.attendances = data.data;
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

    //显示输入检测事项模态框
    $scope.showInputAttModal = function () {
        $("#inputAttModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //执行输入检测事项
    $scope.inputChecked = function () {
        var time = $scope.formData.time;
        var opt = $scope.formData.opt;
        if (time=='1'){
            $scope.formData.morDay = opt;
        } else if (time=='2'){
            $scope.formData.aftDay = opt;
        } else if (time=='3'){
            $scope.formData.eveDay = opt;
        } else {
            $scope.formData.allDay = opt;
        }
        $http.post("rest/attendance/input", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "添加成功！");
                    refreshListView();
                    $("#inputAttModal").modal('hide');

                } else {
                    showOkModal('提示', data.message);
                }
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //清除事项
    $scope.deleteAttInfo = function (index) {
        $http.post("rest/attendance/deleteAttInfo", $scope.attendances[index-1]).success(
            function (data) {if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "删除成功！");
                    refreshListView();
                } else {
                    showOkModal('提示', data.message);
                }
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //显示添加签到时间模态框
    $scope.showInputSignModal = function () {
        $("#inputSignModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //添加签到时间
    $scope.inputSignTime = function () {
        $http.post("rest/attendance/inputSignTime", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "添加成功！");
                    //refreshListView();
                    $("#inputAttModal").modal('hide');

                } else {
                    showOkModal('提示', data.message);
                }
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //显示添加签退时间模态框
    $scope.showInputLeaveModal = function () {
        $("#inputLeaveModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //添加签退时间
    $scope.inputLeaveTime = function () {
        $http.post("rest/attendance/inputLeaveTime", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "添加成功！");
                    //refreshListView();
                    $("#inputAttModal").modal('hide');

                } else {
                    showOkModal('提示', data.message);
                }
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };


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
        $scope.getAttendanceListBySystemId($scope.currentSystemId);
    };

});
