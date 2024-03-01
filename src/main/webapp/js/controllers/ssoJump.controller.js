'use strict';

/**
 * 单点登录跳转 模块
 */
indexModule.controller('ssoJumpController',['$scope', '$http', 'LoginService', function ($scope, $http, LoginService) {

    //判断是否登录 第一次登进来时
    LoginService.checkSSOAuthorization();

}]);