'use strict';

//主页面路由模块，用于控制页面的菜单导航
var routeModule = angular.module("routeModule", ['ngRoute','loginServiceModule']);

//路由器的具体分发
function indexRouteConfig($routeProvider) {
    $routeProvider.
        when('/', {
            templateUrl: 'page/humanResource.html'
        }).when('/system', {
            templateUrl: 'page/system.html'
        }).when('/authority', {
            templateUrl: 'page/authority.html'
        }).when('/role', {
            templateUrl: 'page/role.html'
        }).when('/user', {
            templateUrl: 'page/user.html'
        }).when('/excel', {
            controller: 'excelController',
            templateUrl: 'page/excelTest.html'
        }).otherwise({
            redirectTo: "/"
        });
};

routeModule.run(['$rootScope', '$window', '$location', '$log', '$http','LoginService',
    function ($rootScope, $window, $location, $log, $http, LoginService) {
        $rootScope.$on('$locationChangeSuccess', locationChangeSuccess);
        function locationChangeSuccess(event, location) {
            //alert(LoginService.isAuthorized);
        }

    }]);
routeModule.config(indexRouteConfig);