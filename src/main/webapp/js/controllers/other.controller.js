'use strict';

//otherController
indexModule.controller('otherController',['$scope', '$http', 'NgTableParams', '$timeout', '$location', function ($scope, $http, NgTableParams, $timeout, $location) {
    $scope.staff = {};
    $scope.staff.password = "";

    $scope.showUpdatePwdModal = function () {
        $scope.pass = "";
        $scope.repass = "";
        $scope.newpass = "";
        $("#updatePwdModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //判断当前页面是什么模块 选中当前
    $scope.isOn = function (val) {
        var type;
        var url = $location.url();
        if (url === '/') {                  //考勤
            type = 'attendance';
        } else if (url==='/human') {  //人事
            type = 'human';
        }else if(url==='/commerce'){
            type = 'commerce';
        }
        return val === type ? 'on' : '';
    };

    $scope.updatePass = function () {

        $http.put("rest/staff/" + $scope.pass + "/checkUser").success(function (data) {
            if (data.resultCode == "RESULT_ERROR") {
                showOkModal('错误', "原始密码不正确");
                return;
            }else{
                var userPassReg = /(^[A-Za-z0-9]{6,20}$)/;//密码正则
                //str.replace(/(^\s*)|(\s*$)/g, "")
                if (!userPassReg.test($scope.newpass)) {
                    showOkModal('错误', '密码格式不正确!支持6到20位的英文或数字');
                    return;
                }
                if ($scope.newpass != $scope.repass) {
                    showOkModal('错误', '两次输入不一致');
                    return;
                }
                $scope.staff.password = $scope.newpass;
                $http.post("rest/staff/editPass", $scope.staff).success(function (data) {
                    if (data != null && data != undefined) {
                        if (data.resultCode == "RESULT_ERROR") {
                            showOkModal('错误', data.message);
                        } else {
                            $("#updatePwdModal").modal('hide');
                            showOkModal('提示', "修改成功！");
                            //LoginService.logout();
                        }
                    }
                });
            }
        });
    };

    //跳转到在线考试
    $scope.jumpToZxks = function () {
        var url = $location.absUrl();
        var zxksUrl = url.substr(0, url.indexOf("zzoa")) + "zxks";
        window.open(zxksUrl);
    };
    //跳转到日报系统
    $scope.jumpToRbgl = function () {
        var url = $location.absUrl();
        var rbglUrl = url.substr(0, url.indexOf("zzoa")) + "WorkLog";
        window.open(rbglUrl);
    };
}]);
