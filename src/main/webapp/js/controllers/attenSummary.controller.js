'use strict';

//考勤统计管理模块
indexModule.controller('attenSummaryController',['$scope','$http', 'staffService', function ($scope, $http, staffService) {

    //加载部门信息
    var promise = staffService.getAllDeparts();
    promise.then(function (data) {
        if (data) {
            $scope.departs= data;
        }
    });

    $scope.req = {};
    $scope.selected = [];
    $scope.currentSystemId = -1;
    $scope.req.depart = "null";

    //初始化开始日期和结束日期
    var date = new Date;
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    month = (month < 10 ? "0" + month : month);
    var beginDateStr = (year.toString() + '-' + month.toString());
    var endDateStr = (year.toString() + '-' + month.toString());

    document.getElementById("startTime").value = beginDateStr;
    document.getElementById("endTime").value = endDateStr;
    $scope.req.beginTime = document.getElementById("startTime").value + '-01';
    $scope.req.endTime = document.getElementById("endTime").value + '-01';

    $scope.dateShow = function () {
        $scope.req.beginTime = document.getElementById("startTime").value + '-01';
        $scope.req.endTime = document.getElementById("endTime").value + '-01';

        $http.post("rest/attenSummary/noNeed/list", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    $scope.summarys = data.data;
                } else {
                    showOkModal("提示", data.message);
                }
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //导出excel
    $scope.export = function () {
        $http.post("rest/attenSummary/考勤统计/export", JSON.stringify($scope.summarys), {responseType: 'blob'}).success(function (data, status, headers) {
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

    $scope.showDetails = function(index){
        $("#detailsModal").modal({show: true, keyboard: false, backdrop: 'static'});
        $scope.userName = $scope.summarys[index].userName;
        //详情
        $scope.Content=[];
        $http.get("rest/attenSummary/"+$scope.summarys[index].userId+"/"+DateToString2(new Date($scope.req.beginTime))+"/"+DateToString2(new Date($scope.req.endTime))+"/details", {}).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    $scope.Content = data.data;
                }
            }
        });
    };

    $scope.dateShow();
}]);

