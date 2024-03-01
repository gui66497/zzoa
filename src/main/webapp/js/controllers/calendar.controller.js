'use strict';

//出差管理模块
indexModule.controller('calendarController',['$scope', '$http', '$timeout',function ($scope, $http, $timeout) {

    $scope.req = {};
    $scope.calendars = [];

    var date=new Date;
    var year=date.getFullYear();
    var month=date.getMonth()+1;
    month =(month<10 ? "0"+month:month);
    var mydate = (year.toString()+'-'+month.toString());
    document.getElementById("queryMonth").value=mydate;

    $http.post("rest/calendar/"+document.getElementById("queryMonth").value+"/list", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.calendars = data.slice(1);
            console.log($scope.calendars);
            $scope.work1Ho0D1={
                "font-size": "16px",
                "background-color":"rgb(245,245,245)"
            };
            $scope.work1Ho0D1={
                "font-size": "16px",
                "background-color":"rgb(245,245,245)"
            };
            $scope.work0Ho0D1={
                "font-size": "16px",
                "background-color":"rgb(174,238,238 )"
            };
            $scope.work1Ho1D1={
                "font-size": "16px",
                "background-color":"rgb(255,219,219)"
            };
            $scope.work0Ho1D1={
                "font-size": "16px",
                "background-color":"rgb(255,219,219)"
            };
            $scope.work1Ho0D0={
                "font-size": "14px",
                "color": "rgb(100,100,100 )",
                "background-color":"white"
            };
            $scope.work0Ho0D0={
                "font-size": "14px",
                "color": "rgb(100,100,100 )",
                "background-color":"white"
            };
            $scope.work1Ho1D0={
                "font-size": "14px",
                "color": "rgb(100,100,100 )",
                "background-color":"white"
            };
            $scope.work0Ho0D0={
                "font-size": "14px",
                "color": "rgb(100,100,100 )",
                "background-color":"white"
            };
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    $scope.dateQuery=function () {
        $scope.calendars = [];
        $http.post("rest/calendar/"+document.getElementById("queryMonth").value+"/list", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.calendars = data.slice(1);
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };
    $scope.changeWork=true;
    $scope.changeHoliday=true;
    $scope.changeHoliday0=true;
    $scope.changeWork0=true;

        $scope.setWorkday=function (index) {
            var b;
            $scope.one=function (a) {
               b=a
            };
           $timeout(function () {
               var w_typ = $scope.calendars[b][index].isWorkday;
               $http.put("rest/calendar/"+$scope.calendars[b][index].curDay+"/"+w_typ+"/toWorkday", null).success(function (data) {
                   if (data != null && data != undefined) {
                       if(data.resultCode=="RESULT_SUCCESS") {
                           // showOkModal('提示', "设置成功！");
                           $scope.dateQuery();
                       } else {
                           showOkModal('提示', data.message);
                       }
                   }
               }).error(function (data) {
                   $("#serverErrorModal").modal({show: true});
               });
           },500);
        };

    $scope.setHoliday=function (index) {
        var c;
        $scope.one=function (a) {
            c=a
        };
        $timeout(function () {
            var h_typ = $scope.calendars[c][index].isHoliday;
            $http.put("rest/calendar/"+$scope.calendars[c][index].curDay+"/"+h_typ+"/toHoliday", null).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        // showOkModal('提示', "设置成功！");
                        $scope.dateQuery();
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        },500);
    };


}]);
