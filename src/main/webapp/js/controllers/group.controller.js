'use strict';

//组管理
indexModule.controller("groupController", ['$scope', '$http', 'NgTableParams', 'staffService',function ($scope, $http, NgTableParams, staffService) {

    var self = this;

    $scope.groups = {};

    //获取组列表
    self.tableParams = new NgTableParams({'count': 100}, {
        getData: function (params) {
            //清空请求
            var allParams = params.url();

            var promise = $http.get("rest/group/list");
            return promise.then(function (resut) {
                var response = resut.data;
                console.log("response" ,response);
                $scope.groups = response.data;
                return response.data;
            });
        }
    });

    //准备信息组信息
    $scope.toAddGroup = function () {
        $scope.formData = {};
        $("#groupModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //准备修改组信息
    $scope.toEditGroup = function (index) {
        $scope.formData = $scope.groups[index];
        var selectStaffList = $scope.formData.staffList;
        var selectStaffIdArr = [];
        if (selectStaffList !== null && selectStaffList.length > 0) {
            angular.forEach(selectStaffList, function (staff) {
                selectStaffIdArr.push(staff.staffId);
            });
        }
        angular.forEach($scope.staffs, function (staff) {
            staff.ticked = selectStaffIdArr.indexOf(staff.staffId) > -1;
        });
        $("#groupModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //多选框默认文本替换
    $scope.localLang = {
        nothingSelected : "请选择组的成员",
        selectAll : "选择全部",
        selectNone : "取消全选"
    };

    //获取所有在职员工
    $scope.getAllStaff = function () {
        var promise = staffService.queryAllStaff("onJob");
        return promise.then(function (result) {
            $scope.staffs = result;
            return result;
        });
    };
    $scope.getAllStaff();

    //新增或修改组
    $scope.submitGroup = function () {
        var staffIdArr = [];
        angular.forEach($scope.selectStaffs, function (staff) {
            staffIdArr.push(staff.staffId);
        });
        $scope.formData.staffIdStr = staffIdArr.join(",");

        if ($scope.formData.groupId) {
            //修改组
            console.log("$scope.formData", $scope.formData);
            $http.put("rest/group/update", $scope.formData).success(function (data) {
                if (data !== null && data !== undefined) {
                    showOkModal("提示", data.message);
                    if (data.resultCode === "RESULT_SUCCESS") {
                        self.tableParams.reload();
                        $("#groupModal").modal('hide');
                    }
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        } else {
            //是新增
            console.log("新增", $scope.formData);
            $http.post("rest/group/add", $scope.formData).success(function (data) {
                if (data !== null && data !== undefined) {
                    showOkModal("提示", data.message);
                    if (data.resultCode === "RESULT_SUCCESS") {
                        self.tableParams.reload();
                        $("#groupModal").modal('hide');
                    }
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        }

    };

    //删除组
    $scope.deleteGroup = function (groupId) {
        showConfirmModal("确定要删除吗？", function () {
            $http.delete("rest/group/" + groupId + "/delete").success(function (response) {
                    if (response.resultCode === "RESULT_SUCCESS") {
                        self.tableParams.reload();
                        $("#confirmModal").modal('hide');
                    } else {
                        showOkModal("提示", response.message);
                    }
                }
            ).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        });
    };


}]);
