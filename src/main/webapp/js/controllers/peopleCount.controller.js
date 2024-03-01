'use strict';

//人数计算模块
indexModule.controller('peopleCountController',['$scope', '$http', '$timeout',  function ($scope, $http, $timeout) {
    var date=new Date;
    var year=date.getFullYear();
    var month=date.getMonth()+1;
    month =(month<10 ? "0"+month:month);
    var startdate = (year.toString()+'-01');
    var enddate = (year.toString()+'-'+month.toString());
    document.getElementById("startCountMonth").value=startdate;
    document.getElementById("endCountMonth").value=enddate;

    function count() {
        $scope.req={};
        $scope.req.beginMonth = document.getElementById("startCountMonth").value;
        $scope.req.endMonth = document.getElementById("endCountMonth").value;
        $scope.req.rule = document.getElementById("rule").value;
        $scope.req.education = document.getElementById("record").value==""?null:document.getElementById("record").value;
        $scope.req.post = document.getElementById("moldPeople").value==""?null:document.getElementById("moldPeople").value;

        $http.post("rest/attenSummary/numbersSummary", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.counts = data.data;
                for(var i=0;i<$scope.counts.length-1;i++){
                    $scope.counts[i].month=$scope.counts[i].month.replace(/-/g,"年")+'月'
                }
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });

        $http.get("rest/staff/allPostList", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.molds = data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });

    }
    count();

    $scope.CountShow=function () {
        count()
    };

    //导出excel
    $scope.export = function () {
        $http.post("rest/attenSummary/人数计算/export", JSON.stringify($scope.counts), {responseType: 'blob'}).success(function (data, status, headers) {
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