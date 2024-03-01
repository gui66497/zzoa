'use strict';

//加班管理模块
indexModule.controller('overController',['$scope', '$http', '$q', 'NgTableParams', 'staffService', '$filter', '$rootScope', '$cookieStore', function ($scope, $http, $q, NgTableParams, staffService, $filter, $rootScope, $cookieStore) {
    //列表下的分页
    moment.locale('zh-cn');

    var self = this;

    $scope.tableData = {};

    $scope.req = {};

    //form表单
    $scope.formData = {};

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
        $('#over_range span').html(start.format('MMMM D, YYYY') + ' ~ ' + end.format('MMMM D, YYYY'));
    }
    $('#over_range').daterangepicker({
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

    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            //清空请求
            initialRequest();
            var allParams = params.url();

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;

            console.log("req的值为：", $scope.req);

            var promise = $http.post("rest/over/page", $scope.req);
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
        var rangeInput = $('#over_range').val();
        $scope.req.startTime = rangeInput.substring(0, rangeInput.indexOf("~") - 1);
        $scope.req.endTime = rangeInput.substring(rangeInput.indexOf("~") + 2);
        self.tableParams.reload();
        self.tableParams._params.page = 1;
    };

    $scope.addAndUpdateOver = function (index) {
        $scope.formData = {};
        if (index != undefined && ($scope.tableData[index].status=='00'||$scope.tableData[index].status=='03')) {
            $scope.formData = angular.copy($scope.tableData[index]);
            $("#addAndUpdateOverModal").modal({show: true, keyboard: false, backdrop: 'static'});
        } else if (index != undefined && ($scope.tableData[index].status=='01'||$scope.tableData[index].status=='02'||$scope.tableData[index].status=='99')) {
            showOkModal("提示", "处理中或已通过的流程不能修改！");
        } else {
            let loginDepartName = $cookieStore.get("loginDepartName");
            if (loginDepartName.indexOf("研发") > -1) {
                showOkModal('提示', "研发中心请前往新OA系统提交考勤申请！");
                return;
            }
            $http.get("rest/over/getId", {}).success(function (data) {
                $scope.formData.id=data;
                $("#addAndUpdateOverModal").modal({show: true, keyboard: false, backdrop: 'static'});
            }).error(function (data) {
            });
        }
    };

    $scope.saveAndSubmitOver=function () {
        $scope.hideFrom=true;
            var date = new Date;
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            month = (month < 10 ? "0" + month : month);
            var day = date.getDate();
            var pitch=document.getElementById("overDate").value;
            var pitchDay=pitch.substr(pitch.lastIndexOf("-")+1)
            var pitchYear=pitch.substr(pitch,4);
            var monthDay=pitch.substring(pitch.indexOf('-') + 1);
            var pitchMonth=monthDay.substring(0,monthDay.indexOf('-'));
            /*if(day>15){
                if(year==pitchYear && month==pitchMonth){
                    if(pitchDay>=16){

                    }else{
                        showOkModal("提示", "结算日外不准申请");
                        $scope.formData='';
                        $scope.hideFrom = false;
                    }
                }else if(year==pitchYear && (pitchMonth-1)==month){
                    if(pitchDay<=15){

                    }else{
                        showOkModal("提示", "结算日外不准申请");
                        $scope.formData='';
                        $scope.hideFrom = false;
                    }
                }else{
                    showOkModal("提示", "时间超出太多，不能申请");
                    $scope.formData='';
                    $scope.hideFrom = false;
                }
            }
            if(day<16){
                if(year==pitchYear && month==pitchMonth){
                    if(pitchDay<=15){

                    }else{
                        showOkModal("提示", "结算日外不准申请");
                        $scope.formData='';
                        $scope.hideFrom = false;
                    }
                }else if(year==pitchYear && pitchMonth==(month-1)){
                    if(pitchDay>=16){

                    }else{
                        showOkModal("提示", "结算日外不准申请");
                        $scope.formData='';
                        $scope.hideFrom = false;
                    }
                }else{
                    showOkModal("提示", "时间超出太多，不能申请");
                    $scope.formData='';
                    $scope.hideFrom = false;
                }
            }*/
        $http.post("rest/over/save", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    var staffId = "undefined";
                    if(data.data.length<=1){
                        if(data.data.length==1){
                            staffId = data.data[0].staffId;
                            var applyName=data.data[0].name;
                        }
                        $http.post("rest/over/"+ staffId +"/submit", $scope.formData).success(function (data) {
                            $scope.hideFrom=false;
                            if (data != null && data != undefined) {
                                if(data.resultCode=="RESULT_SUCCESS") {
                                    $("#addAndUpdateOverModal").modal('hide');
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
                        $("#addAndUpdateOverModal").modal('hide');
                        $("#overChooseModal").modal({show: true, keyboard: false, backdrop: 'static'});
                    }

                } else {
                    $scope.hideFrom=false;
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#addAndUpdateOverModal").modal({show: true});
        });
    };

    $scope.radioChange=function () {
        var chooseId = $scope.formData.processor;
        if(chooseId!==undefined){
            $scope.disSub=false;
        }
    };

    $scope.submitOver=function () {
        $scope.hideFrom=true;
        var staffId = $scope.formData.processor;
        var name=document.getElementById("over"+staffId).innerText;
        $http.post("rest/over/"+ staffId +"/submit", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                $scope.hideFrom=false;
                if(data.resultCode=="RESULT_SUCCESS") {
                    $("#overChooseModal").modal('hide');
                    showOkModal('提示', "提交成功，下一审批人为&nbsp;&nbsp;&nbsp;"+name);
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    //弹出确认删除模态框
    $scope.showRevokeOver = function (index) {
        if($scope.tableData[index].status=='00'||$scope.tableData[index].status=='03'){
            var actId = $scope.tableData[index].actId;
            $http.delete("rest/over/" + actId + "/revoke").success(function (response) {
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
    $scope.showDeleteOver = function (index) {
        var time=$scope.tableData[index].overDate.replace(/-/g,'');
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
            $http.delete("rest/over/" + actId + "/delete").success(function (response) {
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

    $scope.viewHistoryOver = function(index){
        var actId = $scope.tableData[index].actId;
        window.open('viewHistory.html?actId='+actId,'','width=800,height=400,left=500,top=200,scrollbars=yes,status=yes')
    };

    //导出加班申请表单
    $scope.downLoadOver = function (index) {
        if($scope.tableData[index].status === '99' || $scope.tableData[index].status === '97'){
            var id = $scope.tableData[index].id;
            $http.post("rest/attendance/over/"+ id +"/export", JSON.stringify($scope.singles), {responseType: 'blob'}).success(function (data, status, headers) {
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
            showOkModal('提示', "审批通过的单子才能导出！");
        }
    };

    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    $scope.formatterTime = function () {
        if ($scope.formData.overDate) {
            $scope.formData.overDate = DateToString2(new Date($scope.formData.overDate));
        }
    };
}]);

//加班管理controller
