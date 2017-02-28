'use strict';

//菜单服务模块
var menuServiceModule = angular.module("menuServiceModule", []);

//菜单服务
var menuService = function ($http, $rootScope, $cookieStore) {
    return {
        //定义当前激活的菜单
        currentURL: "",

        //根据currentURL设置指定的菜单为激活状态
        setMenuStatus: function () {
            if (this.currentURL == "") {
                $("#main-menu-report-overview").addClass("active");
            }
            if (this.currentURL.indexOf("/") > 0) {
                if (this.currentURL.indexOf("/") == this.currentURL.lastIndexOf("/")) {
                    if (this.currentURL.indexOf("report") == -1 && this.currentURL.indexOf("station") == -1 && this.currentURL.indexOf("attendance") == -1 && this.currentURL.indexOf("marketManagement") == -1
                        && this.currentURL.indexOf("media") == -1 && this.currentURL.indexOf("setting") == -1 && this.currentURL.indexOf("heatmap") == -1 && this.currentURL.indexOf("cus") == -1
                        && this.currentURL.indexOf("marketing") == -1) {
                        this.currentURL = this.currentURL.substr(0, this.currentURL.lastIndexOf("/"));
                    }
                } else {
                    this.currentURL = this.currentURL.substr(0, this.currentURL.lastIndexOf("/"));
                }
            }

            this.currentURL = this.currentURL.replace("/", "-");
            $("#main-menu-" + this.currentURL).addClass("active");
        },
        //根据currentURL设置指定的菜单为disable状态
        removeMenuStatus: function () {
            if (this.currentURL == "") {
                $("#main-menu-report-overview").removeClass("active");
            }
            $("#main-menu-" + this.currentURL).removeClass("active");
        },
        //隐藏非当前菜单
        hideOtherMenus: function() {
            $(".submenu").hide();
            $(".triangle-class").attr("src","img/cmcc/triangle-down.png");
            if(this.currentURL == "") {
                $("[id^=main-menu-report]").show();
                $("[id^=main-menu-report]:first").find("img:last").attr("src","img/cmcc/triangle-up.png");
            } else {
                var activeModule = this.currentURL.substr(0,this.currentURL.indexOf("-"));
                $("[id^=main-menu-"+activeModule+"]").show();
                $("[id^=main-menu-"+activeModule+"]:first").find("img:last").attr("src","img/cmcc/triangle-up.png");
            }
        },
        //展示当前菜单
        showCurrentMenus: function() {

        }

    };
};

menuServiceModule.$inject = ['$http', '$rootScope', '$cookieStore'];
menuServiceModule.service("MenuService", menuService);