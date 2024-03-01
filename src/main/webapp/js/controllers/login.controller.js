'use strict';

//主页面模块
var indexModule = angular.module("indexModule", ['routeModule', 'loginServiceModule', 'ngCookies', 'ngAnimate', 'ngSanitize', 'ui.bootstrap', 'ngTable', 'treeControl', 'ui.bootstrap.datetimepicker', 'ui.tree']);

indexModule.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.common['userId'] = getCookie('loginUserId');

}]);

//登陆功能控制器
var loginController = function ($scope, $http, LoginService) {
    $scope.user = {};
    $scope.user.userName = "";
    $scope.user.password = "";

    $("#loginModal").modal({show: true});
    $scope.loginUserStyle={
        "border-bottom":"1px solid #58c9f3"
    };
    $scope.loginUser=function(){
        $scope.loginUserStyle={
            "border-bottom":"1px solid #58c9f3"
        };
        $scope.loginUserStyle2={
            "border-bottom":"1px solid #dddddd"
        };
    };
    $scope.loginUser2=function(){
        $scope.loginUserStyle2={
            "border-bottom":"1px solid #58c9f3"
        };
        $scope.loginUserStyle={
            "border-bottom":"1px solid #dddddd"
        }
    };

    LoginService.checkAuthorization();  //进入页面后就进行一次用户是否登陆的校验
    $scope.mykeyup = function (e) {
        if (e.keyCode == 13 && $scope.user.userName != "" && $scope.user.password != "") {
            $scope.login();
        }
    };
    $scope.login = function () {
        LoginService.login($scope.user);
        if (LoginService.isAuthorized == true) {
            //$scope.$broadcast('loginSuccess', $scope.user);
        }
    };

    $scope.$on('Logout.Success', function (event, userName) {
        $scope.user = null;
        $("#userName").focus();
        $("#loginModal").modal({show: true});
        $("#loginModal").on('shown.bs.modal', function (e) {
            $("#loginUserName").focus();
        });
    });

    $("#loginUserName").focus();
};

//登出功能控制器
var logoutController = function ($scope, LoginService) {
    $scope.user = {};
    $scope.user.userName = "";
    $scope.user.password = "";


    $scope.logout = function () {
        LoginService.logout();
    };

    $scope.ssoLogout = function () {
        LoginService.ssoLogout();
    };

    $scope.$on('Logout.Success', function (event, userName) {
        $scope.user = null;
        $("#userName").focus();
        $("#loginModal").modal({show: true});
        $("#loginModal").on('shown.bs.modal', function (e) {
            $("#loginUserName").focus();
        });
    });
};

indexModule.$inject = ['$scope', 'LoginService'];
indexModule.controller("loginController", loginController);
indexModule.controller("logoutController", logoutController);