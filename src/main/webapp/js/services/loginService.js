'use strict';

//登陆服务模块
var loginServiceModule = angular.module("loginServiceModule", []);

//登陆服务
var loginService = function ($http, $rootScope, $cookieStore, $location) {
    return {
        //定义当前用户是否被授权
        isAuthorized: typeof($cookieStore.get("loginUserName")) == 'undefined' ? false : true,
        //当前登陆的用户名和ID
        loginUserName: typeof($cookieStore.get("loginUserName")) == 'undefined' ? "" : $cookieStore.get("loginUserName"), //当前登陆的用户名
        loginUserId: typeof($cookieStore.get("loginUserId")) == 'undefined' ? "" : $cookieStore.get("loginUserId"), //当前登陆的用户ID
        loginName: typeof($cookieStore.get("loginName")) == 'undefined' ? "" : $cookieStore.get("loginName"), //当前登陆的中文名

        //用户登陆功能
        login: function (user) {
            var self = this;
            $http.post("rest/staff/login", user).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        $cookieStore.put("loginUserName", user.userName);
                        //向cookie中添加userId,name
                        $cookieStore.put("loginUserId", data.otherData[0]);
                        $cookieStore.put("loginName", data.otherData[1]);
                        $cookieStore.put("loginDepartName", user.departName);
                        self.loginUserName = user.userName;
                        self.loginName = data.otherData[1];
                        console.info(data.otherData[1]);
                        $rootScope.$broadcast('Login.Success', user.userName);
                        $rootScope.$broadcast('Login.Success.data', data.data);
                        $("#loginModal").modal('hide');
                        window.location.href = "welcome.html";
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
            $cookieStore.remove("loginName");
            $rootScope.$broadcast('Logout.Success');
            window.location.href = "login.html";
        },

        // 清空用户cookie
        clearCookie: function() {
            $cookieStore.remove("loginUserName");
            $cookieStore.remove("loginUserId");
            $cookieStore.remove("loginName");
        },

        //检验当前是否为已登录状态，或Cookie中仍存在登陆记录
        checkAuthorization: function () {
            var self = this;
            //必须从Cookie中校验(Cookie)
            this.isAuthorized = typeof($cookieStore.get("loginUserName")) == 'undefined' ? false : true;
            //根据是否已登录，设置登陆窗口和主页面的显示与否
            //$("#loginModal").modal(this.isAuthorized == true ? 'hide' : {keyboard:false,backdrop:'static'});

            if (this.isAuthorized == true) {
                $http.get("rest/staff/hasLogin").success(function (data) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        self.loginUserName = $cookieStore.get("loginUserName");
                        self.loginName = $cookieStore.get("loginName");
                        window.location.href = "welcome.html";
                        $("#loginUserName").focus();
                    }
                }).error(function (data) {
                    $("#serverErrorModal").modal({show: true});
                });
            }
        },

        //单点登录登出
        ssoLogout : function () {
            var absUrl = $location.absUrl();
            var url = absUrl.substr(0, absUrl.indexOf("/zzoa"));
            $cookieStore.remove("loginUserName");
            $cookieStore.remove("loginUserId");
            $cookieStore.remove("loginName");
            /*$rootScope.$broadcast('Logout.Success');*/
            window.location.href = url + "/cas/logout?service=" + url + "/zzoa";
        },

        //检验当前是否为已登录状态(sso单点登录模式 正常登录)
        checkSSOAuthorization: function () {
            var self = this;
            $http.get("rest/staff/isSsoLogin").success(function (data) {
                console.log("isSsoLogin:", data);
                var absUrl = $location.absUrl();
                var url = absUrl.substr(0, absUrl.indexOf("/zzoa"));
                if(data.resultCode === "RESULT_SUCCESS") {
                    var user = data.obj;
                    $cookieStore.put("loginUserName", user.userName);
                    //向cookie中添加userId,name
                    $cookieStore.put("loginUserId", user.staffId);
                    $cookieStore.put("loginName", user.name);
                    $cookieStore.put("loginDepartName", user.departName);
                    self.loginUserName = user.userName;
                    self.loginName =  user.name;
                    $rootScope.$broadcast('Login.Success', user.userName);
                    $rootScope.$broadcast('Login.Success.data', data.obj);

                    window.location.href = "welcome.html";
                } else {
                    if (data.resultCode === "RESULT_NOT_EXIST") {
                        //认证通过，但OA系统中未找到此人信息
                        showConfirmModal(data.message + "，点击确定登出", function () {
                            window.location.href = url + "/cas/logout?service=" + url + "/zzoa";
                        });
                    } else {
                      if (absUrl.indexOf("ssoJump") > -1) {
                        //注销后会跳转到ssoJump.html 所以特殊处理 reload一下触发拦截
                        location.reload();
                      } else {
                        window.location.href = "ssoJump.html";
                      }
                    }
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        },

        /*checkSSOAuthorization : function () {
            var self = this;
            $http.get("rest/staff/isSsoLogin").success(function (data) {
                console.log("isSsoLogin:", data);
                if(data.resultCode === "RESULT_SUCCESS") {
                    var user = data.obj;

                    var cookieUserName = $cookieStore.get("loginUserName");
                    if (cookieUserName === null || cookieUserName === undefined) {
                        $cookieStore.put("loginUserName", user.userName);
                        //向cookie中添加userId,name
                        $cookieStore.put("loginUserId", user.staffId);
                        $cookieStore.put("loginName", user.name);
                        self.loginUserName = user.userName;
                        self.loginName =  user.name;
                        $rootScope.$broadcast('Login.Success', user.userName);
                        $rootScope.$broadcast('Login.Success.data', data.obj);
                        window.location.href = "welcome.html";
                    } else if (cookieUserName !== user.userName) {
                        $cookieStore.put("loginUserName", user.userName);
                        //向cookie中添加userId,name
                        $cookieStore.put("loginUserId", user.staffId);
                        $cookieStore.put("loginName", user.name);
                        self.loginUserName = user.userName;
                        self.loginName =  user.name;
                    }

                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });

        }*/

        //每次刷新页面都要做判断
        SsoHasLogin : function () {
            $http.get("rest/staff/isSsoLogin").success(function (data) {
                if(data.resultCode === "RESULT_SUCCESS") {
                    var user = data.obj;
                    var cookieUserName = $cookieStore.get("loginUserName");
                    if (cookieUserName === null && cookieUserName === undefined || cookieUserName !== user.userName) {
                        window.location.href = "ssoJump.html";
                    }
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });

        }
    };
};

loginServiceModule.$inject = ['$http', '$rootScope', '$cookieStore'];
loginServiceModule.service("LoginService", loginService);