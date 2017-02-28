'use strict';

//登陆服务模块
var loginServiceModule = angular.module("loginServiceModule", []);

//登陆服务
var loginService = function ($http, $rootScope, $cookieStore) {
    return {
        //定义当前用户是否被授权
        isAuthorized: typeof($cookieStore.get("loginUserName")) == 'undefined' ? false : true,
        //当前登陆的用户名和ID
        loginUserName: typeof($cookieStore.get("loginUserName")) == 'undefined' ? "" : $cookieStore.get("loginUserName"), //当前登陆的用户名
        loginUserId: typeof($cookieStore.get("loginUserId")) == 'undefined' ? "" : $cookieStore.get("loginUserId"), //当前登陆的用户ID

        //用户登陆功能
        login: function (user) {
            var self = this;
            $http.post("rest/user/login", user).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        $cookieStore.put("loginUserName", user.userName);
                        //向cookie中添加userId
                        $cookieStore.put("loginUserId", data.otherData[0]);
                        self.loginUserName = user.userName;
                        $rootScope.$broadcast('Login.Success', user.userName);
                        $rootScope.$broadcast('Login.Success.data', data.data);
                        $("#loginModal").modal('hide');
                    }
                    else {
                        $("#loginErrorMsg").show();
                    }
                }
                //$scope.setPageButtonStatus();
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        },

        //用户登出功能
        logout: function () {
            $cookieStore.remove("loginUserName");
            $cookieStore.remove("loginUserId");
            $rootScope.$broadcast('Logout.Success');
        },

        // 清空用户cookie
        clearCookie: function() {
            $cookieStore.remove("loginUserName");
            $cookieStore.remove("loginUserId");
        },

        //检验当前是否为已登录状态，或Cookie中仍存在登陆记录
        checkAuthorization: function () {
            var self = this;
            //必须从Cookie中校验(Cookie)
            this.isAuthorized = typeof($cookieStore.get("loginUserName")) == 'undefined' ? false : true,
            //根据是否已登录，设置登陆窗口和主页面的显示与否
            $("#loginModal").modal(this.isAuthorized == true ? 'hide' : {keyboard:false,backdrop:'static'});
            $("#loginUserName").focus();
            if (this.isAuthorized == true) {
                self.loginUserName = $cookieStore.get("loginUserName");
            }
        }
    };
};

loginServiceModule.$inject = ['$http', '$rootScope', '$cookieStore'];
loginServiceModule.service("LoginService", loginService);