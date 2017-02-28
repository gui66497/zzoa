'use strict';

//权限管理模块
var authorityListModule = angular.module("authorityListModule", []);
//权限列表
var authorityListController = function($scope, $http) {
    $scope.req = {};
    $scope.roleReq = {};
    $scope.req.paging = {};
    $scope.req.paging.pageNo = 1;
    $scope.req.paging.pageSize = 15;
    $scope.totalRecordCount = 0;
    $scope.totalPage = 0;
    $scope.currentPage = 1;
    $scope.req.systemId=-1;
    $scope.times = 0;
    $scope.authorityNameArra=[];
    $scope.allDataSourceArra=[];


    $scope.req.authorityName = '查看权限';
    $http.post("rest/systemInfo/systemList", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.systems = data.data;
            $scope.req.systemId= $scope.systems[0].id;;
            $scope.getSysInfoLists($scope.req.systemId );
        }

    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    //根据系统ID获取权限信息列表
    $scope.getSysInfoLists = function(id) {
        $scope.req.paging.pageNo = 1;
        $scope.authorityNameArra=[];
        $scope.req.systemId=id;
        $scope.permissionInfo={};
        $scope.permissionInfo.systemId=id;
        //$scope.roleReq.systemId = id;
        //增删改查赋予权限
        $scope.addFlag = false;
        $scope.deleteFlag = false;
        $scope.updateFlag = false;
        //$scope.showFlag = false;

        $http.post("rest/permissionInfo/getAuthorityName", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                //判断是否为超级管理员，isAdmin为超级管理员具有所有权限，否则权限通过authorityNameArra匹配
                if(data.message != null && data.message=="isAdmin"){
                    $scope.addFlag = true;
                    $scope.deleteFlag = true;
                    $scope.updateFlag = true;
                    //$scope.showFlag = true;
                }else{
                    $scope.authorityNameArra = data.data;
                    if($scope.authorityNameArra.indexOf('添加权限') !=-1 ){
                        $scope.addFlag = true;
                    }
                    if($scope.authorityNameArra.indexOf('删除权限') !=-1 ){
                        $scope.deleteFlag = true;
                    }
                    if($scope.authorityNameArra.indexOf('更新权限') !=-1 ){
                        $scope.updateFlag = true;
                    }
                    //if($scope.authorityNameArra.indexOf('查看权限') !=-1 ){
                    //    $scope.showFlag = true;
                    //}
                }
            }
            $scope.getSysInfoList(id);
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };
    //获取权限信息列表
    $scope.getSysInfoList = function(id) {
        $scope.req.systemId= id;
        $http.post("rest/permissionInfo/list", $scope.req).success(function (data) {

            if (data != null && data != undefined) {
                $scope.authoritys = data.data;
                $scope.otherData = data.otherData;
                $scope.totalRecordCount = $scope.otherData[0];
                $scope.totalPage = $scope.otherData[1];
            }
            if ($scope.times == 0) {
                $("#systemUl").find("li:eq(0)").addClass("liSelected");
            }
            $scope.times +=1;
            //$scope.getSysInfoLists(id);
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };
    //显示新建权限窗口
    $scope.showCreateAuthorityModal = function() {

        $http.post("rest/systemInfo/systemList", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.systemList = data.data;
            }})

        $("#createAuthorityModal").modal({show: true});

        //$scope.permissionInfo=null;
        $scope.permissionInfo.name="";
        $scope.permissionInfo.sign="";
        $scope.permissionInfo.remark="";
        //$scope.permissionInfo.systemId=null;
        $scope.permissionInfo.relation_flag=1;
        $scope.permissionInfo.systemId = $scope.req.systemId;//绑定选中的systemId
    }

    //新增权限
    $scope.createAuthorityInfo = function() {
        $scope.permissionInfo.remark="";
        if( $scope.permissionInfo.relation_flag==undefined){
            $scope.permissionInfo.relation_flag=1;
        }
        $http.post("rest/permissionInfo/insert", $scope.permissionInfo).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "新增成功！");
                    $scope.getSysInfoList($scope.req.systemId );
                    $("#createAuthorityModal").modal('hide');
                } else {
                    showOkModal("提示信息","权限已存在！");
                    return;
                    //$("#createAuthorityModal").modal({show: true});
                }
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }

    //显示权限信息编辑窗口
    $scope.showEditAuthorityModal = function(index) {
        $scope.permissionInfo = null;
        $http.post("rest/systemInfo/systemList", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.systems = data.data;
            }})
        $("#editAuthorityModal").modal({show: true});
        $scope.permissionInfo = $scope.authoritys[index];
        //$scope.systems = $scope.systems[index];
    }

    //编辑权限信息
    $scope.editAuthorityInfo = function() {
        $scope.permissionInfo.remark="";
        $http.put("rest/permissionInfo/update", $scope.permissionInfo).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "编辑成功！");
                    $scope.getSysInfoList($scope.req.systemId );
                    $("#editAuthorityModal").modal('hide');
                } else {
                    showOkModal("提示信息","权限已存在！");
                }
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }
    //显示删除确认模态框
    $scope.showDelConfirmModal = function(index) {
        showConfirmModal('您确定删除吗？', $scope.deleteAuthority);
        $scope.deleteSystemId = $scope.authoritys[index].id;
    }
    //删除权限信息
    $scope.deleteAuthority = function () {
        $http.delete("rest/permissionInfo/"+$scope.deleteSystemId+"/delete", null).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "删除成功！");
                    $scope.getSysInfoList($scope.req.systemId );
                } else {
                    $("#serverErrorModal").modal({show: true});
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
    }

    //上一页
    $scope.previousPage = function () {
        if($("#previousPage").attr('unable') == "false") {
            $scope.req.paging.pageNo = $scope.req.paging.pageNo - 1;
            $scope.getSysInfoList($scope.req.systemId );
        }
    };

    //下一页
    $scope.nextPage = function () {
        if ($("#nextPage").attr('unable') == "false") {
            $scope.req.paging.pageNo = $scope.req.paging.pageNo + 1;
            $scope.getSysInfoList($scope.req.systemId );
        }
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

    var mu = document.getElementById("systemUl");
    var li = mu.getElementsByTagName("li");
    mu.onclick = function (e) {
        e = e||window.event;
        var target = e.srcElement || e.target;
        for (var i= 0,len=li.length;i<len;i++) {
            li[i].className = target ==li[i] ? "liSelected":"";
        }
    };


};


authorityListModule.$inject = ['$scope', '$http'];
authorityListModule.controller("authorityListController", authorityListController);
//authorityListModule.controller("permissionRequestController", permissionRequestController);
