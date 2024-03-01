'use strict';

//考勤统计管理模块
indexModule.controller('mergeController',['$scope', '$http', 'staffService', function ($scope, $http, staffService) {

    //加载部门信息
    var promise = staffService.getAllDeparts();
    promise.then(function (data) {
        if (data) {
            $scope.departs= data;
        }
    });

    //如果是今天1-15号则初始为上个月16号 如果是16或者之后则为当月16号
    function initCHeckDate(){
        var beginDateStr = staffService.getStartTime();
        var endDateStr = staffService.getEndTime();

        document.getElementById("startMergeMonth").value = formatDate(new Date(beginDateStr + ' 08:00:00'));
        document.getElementById("endMergeMonth").value = formatDate(new Date(endDateStr + ' 08:00:00'));
    }

    initCHeckDate();

    $scope.depart = "null";

    function allVacation() {
        var all = "";
        if($scope.leaveinlieu==undefined||$scope.leaveinlieu==false){
            all +="0"
        }else{
            all +="1"
        };
        if($scope.annual==undefined||$scope.annual==false){
            all +="0"
        }else{
            all +="1"
        }
        if($scope.treasure==undefined||$scope.treasure==false){
            all +="0"
        }else{
            all +="1"
        }
        if($scope.maternity==undefined||$scope.maternity==false){
            all +="0"
        }else{
            all +="1"
        }
        if($scope.marriage==undefined||$scope.marriage==false){
            all +="0"
        }else{
            all +="1"
        }
        if($scope.miami==undefined||$scope.miami==false){
            all +="0"
        }else{
            all +="1"
        }
        if($scope.sick==undefined||$scope.sick==false){
            all +="0"
        }else{
            all +="1"
        }
        if($scope.project==undefined||$scope.project==false){
            all +="0"
        }else{
            all +="1"
        }
        $scope.req = {};
        $scope.selected = [];
        $scope.currentSystemId = -1;

        var beginDate = document.getElementById("startMergeMonth").value;
        var endDate = document.getElementById("endMergeMonth").value;
        if(beginDate==''){
            beginDate=undefined
        }
        if(endDate==''){
            endDate=undefined
        }
        $http.post("rest/attenSummary/"+$scope.depart+"/"+beginDate+"/"+endDate+"/"+all+"/summaryYear", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.merges = data.data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }
    allVacation();

    $scope.mergeShow=function () {
        allVacation()
    };

    $scope.off=function () {
        allVacation()
    };



    var now = new Date();                    //当前日期
    var nowDayOfWeek = now.getDay();         //今天本周的第几天
    var nowDay = now.getDate();              //当前日
    var nowMonth = now.getMonth();           //当前月
    var nowYear = now.getYear();             //当前年
    nowYear += (nowYear < 2000) ? 1900 : 0;  //

    var lastMonthDate = new Date();  //上月日期
    lastMonthDate.setDate(1);
    lastMonthDate.setMonth(lastMonthDate.getMonth()-1);
    var lastYear = lastMonthDate.getYear();
    var lastMonth = lastMonthDate.getMonth();

    //格式化日期：yyyy-MM-dd
    function formatDate(date) {
        var myyear = date.getFullYear();
        var mymonth = date.getMonth()+1;
        var myweekday = date.getDate();

        if(mymonth < 10){
            mymonth = "0" + mymonth;
        }
        if(myweekday < 10){
            myweekday = "0" + myweekday;
        }
        return (myyear+"-"+mymonth + "-" + myweekday);
    }

    //获得某月的天数
    function getMonthDays(myMonth){
        var monthStartDate = new Date(nowYear, myMonth, 1);
        var monthEndDate = new Date(nowYear, myMonth + 1, 1);
        var   days   =   (monthEndDate   -   monthStartDate)/(1000   *   60   *   60   *   24);
        return   days;
    }

    //上周
    $scope.lastWeek=function () {
        document.getElementById("startMergeMonth").value = formatDate(new Date(nowYear, nowMonth, nowDay - nowDayOfWeek -7,8));
        document.getElementById("endMergeMonth").value = formatDate(new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek - 7),8));
        if(document.getElementById("startMergeMonth").value){
            $scope.mergeShow();
        }
    };
    //上月
    $scope.lastMonth=function () {
        document.getElementById("startMergeMonth").value = formatDate(new Date(nowYear, lastMonth, 1,8));
        document.getElementById("endMergeMonth").value = formatDate(new Date(nowYear, lastMonth, getMonthDays(lastMonth),8));
        if(document.getElementById("startMergeMonth").value){
            $scope.mergeShow();
        }
    };
    //本周
    $scope.thisWeek=function () {
        document.getElementById("startMergeMonth").value = formatDate(new Date(nowYear, nowMonth, nowDay - nowDayOfWeek,8));
        document.getElementById("endMergeMonth").value = formatDate(new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek),8));
        if(document.getElementById("startMergeMonth").value){
            $scope.mergeShow();
        }
    };
    //本月
    $scope.thisMonth=function () {
        document.getElementById("startMergeMonth").value = formatDate(new Date(nowYear, nowMonth, 1, 8));;
        document.getElementById("endMergeMonth").value = formatDate(new Date(nowYear, nowMonth, getMonthDays(nowMonth), 8));;
        if(document.getElementById("startMergeMonth").value){
            $scope.mergeShow();
        }
    };

    //导出excel
    $scope.export = function () {
        $http.post("rest/attenSummary/合并统计/export", JSON.stringify($scope.merges), {responseType: 'blob'}).success(function (data, status, headers) {
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            var fileName = headers()["filename"];
            fileName = decodeURI(fileName);
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };
}]);