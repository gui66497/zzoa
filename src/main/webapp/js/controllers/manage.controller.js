'use strict';

//出差管理模块
indexModule.controller('manageController',['$scope', '$rootScope', '$http', '$q', 'NgTableParams', '$timeout','$filter', 'staffService', '$cookieStore', function ($scope, $rootScope, $http, $q, NgTableParams,$timeout, $filter, staffService, $cookieStore) {
    $scope.collectShow=function () {
        self.tableParams.reload();
        self.tableParams._params.page=1;
    };

    moment.locale('zh-cn');

    var self = this;

    $scope.tableData = {};

    $scope.req = {};
    $scope.req.applyTyp='all';
    $scope.req.finish = true;
    $scope.req.depart = undefined;
    function initialRequest() {
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }

    $scope.req.startTime = staffService.getStartTime();
    $scope.req.endTime = staffService.getEndTime();
    /***日期范围选择控件 开始 ****/
    var start = moment(staffService.getStartTime());
    var end =  moment(staffService.getEndTime());
    function rangeShow(start, end) {
        $('#manage_range span').html(start.format('MMMM D, YYYY') + ' ~ ' + end.format('MMMM D, YYYY'));
    }
    $('#manage_range').daterangepicker({
        startDate: start,
        endDate: end,
        ranges: {
            '今天': [moment(), moment()],
            '本周': [moment().startOf('week'), moment().endOf('week')],
            '本月': [moment().startOf('month'), moment().endOf('month')],
            '上周': [moment().subtract(1, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
            '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
            '今年': [moment().startOf('year'), moment().endOf('year')]
            /*'过去七天': [moment().subtract(6, 'days'), moment()],
            '过去30天': [moment().subtract(29, 'days'), moment()],*/
        }
    }, rangeShow);
    rangeShow(start, end);
    /***日期范围选择控件 结束 ****/


    //获取部门信息
    var isHr = false;
    // 部门的种类
    $http.get("rest/depart/allList", null).success(function (data) {
        if (data != null) {
            // 20190916 fangguitang 一级二级三级领导 只能查看自己部门的信息
            if ($rootScope.permissionArr.indexOf("人事管理") > -1 || $rootScope.permissionArr.indexOf("查看全部考勤汇总") > -1) {
                isHr = true;
                $scope.allList = data;
            } else if ($rootScope.permissionArr.indexOf("查看考勤汇总") > -1) {
                // 获取所在部门
                var showDepart = [];

                staffService.getUserAuthorityByStaffId($cookieStore.get("loginUserId")).then(function (response) {
                    console.log(response);
                    if (response.resultCode === "RESULT_SUCCESS" && response.data.length > 0) {
                        var authorityDeparts = response.data.map(function (d) {
                            return d.departId;
                        });
                        for (var index in data) {
                            var departId = data[index].departId;
                            if (authorityDeparts.indexOf(departId) > -1) {
                                showDepart.push(data[index]);
                            }
                        }
                        $scope.allList = showDepart;
                        $scope.req.depart = showDepart[0].departName;
                        self.tableParams.reload();
                    }
                });
            }

        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    $scope.selectChange = function () {
        self.tableParams.reload();
    };

    //form表单
    $scope.formData = {};

    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            //清空请求
            initialRequest();
            var allParams = params.url();

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;

            console.log("req的值为：", $scope.req);
            var rangeInput = $('#manage_range').val();
            var beginDate = rangeInput.substring(0, rangeInput.indexOf("~") - 1);
            var endDate = rangeInput.substring(rangeInput.indexOf("~") + 2);
            //var beginDate = document.getElementById("startManageMonth").value;
            //var endDate = document.getElementById("endManageMonth").value;
            var userName = document.getElementById("userName").value;
            var applyTyp = $scope.req.applyTyp;
            var depart = $scope.req.depart;
            var finish = $scope.req.finish;
            if(beginDate==''){
                beginDate = undefined;
            }
            if(endDate==''){
                endDate = undefined;
            }
            if(userName==''){
                userName = undefined;
            }
            if(applyTyp==''){
                applyTyp = "all";
            }
            if(depart==''){
                depart = undefined;
            }
            var promise = $http.post("rest/mainDesk/"+beginDate+"/"+endDate+"/"+userName+"/"+applyTyp+"/"+depart+"/"+finish+"/commitPage", $scope.req);
            return promise.then(function (resut) {
                var response = resut.data;
                var total = response.otherData[0];
                params.total(total);
                $scope.tableData = response.data;
                if($scope.tableData.length==0){
                    $scope.noContent=false;
                    $scope.noNum=true
                }else{
                    $scope.noContent=true;
                    $scope.noNum=false
                }
                return response.data;
            });
        }
    });


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

    /*//上周
    $scope.lastWeek=function () {
        document.getElementById("startManageMonth").value =  formatDate(new Date(nowYear, nowMonth, nowDay - nowDayOfWeek -7));
        document.getElementById("endManageMonth").value =  formatDate(new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek - 7)));
        if(document.getElementById("startManageMonth").value){
            $scope.collectShow();
        }
    };
    //上月
    $scope.lastMonth=function () {
        document.getElementById("startManageMonth").value = formatDate(new Date(nowYear, lastMonth, 1));
        document.getElementById("endManageMonth").value = formatDate(new Date(nowYear, lastMonth, getMonthDays(lastMonth)));
        if(document.getElementById("startManageMonth").value){
            $scope.collectShow();
        }
    };
    //本周
    $scope.thisWeek=function () {
        document.getElementById("startManageMonth").value = formatDate(new Date(nowYear, nowMonth, nowDay - nowDayOfWeek));
        document.getElementById("endManageMonth").value = formatDate(new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek)));
        if(document.getElementById("startManageMonth").value){
            $scope.collectShow();
        }
    };
    //本月
    $scope.thisMonth=function () {
        document.getElementById("startManageMonth").value = formatDate(new Date(nowYear, nowMonth, 1));
        document.getElementById("endManageMonth").value = formatDate(new Date(nowYear, nowMonth, getMonthDays(nowMonth)));
        if(document.getElementById("startManageMonth").value){
            $scope.collectShow();
        }
    };*/

    $scope.billStyles=true;
    $scope.m = [];
    $scope.selectOne=function (select) {
        $scope.checked=[];
        angular.forEach($scope.m , function (row, $index) {
            var index = $scope.checked.indexOf($index);
            if(row && index === -1) {
                $scope.checked.push($scope.tableData[$index].id+':'+$scope.tableData[$index].applyTyp);
                if($scope.tableData[$index].applyTyp=='trip1'){
                    $scope.checked.splice(index, 1);
                    $scope.m[$index]=false;
                    alert("未核销不能选择下载！")
                }
                /*if($scope.checked.length>10){
                    $scope.checked.splice(index, 1);
                    $scope.m[$index]=false;
                    alert('每次选择不能超过10条数据！')
                }*/
            } else if (!row && index !== -1){
                $scope.checked.splice(index, 1);
            };
        });
        if($scope.checked.length!='0'){
            $scope.disBill=true;
            $scope.billStyles=false;
        }else{
            $scope.disBill=false;
            $scope.billStyles=true;
        }
        $scope.downBill=function () {
            var checkedBill='';
            for(var i=0; i<$scope.checked.length;i++){
                checkedBill+=$scope.checked[i]+'|'
            }
            var lastBill=checkedBill.substring(0,checkedBill.length-1);
            $scope.hideFrom=true;
            $http.post("rest/mainDesk/"+ lastBill +"/export", JSON.stringify($scope.singles), {responseType: 'blob'}).success(function (data, status, headers) {
                var blob = new Blob([data], {type: "application/vnd.ms-excel"});
                var fileName = headers()["filename"];
                fileName = decodeURI(fileName);
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = fileName;
                a.href = URL.createObjectURL(blob);
                a.click();

                $scope.hideFrom=false;
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        }
    };

    // 管理汇总信息的查看
    $scope.collectHistory=function (index) {
        $scope.formData = {};
        var Id=$scope.tableData[index].id;
        $scope.departId = $scope.tableData[index].departId;
        $scope.applyTyp = $scope.tableData[index].applyTyp;
        var url = "";
        if($scope.applyTyp=="punch"){
            url = "rest/mainDesk/"+Id+"/getPunchInfo";
        } else if($scope.applyTyp=="over"){
            url = "rest/mainDesk/"+Id+"/getOverInfo";
        } else if($scope.applyTyp=="out"){
            url = "rest/mainDesk/"+Id+"/getOutInfo";
        } else if ($scope.applyTyp.indexOf("trip") > -1) {
            url = "rest/mainDesk/"+Id+"/getTripInfo";
        } else if ($scope.applyTyp=="vacation") {
            url = "rest/mainDesk/"+Id+"/getVacationInfo";
        }
        $http.get(url, {}).success(function (data) {
            console.log("data:", data);
            if (data != null && data != undefined) {
                $scope.formData=data;
                console.log($scope.formData)
                $scope.detain=true;
                var modalName = ($scope.applyTyp.indexOf('trip') > -1) ? "tripModal2" : $scope.applyTyp + "Modal2";
                $("#"+modalName).modal({show: true, keyboard: false, backdrop: 'static'});
                if($scope.formData.leaveTyp=="00"){
                    $scope.formData.leaveTyp="调休"
                }else if($scope.formData.leaveTyp=="01"){
                    $scope.formData.leaveTyp="年假"
                }else if($scope.formData.leaveTyp=="02"){
                    $scope.formData.leaveTyp="事假"
                }else if($scope.formData.leaveTyp=="03"){
                    $scope.formData.leaveTyp="产假"
                }else if($scope.formData.leaveTyp=="04"){
                    $scope.formData.leaveTyp="婚假"
                }else if($scope.formData.leaveTyp=="05"){
                    $scope.formData.leaveTyp="丧假"
                }else if($scope.formData.leaveTyp=="06"){
                    $scope.formData.leaveTyp="病假"
                }else if($scope.formData.leaveTyp=="07"){
                    $scope.formData.leaveTyp="工程调休"
                }else if($scope.formData.leaveTyp=="08"){
                    $scope.formData.leaveTyp="产前假"
                }

                if($scope.formData.outPart=="mor"){
                    $scope.formData.outPart="上午"
                }else if($scope.formData.outPart=="all"){
                    $scope.formData.outPart="全天"
                }else{
                    $scope.formData.outPart="下午"
                }
            }
        });
    };

    $scope.myKeyup = function (e) {
        var keycode = window.event?e.keyCode:e.which;
        if(keycode == 13){
            $scope.collectShow();
        }
    };

    $scope.viewHistory=function (index) {
        var actId=$scope.tableData[index].actId;
        window.open('viewHistory.html?actId='+actId,'','width=800,height=400,left=500,top=200,scrollbars=yes,status=yes')
    };

    //出差部分
    $scope.setTripInfo = function (index) {
        var id=$scope.tableData[index].id;
        $http.get("rest/trip/"+id+"/load", {}).success(function (data) {
            if (data != null && data != undefined) {
                $scope.tripFormData = data;
                $("#showTripFormModal1").modal({show: true, keyboard: false, backdrop: 'static'});
            }
        });

    }
    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    $scope.formatterTime = function () {
        if ($scope.tripFormData.tripDate) {
            $scope.tripFormData.tripDate = DateToString2(new Date($scope.tripFormData.tripDate));
        }
        if ($scope.tripFormData.backDate) {
            $scope.tripFormData.backDate = DateToString2(new Date($scope.tripFormData.backDate));
        }
        if($scope.tripFormData.tripDate==$scope.tripFormData.backDate){
            if($scope.tripFormData.tripPart==1){
                $scope.tripFormData.backPart=1
            }
            if($scope.tripFormData.backPart==0){
                $scope.tripFormData.tripPart=0
            }
        }
    };

    //根据出差信息计算日期
    $scope.changDate = function (){
        $scope.formatterTime();
        $http.post("rest/trip/init", $scope.tripFormData).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    $scope.tripFormData = data.data[0];
                } else {
                    $scope.tripFormData = data.data[0];
                    $("#serverErrorModal").modal({show: true});
                }
            }
        });
    };

    $scope.submitTripInfo = function () {
        if($scope.tripFormData.tripDate>$scope.tripFormData.backDate){
            showOkModal('提示', "开始日期不能高于结束日期！");
        } else {
            $scope.hideFrom=true;
            $http.post("rest/common/"+$scope.tripFormData.tripDate+"/"+$scope.tripFormData.backDate+"/"+$scope.tripFormData.tripPart+"/"+$scope.tripFormData.backPart+"/"+$scope.tripFormData.userId+"/"+$scope.tripFormData.id+"/check",{}).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        $http.post("rest/trip/hrSave", $scope.tripFormData).success(function (data) {
                            if (data != null && data != undefined) {
                                $scope.hideFrom=false;
                                if (data.resultCode == "RESULT_SUCCESS") {
                                    $("#showTripFormModal1").modal('hide');
                                    self.tableParams.reload();
                                } else {
                                    showOkModal('提示', data.message);
                                }
                            }
                        }).error(function (data) {
                            $scope.hideFrom=false;
                            $("#showTripFormModal1").modal({show: true});
                        });
                    }else {
                        $scope.hideFrom=false;
                        showOkModal('提示', data.message);
                    }
                }
            });
        }
    }

}]);

