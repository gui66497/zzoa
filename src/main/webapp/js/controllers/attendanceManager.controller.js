'use strict';

//attendance controller
indexModule.controller('attendanceManController',['$scope', '$rootScope', '$filter', '$http', '$q', 'NgTableParams', '$timeout','$document', 'staffService', '$cookieStore', function ($scope, $rootScope, $filter, $http, $q, NgTableParams, $timeout, $document , staffService, $cookieStore) {

    //判断是否有考勤管理的权限 如果没有就只能看自己的考勤记录
    // $scope.isManager = $rootScope.permissionArr.indexOf("考勤统计") >= 0;
    $scope.isManager = $rootScope.permissionArr.indexOf("人事管理") >= 0;

    //搜索查询
    $scope.workSearch=function () {
        var rangeInput = $('#attendance_range').val();
        $scope.req.beginTime = rangeInput.substring(0, rangeInput.indexOf("~") - 1);
        $scope.req.endTime = rangeInput.substring(rangeInput.indexOf("~") + 2);
        self.tableParams.reload();
        self.tableParams._params.page = 1;
    };

    var self = this;
    $scope.tableData = {};
    $scope.req = {};
    function initialRequest() {
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }

    $scope.req.beginTime = staffService.getStartTime();
    $scope.req.endTime = staffService.getEndTime();
    /***日期范围选择控件 开始 ****/
    var start = moment(staffService.getStartTime());
    var end =  moment(staffService.getEndTime());
    function rangeShow(start, end) {
        $('#attendance_range span').html(start.format('MMMM D, YYYY') + ' ~ ' + end.format('MMMM D, YYYY'));
    }
    $('#attendance_range').daterangepicker({
        startDate: start,
        endDate: end,
        ranges: {
            '今天': [moment(), moment()],
            '本周': [moment().startOf('week'), moment().endOf('week')],
            '本月': [moment().startOf('month'), moment().endOf('month')],
            '上周': [moment().subtract(1, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
            '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
            '今年': [moment().startOf('year'), moment().endOf('year')]
        }
    }, rangeShow);
    rangeShow(start, end);
    /***日期范围选择控件 结束 ****/

    if (!$scope.isManager) {
        $scope.req.userName = $cookieStore.get("loginName");
    }

    var checkedDeparts = ["东部南部销售中心", "人事行政部", "质量(项目)管理部", "商务财务部", "无锡研发中心", "系统集成部"];
    function loadStaffs(){
        $http.get("rest/depart/allList", null).success(function (data) {
            if (data != null && data != undefined) {

                //普通用户不需要选中部门 人事需要默认选中无锡的部门
                if ($scope.isManager) {
                    for (var i=0;i<data.length;i++) {
                        if (checkedDeparts.indexOf(data[i].departName) > -1) {
                            data[i].ticked = true;
                        }
                    }
                }
                $scope.sections= data;
                $scope.departSections = data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }
    loadStaffs();

    //form表单
    $scope.formData = {};

    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            //清空请求
            initialRequest();
            var allParams = params.url();

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;

            var departArr = [];
            angular.forEach($scope.selectedDeparts, function (depart) {
                departArr.push(depart.departName);
            });
            $scope.req.departs = departArr;

            var promise = $http.post("rest/attendance/page", $scope.req);
            return promise.then(function (resut) {
                var response = resut.data;
                var total = response.otherData[0];
                params.total(total);
                $scope.tableData = response.data;
                return response.data;

            });
        }
    });
    $timeout(function () {
        $scope.workSearch();
    },200);
    var now = new Date();                    //当前日期
    var nowDayOfWeek = now.getDay();         //今天本周的第几天
    var nowDay = now.getDate();              //当前日
    var nowMonth = now.getMonth();           //当前月
    var nowYear = now.getYear();             //当前年
    nowYear += (nowYear < 2000) ? 1900 : 0;  //

    var lastMonthDate = new Date();  //上月日期
    lastMonthDate.setDate(1);
    lastMonthDate.setMonth(lastMonthDate.getMonth()-1);
    var lastMonth = lastMonthDate.getMonth();

    //获得某月的天数
    function getMonthDays(myMonth){
        var monthStartDate = new Date(nowYear, myMonth, 1);
        var monthEndDate = new Date(nowYear, myMonth + 1, 1);
        var   days   =   (monthEndDate   -   monthStartDate)/(1000   *   60   *   60   *   24);
        return   days;
    }

    //上周
    $scope.lastWeek = function () {
        $scope.req.beginTime = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek -7, 8);
        $scope.req.endTime = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek - 7), 8);
        if($scope.req.beginTime){
            $scope.workSearch();
        }
    };
    //上月
    $scope.lastMonth = function () {
        $scope.req.beginTime = new Date(nowYear, lastMonth, 1, 8);
        $scope.req.endTime = new Date(nowYear, lastMonth, getMonthDays(lastMonth), 8);
        if($scope.req.beginTime){
            $scope.workSearch();
        }
    };
    //本周
    $scope.thisWeek = function () {
        $scope.req.beginTime = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek, 8);
        $scope.req.endTime = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek), 8);
        if($scope.req.beginTime){
            $scope.workSearch();
        }
    };
    //本月
    $scope.thisMonth = function () {
        $scope.req.beginTime = new Date(nowYear, nowMonth, 1, 8);
        $scope.req.endTime = new Date(nowYear, nowMonth, getMonthDays(nowMonth), 8);
        if($scope.req.beginTime){
            $scope.workSearch();
        }
    };

    //多选框默认文本替换
    $scope.localLang = {
        nothingSelected : "请选择部门"
    };

    /*替换原生的选择文件按钮事件*/
    $scope.getFileClick = function(){
        $document[0].getElementById("file_btn").click();
    };

    /**
     * 上传
     */
    $scope.upload = function () {
        var files = document.getElementsByName('file')[0].files;
        if (files.length < 1) {
            showOkModal("提示信息", "请先选择文件！");
            return;
        }
        $http({
            method: 'POST',
            url: 'rest/attendance/upload',
            headers: {
                'Content-Type': undefined
            },
            data: {
                file: files[0]
            },
            transformRequest: function (data) {
                var formData = new FormData();
                formData.append('file', data.file);
                return formData;
            }
        }).success(function (data) {
            //请求成功
            showOkModal('提示', data.message);
            $("#inputLeadModal").modal('hide');
            self.tableParams.reload();
        }).error(function (err, status) {
            console.log(err);
        });
    };


    if($scope.req.isError!="0" && $scope.req.isError!="1"){
        $scope.selectWork={
            "color":"rgb(117, 116, 116)"
        }
    }
    $scope.alter=function () {
        if($scope.req.isError!="0" && $scope.req.isError!="1"){
            $scope.selectWork={
                "color":"rgb(117, 116, 116)"
            }
        }else if($scope.req.isError=="0"|| $scope.req.isError=="1"){
            $scope.selectWork={
                "color":"black"
            }
        }
    };

    //显示输入检测事项模态框
    $scope.showInputAttModal = function () {
        $("#inputAttModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //执行输入检测事项
    $scope.inputChecked = function () {
        $scope.formData.checkDate =$filter("date")($scope.formData.checkDate,'yyyy-MM-dd');
        var time = $scope.formData.time;
        var opt = $scope.formData.opt;
        if (time=='1'){
            $scope.formData.morDay = opt;
        } else if (time=='2'){
            $scope.formData.aftDay = opt;
        } else if (time=='3'){
            $scope.formData.eveDay = opt;
        } else {
            $scope.formData.allDay = opt;
        }
        $scope.formData.checkDate = document.getElementById("addDate").value;
        $http.post("rest/attendance/input", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "添加成功！");
                    $("#inputAttModal").modal('hide');

                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //清除事项
    $scope.removal = function (index) {
        $http.post("rest/attendance/deleteAttInfo", $scope.tableData[index]).success(
            function (data) {if (data != null && data != undefined) {
                $scope.tableData.splice($scope.tableData.allDay);
                $scope.tableData.splice($scope.tableData.morDay);
                $scope.tableData.splice($scope.tableData.aftDay);
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "删除成功！");
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
            }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //显示添加签到时间模态框
    $scope.showInputSignModal = function () {
        $("#inputSignModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    
    $scope.editSignTime = function(index) {
        var userId = $scope.tableData[index].userId;
        var checkDate = $scope.tableData[index].checkDate;
        document.getElementById('inputTime').value=$scope.tableData[index].signTime;
        $scope.formData.inputTime = document.getElementById('inputTime').value;
        $("#inputTimeModal").modal({show: true, keyboard: false, backdrop: 'static'});
        $scope.inputTimeSubmit = function () {
            $scope.formData.userId = userId;
            $scope.formData.checkDate = checkDate;
            $scope.formData.signTime = document.getElementById("inputTime").value;
            $http.post("rest/attendance/inputSignTime", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        showOkModal('提示', "修改成功！");
                        $("#inputTimeModal").modal('hide');
                        self.tableParams.reload();
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        }
    };
    
    $scope.editLeaveTime = function (index) {
        var userId = $scope.tableData[index].userId;
        var checkDate = $scope.tableData[index].checkDate;
        document.getElementById('inputTime').value=$scope.tableData[index].leaveTime;
        $scope.formData.inputTime = document.getElementById('inputTime').value;
        // $scope.formData.inputTime = null;
        $("#inputTimeModal").modal({show: true, keyboard: false, backdrop: 'static'});
        $scope.inputTimeSubmit = function () {
            $scope.formData.userId = userId;
            $scope.formData.checkDate = checkDate;
            $scope.formData.leaveTime = document.getElementById("inputTime").value;
            $http.post("rest/attendance/inputLeaveTime", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        showOkModal('提示', "修改成功！");
                        $("#inputTimeModal").modal('hide');
                        self.tableParams.reload();
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        }
    };

    //添加签到时间
    $scope.inputSignTime = function () {
        // $scope.formData.checkDate = new Date($scope.formData.checkDate);
        // $scope.formData.checkDate=$filter("date")($scope.formData.checkDate,'yyyy-MM-dd');
        $scope.formData.checkDate = document.getElementById("signDate").value;
        $scope.formData.leaveTime = document.getElementById("signTime").value;
        $http.post("rest/attendance/inputSignTime", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "添加成功！");
                    $("#inputSignModal").modal('hide');
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //显示添加签退时间模态框
    $scope.showInputLeaveModal = function () {
        $("#inputLeaveModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    //显示考勤导入模态框
    $scope.showInputLeadModal = function () {
        $("#inputLeadModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    $scope.recordAgainDate = "";
    $scope.showWait = false;

    //弹出考勤补录
    $scope.showRecordAgainModal = function () {
        $scope.recordAgainDate = "";
        $("#recordAgainModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //考勤补录
    $scope.recordAgain = function () {
        if (!$scope.recordAgainDate) {
            showOkModal("提示", "请先选择日期！");
            return;
        }
        $scope.showWait = true;
        $http.get("rest/attendance/recordAgain/" + $scope.recordAgainDate).success(function (data) {
            $scope.showWait = false;
            if (data != null && data != undefined) {
                console.log(data);
                showOkModal("提示", data.message);
                if(data.resultCode=="RESULT_SUCCESS") {
                    $("#recordAgainModal").modal('hide');
                    self.tableParams.reload();
                }
            }
            }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };


    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    $scope.formatterTime = function () {
        if ($scope.formData.checkDate) {
            $scope.formData.checkDate = DateToString2(new Date($scope.formData.checkDate));
        }
        if ($scope.recordAgainDate) {
            $scope.recordAgainDate = DateToString2(new Date($scope.recordAgainDate));
        }
    };
    //添加签退时间
    $scope.inputLeaveTime = function () {
        $scope.formData.checkDate = document.getElementById("leaveDate").value;
        $scope.formData.leaveTime = document.getElementById("leaveTime").value;
        $http.post("rest/attendance/inputLeaveTime", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "添加成功！");
                    $("#inputLeaveModal").modal('hide');
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
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