//考勤单项统计管理模块
indexModule.controller('attenSingleSummaryController',['$scope','$http','staffService', function ($scope, $http, staffService) {

    //加载部门信息
    var promise = staffService.getAllDeparts();
    promise.then(function (data) {
        if (data) {
            $scope.departs= data;
        }
    });

    var date=new Date;
    var year=date.getFullYear();
    if(date.getMonth()<2||(date.getMonth()==3&&date.getDate()<16)){
        document.getElementById("dateYear").value=year-1;
    } else {
        document.getElementById("dateYear").value=year;
    }

    $scope.req = {};
    $scope.selected = [];
    $scope.times = 0;
    $scope.req.depart = "null";

    $scope.req.opt='02';
    var userId = undefined;
    var userName = undefined;
    var opt = $scope.req.opt;
    $http.post("rest/attenSummary/"+document.getElementById("dateYear").value+"/"+opt+"/"+userId+"/"+userName+"/"+$scope.req.depart+"/singleList", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.singles = data.data;
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    $scope.autoQuery = function () {
        userId = undefined;
        userName = undefined;
        opt = $scope.req.opt;
        $http.post("rest/attenSummary/"+document.getElementById("dateYear").value+"/"+opt+"/"+userId+"/"+userName+"/"+$scope.req.depart+"/singleList", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.singles = data.data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //导出excel
    $scope.export = function () {
        $http.post("rest/attenSummary/单项统计/export", JSON.stringify($scope.singles ), {responseType: 'blob'}).success(function (data, status, headers) {
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

//考勤年假统计管理模块
indexModule.controller('attenAnnualSummaryController',['$scope','$http','staffService', function ($scope, $http, staffService) {

    //加载部门信息
    var promise = staffService.getAllDeparts();
    promise.then(function (data) {
        if (data) {
            $scope.departs= data;
        }
    });

    var date=new Date;
    var year=date.getFullYear();
    if (date.getMonth() < 4) {
        document.getElementById("ansYear").value = year - 1;
    } else {
        document.getElementById("ansYear").value = year;
    }

    $scope.req = {};
    $scope.selected = [];
    $scope.times = 0;
    $scope.formData = {};
    var ansYear = document.getElementById("ansYear").value;
    $scope.req.depart = "null";

    var userId = undefined;
    var userName = undefined;
    $http.post("rest/attenSummary/"+ansYear+"/01/"+userId+"/"+userName+"/"+$scope.req.depart+"/singleList", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.singles = data.data;
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    $scope.autoQuery = function () {
        userId = undefined;
        if($scope.req.userName==''){
            $scope.req.userName=undefined;
        }
        ansYear = document.getElementById("ansYear").value;
        $http.post("rest/attenSummary/"+ansYear+"/01/"+userId+"/"+$scope.req.userName+"/"+$scope.req.depart+"/singleList", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.singles = data.data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    $scope.showChangeAnnualDaysModel = function(index){
        $scope.formData = {};
        $scope.formData.userId = $scope.singles[index].staffId;
        $scope.formData.year = ansYear;
        $scope.formData.annualAll = $scope.singles[index].monthAll;
        $scope.formData.annualUse = $scope.singles[index].monthUse;
        $scope.formData.annualLeft = $scope.singles[index].monthLeft;
        $("#changeAnnualDaysModel").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    $scope.changDays = function(){
        $scope.formData.annualLeft = $scope.formData.annualAll - $scope.formData.annualUse;
    };

    $scope.submit = function(){
        $http.post("rest/attenSummary/changeAnnual", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    $("#changeAnnualDaysModel").modal('hide');
                    $scope.autoQuery();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#changeAnnualDaysModel").modal({show: true});
        });
    };

    //导出excel
    $scope.export = function () {
        $http.post("rest/attenSummary/年假统计/export", JSON.stringify($scope.singles), {responseType: 'blob'}).success(function (data, status, headers) {
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

    $scope.myKeyup = function (e) {
        var keycode = window.event?e.keyCode:e.which;
        if(keycode == 13){
            $scope.workSearch();
        }
    };
}]);


//考勤调休统计管理模块
indexModule.controller('attenOffSummaryController', ['$scope','$http','staffService',function ($scope, $http, staffService) {

    //加载部门信息
    var promise = staffService.getAllDeparts();
    promise.then(function (data) {
        if (data) {
            $scope.departs= data;
        }
    });

    var date=new Date;
    var year=date.getFullYear();
    var month=date.getMonth()+1;
    var nowDate=date.getDate().toString();
    if(nowDate.length==1){
        nowDate = "0"+nowDate;
    }
    var mydate = (month.toString()+''+nowDate);
    if(date.getMonth()<2||(date.getMonth()==3&&date.getDate()<16)){
        document.getElementById("offYear").value=year-1;
    } else {
        document.getElementById("offYear").value=year;
    }

    $scope.req = {};
    $scope.selected = [];
    $scope.times = 0;
    $scope.req.depart = "null";

    var userId = undefined;
    var userName = undefined;
    $http.post("rest/attenSummary/"+document.getElementById("offYear").value+"/00/"+userId+"/"+userName+"/"+$scope.req.depart+"/singleList", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.singles = data.data;
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    $scope.autoQuery = function () {
        userId = undefined;
        userName = undefined;
        year = document.getElementById("offYear").value;
        $http.post("rest/attenSummary/"+year+"/00/"+userId+"/"+userName+"/"+$scope.req.depart+"/singleList", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.singles = data.data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    $scope.clearUpYear = function (index) {
        var staffId = $scope.singles[index].staffId;
        $http.post("rest/overOff/"+year+"-05/"+staffId+"/clearLastDay", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    showOkModal('提示', data.message);
                } else {
                    showOkModal('错误', data.message);
                }
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    $scope.clearDownYear = function (index) {
        var staffId = $scope.singles[index].staffId;
        $http.post("rest/overOff/"+year+"-09/"+staffId+"/clearLastDay", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    showOkModal('提示', data.message);
                } else {
                    showOkModal('错误', data.message);
                }
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };
    $scope.months=false;
    if(mydate>"510"&&mydate<"530"){
        $scope.months=true;
    }else if((mydate>"915"&&mydate<"931")||(mydate>"1000"&&mydate<"1016")){
        $scope.months=true;
    }else{
        $scope.months=false;
    }
    if(mydate>"510"&&mydate<"530"){
        $scope.threes=true;
    }else{
        $scope.threes=false;
    }
    if((mydate>"915"&&mydate<"931")||(mydate>"1000"&&mydate<"1016")){
        $scope.eights=true;
    }else {
        $scope.eights=false;
    }

    //导出excel
    $scope.export = function () {
        $http.post("rest/attenSummary/调休统计/export", JSON.stringify($scope.singles), {responseType: 'blob'}).success(function (data, status, headers) {
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

    $scope.showOverDetails = function (index,month){
        var over = '$scope.singles[index].over'+month;
        if(eval(over)>0){
            $("#overOffDetailsModal").modal({show: true, keyboard: false, backdrop: 'static'});
            $scope.userName = $scope.singles[index].name;
            //加班详情
            $scope.Content=[];
            $http.get("rest/attenSummary/"+$scope.singles[index].staffId+"/"+document.getElementById("offYear").value+"/"+month+"/overDetails", {}).success(function (data) {
                if (data != null && data != undefined) {
                    if (data.resultCode == "RESULT_SUCCESS") {
                        $scope.Content = data.data;
                    }
                }
            });
        }
    };

    $scope.showOffDetails = function (index,month){
        var off = '$scope.singles[index].month'+month;
        if(eval(off)>0){
            $("#overOffDetailsModal").modal({show: true, keyboard: false, backdrop: 'static'});
            $scope.userName = $scope.singles[index].name;
            //调休详情
            $scope.Content=[];
            $http.get("rest/attenSummary/"+$scope.singles[index].staffId+"/"+document.getElementById("offYear").value+"/"+month+"/offDetails", {}).success(function (data) {
                if (data != null && data != undefined) {
                    if (data.resultCode == "RESULT_SUCCESS") {
                        $scope.Content = data.data;
                    }
                }
            });
        }
    };

}]);