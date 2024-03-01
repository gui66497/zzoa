'use strict';

//假期管理模块
indexModule.controller('vacationController', ['$scope', '$http', '$q', 'NgTableParams', '$timeout', 'staffService', '$filter', '$cookieStore', '$rootScope', function ($scope, $http, $q, NgTableParams, $timeout, staffService, $filter, $cookieStore, $rootScope) {
    //列表下的分页
    moment.locale('zh-cn');

    var self = this;

    $scope.tableData = {};

    $scope.req = {};
    function initialRequest() {
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }

    //根据部门名称判断是否是研发中心
    console.log($cookieStore.get("loginDepartName"));
    $scope.isYanfa = $cookieStore.get("loginDepartName").indexOf("研发") > -1;

    $scope.req.startTime = staffService.getStartTime();
    $scope.req.endTime = staffService.getEndTime();
    /***日期范围选择控件 开始 ****/
    var start = moment(staffService.getStartTime());
    var end =  moment(staffService.getEndTime());
    function rangeShow(start, end) {
        $('#vacation_range span').html(start.format('MMMM D, YYYY') + ' ~ ' + end.format('MMMM D, YYYY'));
    }
    $('#vacation_range').daterangepicker({
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

            var promise = $http.post("rest/vacation/page", $scope.req);
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

    //按日期查询
    $scope.search = function () {
        var rangeInput = $('#vacation_range').val();
        $scope.req.startTime = rangeInput.substring(0, rangeInput.indexOf("~") - 1);
        $scope.req.endTime = rangeInput.substring(rangeInput.indexOf("~") + 2);
        self.tableParams.reload();
        self.tableParams._params.page = 1;
    };

    //新增请假信息，完成初始化信息
    $scope.addAndUpdate = function (index) {
        $scope.up=true;
        $scope.id=false;
        $scope.formData = {};
        if($scope.formData.leaveTyp==undefined||$scope.formData.leaveTyp==''){
            $scope.vShow=false;
            $scope.vDis=true;
        }
        if (index != undefined && ($scope.tableData[index].status=='00'||$scope.tableData[index].status=='03')) {
            $scope.formData = angular.copy($scope.tableData[index]);
            $("#AddAndUpdateVacationModal").modal({show: true, keyboard: false, backdrop: 'static'});
        } else if (index != undefined && ($scope.tableData[index].status=='01'||$scope.tableData[index].status=='02'||$scope.tableData[index].status=='98'||$scope.tableData[index].status=='99')) {
            showOkModal('提示', "处理中或已通过的流程不能修改！");
        } else {
            let loginDepartName = $cookieStore.get("loginDepartName");
            if (loginDepartName.indexOf("研发") > -1) {
                showOkModal('提示', "研发中心请前往新OA系统提交考勤申请！");
                return;
            }
            $http.get("rest/vacation/getId", {}).success(function (data) {
                $scope.formData.id=data;
                $("#AddAndUpdateVacationModal").modal({show: true, keyboard: false, backdrop: 'static'});
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        }

    };

    //销假
    $scope.forVacation = function (index) {
        if (index != undefined && $scope.tableData[index].status=='99') {
            $scope.formData = angular.copy($scope.tableData[index]);
            $scope.formData.backDateFor = $scope.formData.backDate;
            $scope.formData.backPartFor = $scope.formData.backPart;
            $scope.formData.leaveDayFor = $scope.formData.leaveDay;
            $("#forVacationModal").modal({show: true, keyboard: false, backdrop: 'static'});
        } else {
            showOkModal('提示', "只能操作已通过的流程！");
        }
    };

    //保存
    $scope.saveAndSubmit = function () {
        var option=document.getElementById("leaveTyp").value;
        var date = new Date;
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        month = (month < 10 ? "0" + month : month);
        var day = date.getDate();
        var pitch=document.getElementById("leaveDate").value;
        var pitchDay=pitch.substr(pitch.lastIndexOf("-")+1);
        var pitchYear=pitch.substr(pitch,4);
        var monthDay=pitch.substring(pitch.indexOf('-') + 1);
        var pitchMonth=monthDay.substring(0,monthDay.indexOf('-'));
        /*if(option=="00"){
            if(day>15){
                if(year==pitchYear && month==pitchMonth){
                    if(pitchDay>=16){

                    }else{
                        showOkModal("提示", "结算日外不准申请");
                        $scope.formData=''
                    }
                }else if(year==pitchYear && (pitchMonth-1)==month){
                    if(pitchDay<=15){

                    }else{
                        showOkModal("提示", "结算日外不准申请");
                        $scope.formData=''
                    }
                }else{
                    showOkModal("提示", "时间超出太多，不能申请");
                    $scope.formData=''
                }
            }
            if(day<16){
                if(year==pitchYear && month==pitchMonth){
                    if(pitchDay<=15){

                    }else{
                        showOkModal("提示", "结算日外不准申请");
                        $scope.formData=''
                    }
                }else if(year==pitchYear && pitchMonth==(month-1)){
                    if(pitchDay>=16){

                    }else{
                        showOkModal("提示", "结算日外不准申请");
                        $scope.formData=''
                    }
                }else{
                    showOkModal("提示", "时间超出太多，不能申请");
                    $scope.formData=''
                }
            }
        }*/
        if(option=='01'){
            if(month<4 && day<16){
                if(pitchMonth>2 && pitchDay>15 && year==pitchYear){
                    showOkModal("提示", "不能跨年请");
                    $scope.formData=''
                }
            }
        }
        $scope.hideFrom=true;
        $http.post("rest/common/"+$scope.formData.leaveDate+"/"+$scope.formData.backDate+"/"+$scope.formData.leavePart+"/"+$scope.formData.backPart+"/check",{}).success(function (data){
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    $http.post("rest/vacation/save", $scope.formData).success(function (data) {
                        if (data != null && data != undefined) {
                            if(data.resultCode=="RESULT_SUCCESS") {
                                var staffId = "undefined";
                                if(data.data.length<=1){
                                    if(data.data.length==1){
                                        staffId = data.data[0].staffId;
                                        var applyName=data.data[0].name;
                                    }
                                    $http.post("rest/vacation/"+ staffId +"/submit", $scope.formData).success(function (data) {
                                        $scope.hideFrom=false;
                                        if (data != null && data != undefined) {
                                            if(data.resultCode=="RESULT_SUCCESS") {
                                                $("#AddAndUpdateVacationModal").modal('hide');
                                                if(applyName!=undefined){
                                                    showOkModal('提示', "提交成功，下一审批人为&nbsp;&nbsp;&nbsp;"+applyName);
                                                }
                                                self.tableParams.reload();
                                            } else {
                                                showOkModal('提示', data.message);
                                            }
                                        }
                                    });
                                }else if(data.data.length>1){
                                    $scope.hideFrom=false;
                                    $scope.formData.processors = data.data;
                                    self.tableParams.reload();
                                    var chooseId = $scope.formData.processor;
                                    if(chooseId==undefined){
                                        $scope.disSub=true;
                                    }
                                    $("#AddAndUpdateVacationModal").modal('hide');
                                    $("#vacationChooseModal").modal({show: true, keyboard: false, backdrop: 'static'});
                                }

                            } else {
                                $scope.hideFrom=false;
                                showOkModal('提示', data.message);
                            }
                        }
                    }).error(function (data) {
                        $scope.hideFrom=false;
                        $("#AddAndUpdateVacationModal").modal({show: true});
                    });
                }else {
                    $scope.hideFrom=false;
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    $scope.radioChange=function () {
        var chooseId = $scope.formData.processor;
        if(chooseId!==undefined){
            $scope.disSub=false;
        }
    };

    $scope.submitVacation=function () {
        $scope.hideFrom=true;
        var staffId = $scope.formData.processor;
        var name=document.getElementById("vacation"+staffId).innerText;
        $http.post("rest/vacation/"+ staffId +"/submit", $scope.formData).success(function (data) {
            $scope.hideFrom=false;
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    $("#vacationChooseModal").modal('hide');
                    showOkModal('提示', "提交成功，下一审批人为&nbsp;&nbsp;&nbsp;"+name);
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    //保存
    $scope.forSubmit = function () {
        $scope.hideFrom=true;
        $http.post("rest/vacation/forVacation", $scope.formData).success(function (data) {
            $scope.hideFrom=false;
            if (data != null && data != undefined) {
                $("#forVacationModal").modal('hide');
                self.tableParams.reload();
            } else {
                showOkModal('提示', data.message);
            }
        }).error(function (data) {
            $scope.hideFrom=false;
            $("#forVacationModal").modal({show: true});
        });
    }

    //弹出确认撤销模态框
    $scope.showRevokeVacation = function (index) {
        if($scope.tableData[index].status=='00'||$scope.tableData[index].status=='03'){
            var actId = $scope.tableData[index].actId;
            $http.delete("rest/vacation/" + actId + "/revoke").success(function (response) {
                if (response.resultCode == "RESULT_SUCCESS") {
                    self.tableParams.reload();
                    $("#confirmModal").modal('hide');
                }
            }
            ).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        } else {
            showOkModal('提示', "只能撤销待发起或已打回的流程！");
        }
    };

    //弹出确认删除模态框
    $scope.showDeleteVacation = function (index) {
        var time=$scope.tableData[index].backDate.replace(/-/g,'');
        var date=new Date;
        var year=date.getFullYear();
        var month=date.getMonth()+1;
        month =(month<10 ? "0"+month:month);
        var day = date.getDate();
        day = (day<10 ? "0"+day:day);
        var myMonth = (year.toString()+month.toString());
        var inputDate=time.substr(-2);
        var inputMonth=time.substr(0, 6);
        if (!$rootScope.ignoreDeleteLimit) {
            if (inputMonth === myMonth && inputDate <= 15 && day > 18 || inputMonth < myMonth && day > 18 || inputMonth < myMonth - 1 || day <= 15 && inputMonth == myMonth - 1 && inputDate <= 15) {
                showOkModal('提示', "不能删除结算日之前的流程");
                return;
            }
        }
        var actId = $scope.tableData[index].actId;
        showConfirmModal("删除后，此事项将作废，是否删除？", function () {
            $http.delete("rest/vacation/" + actId + "/delete").success(function (response) {
                    if (response.resultCode == "RESULT_SUCCESS") {
                        self.tableParams.reload();
                        $("#confirmModal").modal('hide');
                    }
                }
            ).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        });
    };

    $scope.viewHistoryVacation = function(index){
        var actId = $scope.tableData[index].actId;
        window.open('viewHistory.html?actId='+actId,'','width=800,height=400,left=500,top=200,scrollbars=yes,status=yes')
    };

    //导出请假表单
    $scope.downLoadVacation = function (index) {
        if($scope.tableData[index].status === '98' || $scope.tableData[index].status === '97'){
            var id = $scope.tableData[index].id;
            $http.post("rest/attendance/vacation/"+ id +"/export", JSON.stringify($scope.singles), {responseType: 'blob'}).success(function (data, status, headers) {
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
        } else {
            showOkModal('提示', "审批通过并核销的单子才能导出！");
        }
    };

    //根据请假类型计算日期
    $scope.changValue = function (){
        $scope.formData.leaveDate = null;
        $scope.formData.leavePart = null;
        $scope.formData.backDate = null;
        $scope.formData.backPart = null;
        $scope.formData.leaveDay = null;
        $scope.leftDays = null;
        if($scope.formData.leaveTyp!=undefined||$scope.formData.leaveTyp!=''){
            $scope.vShow=true;
            $scope.vDis=false;
        }
        //查询剩余调休天数
        $http.get("rest/vacation/getLeftOffDays", {}).success(function (data) {
            if (data != null) {
                var leftOffDays = data;
                //查询剩余年假天数
                $http.get("rest/vacation/getLeftAnnualDays", {}).success(function (data) {
                    if (data != null) {
                        var leftAnnualDays = data;
                        console.log(leftOffDays, leftAnnualDays);
                        if($scope.formData.leaveTyp=="00"){
                            $scope.formData.reason="调休";
                            $scope.leftDays = "当前剩余调休天数 "+leftOffDays+" 天";
                        } else if($scope.formData.leaveTyp=="01"){
                            $scope.formData.reason="年假";
                            $scope.leftDays = "当前剩余年假天数 "+leftAnnualDays+" 天";
                            if ($scope.isYanfa && leftOffDays > 0) {
                                showOkModal('提示', "您还有" + leftOffDays + "天调休，请优先使用调休！");
                            }

                        } else if($scope.formData.leaveTyp=="02"){
                            $scope.formData.reason="事假";
                        } else if($scope.formData.leaveTyp=="03"){
                            $scope.formData.reason="产假";
                        } else if($scope.formData.leaveTyp=="04"){
                            $scope.formData.reason="婚假";
                        } else if($scope.formData.leaveTyp=="05"){
                            $scope.formData.reason="丧假";
                        } else if($scope.formData.leaveTyp=="06"){
                            $scope.formData.reason="病假";
                        } else if($scope.formData.leaveTyp=="07"){
                            $scope.formData.reason="工程调休";
                        } else if($scope.formData.leaveTyp=="08"){
                            $scope.formData.reason="产前假";
                        }

                    }
                });

            }
        });


    };

    //根据请假类型计算日期
    $scope.changDate = function (){
        $scope.formatterTime();
        $http.post("rest/vacation/init", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    $scope.formData = data.data[0];
                } else {
                    $scope.formData = data.data[0];
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    //根据请假类型计算日期
    $scope.changDateFor = function (){
        $scope.formatterTime();
        var backDateFor = $scope.formData.backDateFor;
        var backPartFor = $scope.formData.backPartFor;
        var leaveDayFor = $scope.formData.leaveDayFor;

        var date=new Date;
        var year=date.getFullYear();
        var month=date.getMonth()+1;
        month =(month<10 ? "0"+month:month);
        var day = date.getDate();
        day = (day<10 ? "0"+day:day);
        var mydate = (year.toString()+'-'+month.toString()+"-"+day.toString());

        if($scope.formData.backDate>mydate){
            showOkModal('提示', "销假结束日期不能晚于当前时间！");
            $scope.formData.backDate = backDateFor;
            $scope.formData.backPart = backPartFor;
            $scope.formData.leaveDay = leaveDayFor;
        } else {
            $http.post("rest/vacation/init", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    if (data.resultCode == "RESULT_SUCCESS") {
                        if(leaveDayFor>=data.data[0].leaveDay){
                            $scope.formData = data.data[0];
                        } else {
                            showOkModal('提示', "销假不能增加请假天数！");
                            $scope.formData.backDate = backDateFor;
                            $scope.formData.backPart = backPartFor;
                            $scope.formData.leaveDay = leaveDayFor;
                        }
                    } else {
                        $scope.formData.backDate = backDateFor;
                        $scope.formData.backPart = backPartFor;
                        $scope.formData.leaveDay = leaveDayFor;
                        showOkModal('提示', data.message);
                    }
                }
                $scope.formData.backDateFor = backDateFor;
                $scope.formData.backPartFor = backPartFor;
                $scope.formData.leaveDayFor = leaveDayFor;
            });
        }
    };

    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    $scope.formatterTime = function () {
        if ($scope.formData.leaveDate) {
            $scope.formData.leaveDate = DateToString2(new Date($scope.formData.leaveDate));
        }
        if ($scope.formData.backDate) {
            $scope.formData.backDate = DateToString2(new Date($scope.formData.backDate));
        }
        if($scope.formData.backDate==$scope.formData.leaveDate){
            if($scope.formData.leavePart==1){
                $scope.up=false
            }else{
                $scope.up=true
            }
            if($scope.formData.backPart==0){
                $scope.formData.leavePart=0
            }
        }
        if($scope.formData.backDate<$scope.formData.leaveDate){
            $scope.formData.backDate=null
        }
    };

    //刷新列表页面
    var refreshListView = function () {
        $scope.getVacationListBySystemId($scope.currentSystemId);
    };

}]);

//假期管理controller
