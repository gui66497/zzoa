'use strict';

//假期管理模块
indexModule.controller('vacationController', function ($scope, $http, $timeout) {

    $scope.req = {};
    $scope.req.paging = {};
    $scope.req.paging.pageNo = 1;
    $scope.req.paging.pageSize = 5;
    $scope.totalRecordCount = 0;
    $scope.totalPage = 0;
    $scope.currentPage = 1;
    $scope.selected = [];
    $scope.formData = {};
    $scope.times = 0;
    $scope.currentSystemId = -1;
    $scope.authReq = {};

    $http.post("rest/vacation/page", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.vacations = data.data;
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

    //新增请假信息，完成初始化信息
    $scope.add = function () {
        $http.get("rest/vacation/getId", {}).success(function (data) {
                $scope.formData.id=data;
                $("#AddAndUpdateModal").modal({show: true, keyboard: false, backdrop: 'static'});
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });

    }

    //修改请假信息
    $scope.update = function () {
        var id = 12;
        $http.get("rest/vacation/"+id+"/load", id).success(function (data) {
            if (data != null && data != undefined) {
                $scope.formData = data;
                $("#AddAndUpdateModal").modal({show: true, keyboard: false, backdrop: 'static'});
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }

    //审批请假信息
    $scope.approve = function () {
        var id = 12;
        $http.get("rest/vacation/"+id+"/load", id).success(function (data) {
            if (data != null && data != undefined) {
                $scope.formData = data;
                $("#ApproveModal").modal({show: true, keyboard: false, backdrop: 'static'});
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }

    //保存
    $scope.save = function () {
        $http.post("rest/vacation/save", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "保存成功！");
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
    }

    //保存并提交
    $scope.saveAndSubmit = function () {
        $http.post("rest/vacation/saveAndSubmit", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "提交成功！");
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
    }

    //审批保存
    $scope.approveSave = function () {
        $http.post("rest/vacation/approveSave", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "保存成功！");
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
    }

    //审批
    $scope.approve = function () {
        $http.post("rest/vacation/approve", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "提交成功！");
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
    }

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
        $scope.getVacationListBySystemId($scope.currentSystemId);
    };

});

//假期管理controller
