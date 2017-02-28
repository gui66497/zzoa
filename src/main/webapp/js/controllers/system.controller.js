'use strict';

//系统管理模块
var systemModule = angular.module("systemModule", []);

var systemListController = function($scope, $http, LoginService) {
    $scope.req = {};
    $scope.req.paging = {};
    $scope.req.paging.pageNo = 1;
    $scope.req.paging.pageSize = 15;
    $scope.totalRecordCount = 0;
    $scope.totalPage = 0;
    $scope.currentPage = 1;
    $scope.dataSourceCount = 1; //数据源总数
    $scope.systemInfo = {};
    $scope.systemInfo.connection = "";
    $scope.systemInfo.tableName = "";
    $scope.systemInfo.columns = "";
    //$scope.dataSources = [{"connection":"jdbc:mysql://192.168.1.91:3306/tyqxgl?characterEncoding=utf8,gx,123456","tableName":"tb_system","columns":"system_id,system_name"}]
    $scope.dataSources = [{"connection":"","tableName":"","columns":""}];

    $scope.req.systemId=-1;
    $scope.req.authorityName = '查看系统';
    $http.post("rest/systemInfo/permissionSystem", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.systems = data.data;
            $scope.otherData = data.otherData;
            $scope.totalRecordCount = $scope.otherData[0];
            $scope.totalPage = $scope.otherData[1];
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    $http.post("rest/permissionInfo/getAuthorityName", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            //判断是否为超级管理员，isAdmin为超级管理员具有所有权限，否则权限通过authorityNameArra匹配
            if(data.message != null && data.message=="isAdmin"){
                $scope.addFlag = true;

            }else{
                $scope.authorityNameArra = data.data;
                if($scope.authorityNameArra.indexOf('添加系统') !=-1 ){
                    $scope.addFlag = true;
                }
            }
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    //获取系统信息列表
    $scope.getSystemList = function() {
       $http.post("rest/systemInfo/permissionSystem", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                console.log(data);
                $scope.systems = data.data;
                $scope.otherData = data.otherData;
                $scope.totalRecordCount = $scope.otherData==null?0:$scope.otherData[0];
                $scope.totalPage = $scope.otherData==null?0:$scope.otherData[1];
                //$scope.changeSysBySystemId($scope.systems.id)
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };


    ////根据系统ID获取角色信息列表
    //$scope.changeSysBySystemId = function(id) {
    //    //$scope.req.paging.pageNo = 1;
    //    //$scope.authorityNameArra=[];
    //    //$scope.req.systemId=id;
    //    //$scope.roleReq.systemId = id;
    //    //增删改查赋予权限
    //    $scope.addFlag = false;
    //    $scope.deleteFlag = false;
    //    $scope.updateFlag = false;
    //    $scope.showFlag = false;
    //
    //
    //    $http.post("rest/permissionInfo/getAuthorityName", $scope.req).success(function (data) {
    //        if (data != null && data != undefined) {
    //            console.log(data);
    //            //判断是否为超级管理员，isAdmin为超级管理员具有所有权限，否则权限通过authorityNameArra匹配
    //            if(data.message != null && data.message=="isAdmin"){
    //                $scope.addFlag = true;
    //                $scope.deleteFlag = true;
    //                $scope.updateFlag = true;
    //                $scope.showFlag = true;
    //            }else{
    //                $scope.authorityNameArra = data.data;
    //                if($scope.authorityNameArra.indexOf('添加系统') !=-1 ){
    //                    $scope.addFlag = true;
    //                }
    //                if($scope.authorityNameArra.indexOf('删除系统') !=-1 ){
    //                    $scope.deleteFlag = true;
    //                }
    //                if($scope.authorityNameArra.indexOf('更新系统') !=-1 ){
    //                    $scope.updateFlag = true;
    //                }
    //                if($scope.authorityNameArra.indexOf('查看系统') !=-1 ){
    //                    $scope.showFlag = true;
    //                }
    //            }
    //        }
    //    }).error(function (data) {
    //        $("#serverErrorModal").modal({show: true});
    //    });
    //};

    //
    ////获取所有系统信息列表
    //$scope.getAllSystemList = function() {
    //    //$scope.req.paging.pageSize = 10000;
    //    $http.post("rest/systemInfo/list", $scope.req).success(function (data) {
    //        if (data != null && data != undefined) {
    //            $scope.systems = data.data;
    //            $scope.otherData = data.otherData;
    //            $scope.totalRecordCount = $scope.otherData[0];
    //            $scope.totalPage = $scope.otherData[1];
    //        }
    //        $scope.setPageButtonStatus();
    //    }).error(function (data) {
    //        $("#serverErrorModal").modal({show: true});
    //    });
    //};

    //显示系统信息
    $scope.showSystemInfo = function(index) {
        $("#showSystemModal").modal({show: true});
        console.log($scope.systems[index]);
        $scope.systemInfo = $scope.systems[index];
        var dataSource = $scope.systemInfo.dataSource;
        $scope.dataSources = [];
        var dataSourceArray = dataSource.split("√");
        for(var i=0;i<dataSourceArray.length;i++) {
            var tmpDataSource = {};
            tmpDataSource.tableName = dataSourceArray[i].split(";")[0];
            tmpDataSource.columns = dataSourceArray[i].split(";")[1];
            tmpDataSource.connection = dataSourceArray[i].split(";")[2];
            $scope.dataSources.push(tmpDataSource)
        }
        /*$scope.systemInfo.tableName = dataSource.split(";")[0];
        $scope.systemInfo.columns = dataSource.split(";")[1];
        $scope.systemInfo.connection = dataSource.split(";")[2];*/
    }

    //显示新建系统窗口
    $scope.showCreateModal = function() {
        $("#createSystemModal").modal({show: true});
        $scope.systemInfo = {};
        //$scope.dataSources = [{"connection":"jdbc:mysql://192.168.1.91:3306/tyqxgl?characterEncoding=utf8,gx,123456","tableName":"tb_system","columns":"system_id,system_name"}]
        $scope.dataSources = [{"connection":"","tableName":"","columns":""}];
    }

    //创建系统
    $scope.createSystemInfo = function() {
        $scope.systemInfo.dataSource = "";
        for(var i=0;i<$scope.dataSources.length;i++) {
            var dataSource = $scope.dataSources[i];
            if(i==0) {
                $scope.systemInfo.dataSource = $scope.systemInfo.dataSource + dataSource.tableName + ";" + dataSource.columns + ";" + dataSource.connection;
            } else {
                $scope.systemInfo.dataSource = $scope.systemInfo.dataSource + "√" + dataSource.tableName + ";" + dataSource.columns + ";" + dataSource.connection;
            }
        }
        $scope.systemInfo.createTime = new Date();
        $http.post("rest/systemInfo/insert", $scope.systemInfo).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal("提示信息","创建成功！");
                    $scope.getSystemList();
                    $("#createSystemModal").modal('hide');
                } else {
                    showOkModal("提示信息","创建失败！" + data.message);
                }
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }

    //显示系统信息编辑窗口
    $scope.showEditModal = function(index) {
        $scope.systemInfo = null;
        $("#editSystemModal").modal({show: true});
        $scope.systemInfo = $scope.systems[index];
        var dataSource = $scope.systemInfo.dataSource;
        $scope.dataSources = [];
        var dataSourceArray = dataSource.split("√");
        for(var i=0;i<dataSourceArray.length;i++) {
            var tmpDataSource = {};
            tmpDataSource.tableName = dataSourceArray[i].split(";")[0];
            tmpDataSource.columns = dataSourceArray[i].split(";")[1];
            tmpDataSource.connection = dataSourceArray[i].split(";")[2];
            $scope.dataSources.push(tmpDataSource)
        }
    }

    //编辑系统信息
    $scope.editSystemInfo = function() {
        $scope.systemInfo.dataSource = "";
        for(var i=0;i<$scope.dataSources.length;i++) {
            var dataSource = $scope.dataSources[i];
            if(i==0) {
                $scope.systemInfo.dataSource = $scope.systemInfo.dataSource + dataSource.tableName + ";" + dataSource.columns + ";" + dataSource.connection;
            } else {
                $scope.systemInfo.dataSource = $scope.systemInfo.dataSource + "√" + dataSource.tableName + ";" + dataSource.columns + ";" + dataSource.connection;
            }
        }
        //$scope.systemInfo.dataSource = $scope.systemInfo.tableName + ";"  + $scope.systemInfo.columns + ";"  + $scope.systemInfo.connection;
        $http.put("rest/systemInfo/update", $scope.systemInfo).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal("提示信息","修改成功！");
                    $scope.getSystemList();
                    $("#editSystemModal").modal('hide');
                } else {
                    showOkModal("提示信息","修改失败！" + data.message);
                }
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }

    //添加数据源
    $scope.addDataSource = function() {
        $scope.dataSourceCount++;
        $scope.dataSources.push({"connection":"","tableName":"","columns":""});
    }

    //删除数据源
    $scope.deleteDataSource = function(i) {
        $scope.dataSources.splice(i,1);
    }

    //显示删除确认模态框
    $scope.showDelConfirmModal = function(index) {
        showConfirmModal('您确定删除吗？', $scope.deleteSystem);
        $scope.deleteSystemId = $scope.systems[index].id;
    }

    //删除系统信息
    $scope.deleteSystem = function () {
        $http.delete("rest/systemInfo/"+$scope.deleteSystemId+"/delete", null).success(function (data) {
            if(data.resultCode=="RESULT_SUCCESS") {
                showOkModal("提示信息","删除成功！");
                $scope.getSystemList();
            } else {
                showOkModal("提示信息","删除失败！");
            }
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
            $scope.getSystemList();
        }
    };

    //下一页
    $scope.nextPage = function () {
        if ($("#nextPage").attr('unable') == "false") {
            $scope.req.paging.pageNo = $scope.req.paging.pageNo + 1;
            $scope.getSystemList();
        }
    };

    //设置系统创建时间
    $scope.setSystemCreateTime = function (date) {
        $scope.systemInfo.createTime = date;
        alert($scope.systemInfo.createTime);
    };
    if(LoginService.isAuthorized==true) {
        $scope.getSystemList();
    }
};


systemModule.$inject = ['$scope', '$http'];
systemModule.controller("systemListController", systemListController);
