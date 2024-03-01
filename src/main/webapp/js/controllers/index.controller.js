'use strict';

//主页面模块
var indexModule = angular.module("indexModule", ['routeModule','ngSanitize', 'loginServiceModule', 'ngCookies', 'ngAnimate', 'ui.bootstrap', 'ngTable', 'treeControl', 'ui.bootstrap.datetimepicker','ui.tree','ui.select', 'angular-loading-bar', 'isteven-multi-select','selectAddress']);

indexModule.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.common['userId'] = getCookie('loginUserId');

}]);

//菜单模块控制器
var menuController = ['$scope', '$http', 'LoginService', '$q', 'NgTableParams',function ($scope, $http, LoginService, $q, NgTableParams) {
    moment.locale('zh-cn');

    //判断用户是否已登陆 (单点登录之前的版本)
    /*$scope.hasLogin = function() {
        alert(1);
        $http.get("rest/staff/hasLogin").success(function (data) {
            if(data.resultCode=="RESULT_NOT_AUTHORIZED") {
                LoginService.clearCookie();
                window.location.href = "login.html";
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };
    $scope.hasLogin();*/

    //判断用户是否已登陆
    LoginService.SsoHasLogin();


    $scope.loginUserName = LoginService.loginUserName;
    $scope.loginName = LoginService.loginName;
    $scope.$on('Login.Success', function (event, userName) {
        $scope.loginUserName = LoginService.loginUserName;
        $scope.loginName = LoginService.loginName;
    });

    $scope.$on('Login.Success.data', function (event, menus) {
        //setAllMenu(menus);
        //登陆成功后刷新页面，会调用rest/user/getMenus获取应该展示的菜单
        location.reload();
    });


    /*if(LoginService.isAuthorized) {
        $scope.getMenus();
    }*/

}];

//菜单模块控制器
var backupController =[ '$scope', '$http',function ($scope, $http) {
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
    };

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
    };

    $scope.resetBackupFileName = function () {
        $scope.backup.backupFileName = "";
    };

    $scope.showOkModal = function(title,bodyContent){
        $("#noticeTitle").html(title);
        $("#noticeBody").html(bodyContent);
        $("#showOkModal").modal({show: true});
    }

}];

indexModule.$inject = ['$scope','LoginService'];
indexModule.controller("loginController", loginController);
indexModule.controller("logoutController", logoutController);
indexModule.controller("menuController", menuController);
indexModule.controller("backupController", backupController);