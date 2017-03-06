'use strict';

//主页面模块
var indexModule = angular.module("indexModule", ['routeModule', 'loginServiceModule', 'ngCookies', 'ngAnimate', 'ngSanitize', 'ui.bootstrap', 'ngTable', 'treeControl', 'ui.bootstrap.datetimepicker']);

indexModule.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.common['userId'] = getCookie('loginUserId');

}]);

//登陆功能控制器
var loginController = function ($scope, LoginService) {

    $scope.user = {};
    $scope.user.userName = "";
    $scope.user.password = "";

    //LoginService.checkAuthorization();  //进入页面后就进行一次用户是否登陆的校验
        $scope.mykeyup = function (e) {
            if (e.keyCode == 13 && $scope.user.userName != "" && $scope.user.password != "") {
                $scope.login();
            }

    }
    $scope.login = function () {
        LoginService.login($scope.user);
        if (LoginService.isAuthorized == true) {
            //$scope.$broadcast('loginSuccess', $scope.user);
        }
    };

    $scope.logout = function () {
        LoginService.logout();
    }

    $scope.$on('Logout.Success', function (event, userName) {
        $scope.user = null;
        $("#userName").focus();
        $("#loginModal").modal({show:true});
        $("#loginModal").on('shown.bs.modal', function(e) {
            $("#loginUserName").focus();
        });
    });
};

//菜单模块控制器
var menuController = function ($scope, $http, LoginService) {

    $scope.showSystem = false;      //显示系统
    $scope.showPermision = false;   //显示权限
    $scope.showRole = false;        //显示角色
    $scope.showUser = false;        //显示用户
    $scope.showBackup = false;      //显示备份

    $scope.loginUserName = LoginService.loginUserName;
    $scope.$on('Login.Success', function (event, userName) {
        $scope.loginUserName = LoginService.loginUserName;
    });

    //判断用户是否已登陆
    $scope.hasLogin = function() {
        $http.get("rest/user/hasLogin").success(function (data) {
            if(data.resultCode=="RESULT_NOT_AUTHORIZED") {
                LoginService.clearCookie();
                $("#loginModal").modal({show:true});
                $("#loginModal").on('shown.bs.modal', function(e) {
                    $("#loginUserName").focus();
                });
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }

    //系统备份
    $scope.backup = function () {
        $http.get("rest/bakReduce/bak").success(function (data) {
            if (data.resultCode == "RESULT_SUCCESS") {
                showOkModal("提示信息", "系统备份成功！");
            } else {
                showOkModal("提示信息", "系统备份失败！");
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }

    //显示系统数据恢复窗口
    $scope.showRreduceModal = function () {
        $("#reduceModal").modal({show: true});
    }

    $scope.$on('Login.Success.data', function (event, menus) {
        //setAllMenu(menus);
        //登陆成功后刷新页面，会调用rest/user/getMenus获取应该展示的菜单
        location.reload();
    });

    //获取当前用户可访问的菜单
    $scope.getMenus = function() {
        $http.get("rest/user/getMenus").success(function (data) {
            if (data != null && data != undefined) {
                if (data.message != null && data.message == "isAdmin") {
                    $scope.showSystem = true;
                    $scope.showPermision = true;
                    $scope.showRole = true;
                    $scope.showUser = true;
                    $scope.showBackup = true;
                } else {
                    setAllMenu(data.data);
                }
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }


    //根据数组判定菜单显示
    var setAllMenu = function (menus) {
        var menuArr = menus;
        if (menuArr.indexOf('系统管理') != -1) {
            $scope.showSystem = true;
        }
        if (menuArr.indexOf('权限管理') != -1) {
            $scope.showPermision = true;
        }
        if (menuArr.indexOf('角色管理') != -1) {
            $scope.showRole = true;
        }
        if (menuArr.indexOf('用户管理') != -1) {
            $scope.showUser = true;
        }
        if (menuArr.indexOf('备份恢复') != -1) {
            $scope.showBackup = true;
        }
    };

    //$scope.hasLogin();
    if(LoginService.isAuthorized) {
        $scope.getMenus();
    }

};

//菜单模块控制器
var backupController = function ($scope, $http) {
    //$scope.backupFiles = [{id:1,fileName:'file1'},{id:2,fileName:'file2'}]
    $scope.backupFiles = [];
    $scope.backup = {};
    $scope.backup.backupFileName = "";
    $scope.backupStatus = false;

    //获取备份文件列表
    $scope.getBackupFiles = function () {
        $http.get("rest/bakReduce/backupFiles").success(function (data) {
            if (data.resultCode == "RESULT_SUCCESS") {
                $scope.backupFiles = data.data;
                //$scope.backupFiles.unshift("请选择要还原的备份文件");
            } else {
                showOkModal("提示信息", "获取备份文件失败！");
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }

    //系统还原
    $scope.reduce = function () {
        $scope.backupStatus = true;
        $http.post("rest/bakReduce/reduce", $scope.backup).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    showOkModal("提示信息", "系统还原成功！");
                } else {
                    showOkModal("提示信息", "系统还原失败！");
                }
                $scope.backupStatus = false;
                $("#reduceModal").modal('hide');
                $scope.backup.backupFileName = "";
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }

    $scope.resetBackupFileName = function () {
        $scope.backup.backupFileName = "";
    }

}

indexModule.$inject = ['$scope','LoginService'];
indexModule.controller("loginController", loginController);
indexModule.controller("menuController", menuController);
indexModule.controller("backupController", backupController);