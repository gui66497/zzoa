/**
 * Created by Steven on 2017/5/9.
 */
'use strict';

//部门管理模块
indexModule.controller('departmentCtrl', ['$scope', '$document', '$http', 'staffService', 'NgTableParams', function ($scope, $document, $http, staffService, NgTableParams) {
    $scope.treeOptions = {
        accept: function (sourceNodeScope, destNodesScope, destIndex) {
            return true;
        }
    };
    $scope.roleList = {
        list: {},
        checked: false
    };
    $scope.init = function () {
        $scope.getTreeList();
    };

    var self = this;

    $scope.getTreeList = function () {
        $scope.angularTreeList = [];
        //查询树
        $http.get("rest/depart/list", null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.angularTreeList.push(data[0]);
                console.log($scope.angularTreeList);
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
        $scope.treeOptions.data = $scope.angularTreeList;
    };
    //新建节点
    $scope.newSubItem = function (item) {
        $scope.item = item;
        $scope.name="";
        $("#addDepartModal").modal({show: true, keyboard: false, backdrop: 'static'});

        ////var name=window.prompt("请输入节点的名称");

    };
    $scope.addDepartInfo = function () {

        if ($scope.name == "" || $scope.name == undefined) {
            return;
        }
        if ($scope.item.departId == undefined) {
            $scope.item.departId = -1;
        }
        var json = {
            "level": parseInt($scope.item.level) + 1,
            "departName": $scope.name,
            "parentDepart": $scope.item.departId,
            "nodes": []
        };
        if ($scope.item.nodes == null || $scope.item.nodes.length == 0) {
            $scope.item.nodes = [];
        }
        $http.post("rest/depart/add", json).success(function (data) {
            if (data && data.resultCode == "RESULT_SUCCESS") {
                $scope.init();
                $scope.item = undefined;
                $("#addDepartModal").modal('hide');
            }
        }).error(function (data) {
            $scope.init();
            $("#serverErrorModal").modal({show: true});
        });

    };

    //获取焦点用
    $scope.isFocus = false;

    //失去焦点触发
    $scope.setBlur = function (item) {
        $scope.isFocus = false;
        console.log("setBlur", item);
        //todo 失去焦点后保存正在修改的节点
    };

    //编辑节点
    $scope.editItem = function (item) {
        if (item.isEdit == undefined || item.isEdit == false) {
            var oldEditNodes = [];
            getOldEditNode($scope.angularTreeList, oldEditNodes);
            if (oldEditNodes.length > 0) {
                var depart = {"departId": oldEditNodes[0].departId, "departName": oldEditNodes[0].departName};
                $http.post("rest/depart/update", depart).success(function (data) {
                    if (data && data.resultCode == "RESULT_SUCCESS") {
                        $scope.init();
                    }
                }).error(function (data) {
                    $scope.init();
                    $("#serverErrorModal").modal({show: true});
                });
            } else {
                item.isEdit = true;
                $scope.isFocus = true;
            }
        } else if (item.isEdit == true) {
            item.isEdit = false;
            var depart = {"departId": item.departId, "departName": item.departName};
            $http.post("rest/depart/update", depart).success(function (data) {
                if (data && data.resultCode == "RESULT_SUCCESS") {
                    $scope.init();
                }
            }).error(function (data) {
                $scope.init();
                $("#serverErrorModal").modal({show: true});
            });
        }
    };

    //获取其他正在被编辑的节点(获取到一个后退出)
    function getOldEditNode (node, oldEditNodes) {
        angular.forEach(node, function (one) {
            if(oldEditNodes < 1) {
                if(one.isEdit) {
                    console.log("找到了：", one);
                    oldEditNodes.push(one);
                } else {
                    if (one.nodes && one.nodes.length > 0) {
                        getOldEditNode(one.nodes, oldEditNodes)
                    }
                }
            }
        });
    }
    
    //删除节点
    $scope.removeItem = function (item) {
        var departId = item.departId;
        $http.delete("rest/depart/" + departId + "/Del", null).success(function (data) {
            if (data && data.resultCode == "RESULT_SUCCESS") {
                $scope.init();
            } else {
                showOkModal("提示框", data.message);
                $scope.init();

            }
        }).error(function (data) {
            $scope.init();
            $("#serverErrorModal").modal({show: true});
        });
    };

    $scope.getDepartRoleList = function () {
        var promise = staffService.getAllGroup();
        return promise.then(function (result) {
            $scope.roleList.list = result || null;
            return result;
        });
    };
    $scope.isRoleChecked = function (groupId) {
        var flag;
        if($.inArray(groupId,$scope.roleIdList)>-1){
            flag =true;
        }else{
            flag=false;
        }
        return flag;
    };
    $scope.getRoleListByDepartId = function (departId) {
        var promise = staffService.getGroupsByDepartId(departId);
        return promise.then(function (result) {
            return result;
        });
    };

    $scope.getRolesByDepartId = function(departId){
        $scope.roleIdList =[];
        $http.get("rest/groupDepart/" + departId + "/getListByDepartId", null).success(function (data) {
            if (data.list.length>0) {
                angular.forEach(data.list,function(obj,index){
                    $scope.roleIdList.push(obj.groupId);
                })
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };


    //设置部门权限
    $scope.showDepartAuthorityModal = function (item) {
        $scope.getRolesByDepartId(item.departId);
        $scope.departNode = item || null;
        $scope.getDepartRoleList();
        $("#departAuthorityModal").modal({
            show: true
        })
    };

    //提交部门角色信息
    $scope.addDepartAuthority = function () {
        var json = {
            departId: $scope.departNode.departId,
            groupList: []
        };
        var roleCheckBoxes = angular.element(document.querySelectorAll(".depart_role_check"));
        angular.forEach(roleCheckBoxes, function (obj, index) {
            if (obj.checked) {
                json.groupList.push(obj.value);
            }
        });
        staffService.addAuthority(json).then(function (data) {
            if (data && data.resultCode == "RESULT_SUCCESS") {
                $("#departAuthorityModal").modal("hide");
                showOkModal("提示框", data.message);
            } else {
                showOkModal("提示框", data.message);
            }
        })

    };

    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            console.log("req的值为：", $scope.req);
            var promise = $http.get("rest/staff/getByDepart/" + $scope.clickDepartId);
            return promise.then(function (resut) {
                var response = resut.data;
                $scope.tableData = response.data;
                console.log("params值：", params);
                params.settings().counts = [];
                console.log("params值2：", params);
                return response.data;
            });
        }
    });

    //部门节点点击事件
    $scope.departNodeClick = function (item) {
        if (item.isEdit)
            return;
        console.log("点击了：", item);
        $scope.clickDepartId = item.departId;
        var departId = item.departId;

        self.tableParams.reload();

        $http.get("rest/staff/getByDepart/" + departId).success(function (data) {
            if (data && data.resultCode == "RESULT_SUCCESS") {
                $scope.staffs = data.data;
                console.log("其下员工：", $scope.staffs);
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    $scope.isClick = function (item) {
        if (item.departId == $scope.clickDepartId) {
            return {"background" : "#b3c6de"}
        }
        return "";
    };

}]);