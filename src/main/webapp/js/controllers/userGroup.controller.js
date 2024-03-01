'use strict';

//用户组管理
indexModule.controller("userGroupController", ['$scope', '$http', 'NgTableParams', 'staffService',function ($scope, $http, NgTableParams, staffService) {
    $scope.groupForm = {};

    $scope.groupList = [];

    var INNER_GROUPS = ["admin", "user", "hr", "firstLeader", "secondLeader", "thirdLeader"];

    var self = this;

    //获取组列表
    self.tableParams = new NgTableParams({'count': 100}, {
        getData: function (params) {
            //清空请求
            var allParams = params.url();

            var promise = staffService.getAllGroup();
            return promise.then(function (result) {
                $scope.groupList = result;
                console.log($scope.groupList)
                return result;
            });
        }
    });

    $scope.toAddGroup = function () {
        $scope.groupForm = {};
        $("#userGroupModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    $scope.toEditGroup = function (index,groupId) {
        if($scope.groupList.length > 0) {
            var editGroup = $scope.groupList[index];
            $scope.groupForm.groupId = editGroup.id;
            $scope.groupForm.groupName = editGroup.name;
            $scope.groupForm.groupType = editGroup.type;
        }
        $("#userGroupModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    /**
     * 保存组信息
     */
    $scope.saveGroup = function () {
        if ($scope.groupForm.groupId == null || $scope.groupForm.groupName == null || $scope.groupForm.groupType == null) {
            showOkModal("提示", "请完善组信息");
            return;
        }
        $http.post("rest/identity/group/save", $scope.groupForm).success(function (data) {
            if (data != null && data != undefined) {
                // console.log(data);
                // showOkModal('提示', data.message);
                // if (data.resultCode = "RESULT_SUCCESS") {
                    self.tableParams.reload();
                    $("#userGroupModal").modal('hide');
                // }
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };
    /**
     * 删除组
     * @param groupId     */
    $scope.deleteGroup = function (groupId) {
        showConfirmModal("确定要删除角色[" + groupId + "]吗？", function () {
            $http.delete("rest/identity/group/delete/" + groupId).success(function (data) {
                if (data != null && data != undefined) {
                    self.tableParams.reload();
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        });
        return false;
    };

    $scope.isDisable = function (row) {
        var type = row.id;
        return INNER_GROUPS.indexOf(type) > -1;
    };

}]);
