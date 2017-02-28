'use strict';

//用户管理模块
indexModule.controller('userController', function ($scope, $http, $timeout) {

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

    $http.post("rest/user/page", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            console.log(data.data);
            $scope.users = data.data;
            $scope.otherData = data.otherData;
            $scope.totalRecordCount = $scope.otherData[0];
            $scope.totalPage = $scope.otherData[1];

            //第一次进来默认选中第一个系统
            if ($scope.times == 0) {
                $("#systemUl").find("li:eq(0)").addClass("liSelected");
            }
            $scope.times +=1;
        }
        $scope.setPageButtonStatus();
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    //根据systemId获取角色信息列表
    $scope.getUserListBySystemId = function() {
        $http.post("rest/user/page", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                console.log(data.data);
                $scope.users = data.data;
                $scope.otherData = data.otherData;
                $scope.totalRecordCount = $scope.otherData[0];
                $scope.totalPage = $scope.otherData[1];

                //第一次进来默认选中第一个系统
                if ($scope.times == 0) {
                    $("#systemUl").find("li:eq(0)").addClass("liSelected");
                }
                $scope.times +=1;
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //显示新增用户模态框
    $scope.showAddUserModal = function () {
        $scope.userInfo = {};
        $scope.userInfo.unitName = "";
        $("#addUserModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    $scope.showEditUser= function (index) {
        $scope.userInfo = {};
        $scope.userInfo.unitName = "";
        $("#editUserModal").modal({show: true});
        $scope.userInfo = $scope.users[index];
    };

    //执行新增用户
    $scope.addUser = function () {
        if ($scope.userInfo.password != $scope.userInfo.rePassword) {
            showOkModal('提示', "两次输入的密码不一致，请核实！");
            return;
        }
        $http.post("rest/user/add", $scope.userInfo).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "添加成功！");
                    refreshListView();
                    $("#addUserModal").modal('hide');

                } else {
                    showOkModal('提示', data.message);
                }
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //编辑用户系信息
    $scope.editUser = function () {
        $http.put("rest/user/update", $scope.userInfo).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "修改成功！");
                    refreshListView();
                    $("#editUserModal").modal('hide');
                } else {
                    showOkModal('提示', data.message);
                }
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });

    };


    //删除用户
    $scope.showDeleteUser = function (index) {
        showConfirmModal('您确定删除吗？', $scope.deleteUser);
        $scope.deleteUserId = $scope.users[index].userId;
    };

    //删除用户信息
    $scope.deleteUser = function () {
        $http.delete("rest/user/"+$scope.deleteUserId+"/delete", null).success(function (data) {
            if(data.resultCode=="RESULT_SUCCESS") {
                showOkModal('提示', "删除成功！");
                refreshListView();
            } else {
                showOkModal('提示', "删除失败,"+data.message);
            }
        }).error(function (data) {
            alert(data);
            $("#serverErrorModal").modal({show: true});
        });
    };

    //搜索
    $scope.search = function () {
        $timeout(function () {
            $scope.req.userName = $scope.searchStr;
            $scope.getUserListBySystemId();
        }, 300);
    };


    //点击checkbox触发
    $scope.updateSelection = function($event, id) {
        var checkbox = $event.target;
        var action = (checkbox.checked?'add':'remove');
        updateSelected(action,id);
    };

    //更新选中的role id
    var updateSelected = function (action, id) {
        if (action == 'add' && $scope.selected.indexOf(id) == -1) {
            $scope.selected.push(id);
        }
        if (action == 'remove' && $scope.selected.indexOf(id) != -1) {
            var idx = $scope.selected.indexOf(id);
            $scope.selected.splice(idx, 1);
        }
    };

    //判断是否选中
    $scope.isSelected = function(id) {
        return $scope.selected.indexOf(id) != -1;
    };

    //全选 取消全选
    $scope.selectAll = function ($event) {
        //获取checkbox对象
        var checkbox = $event.target;
        var ss = checkbox.checked;
        //勾选
        if (checkbox.checked) {
            for (var i in $scope.roles) {
                if ($scope.selected.indexOf($scope.roles[i].id) == -1) {
                    $scope.selected.push($scope.roles[i].id);
                }
            }
        } else {//取消勾选
            var f = $("input[name='roleCheckbox']");
            for (var j=0;j< f.length;j++) {
                if (f[j].checked) {
                    f[j].checked = false;
                }
            }
            $scope.selected = [];
        }

    };

    $scope.systemList = [{"name":"excel", "url":"excelTest.html"},{"name":"system","url":"system.html"}];


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
        $scope.getUserListBySystemId($scope.currentSystemId);
    };

    /*var mu = document.getElementById("systemUl");
     var li = mu.getElementsByTagName("li");
     mu.onclick = function (e) {
     e = e||window.event;
     var target = e.srcElement || e.target;
     for (var i= 0,len=li.length;i<len;i++) {
     li[i].className = target ==li[i] ? "liSelected":"";
     }
     };*/
});

//用户管理controller
