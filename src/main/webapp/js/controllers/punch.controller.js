'use strict';

//忘打卡管理模块
indexModule.controller('punchController',['$scope', '$http', '$q', 'NgTableParams', '$timeout', 'staffService', '$filter', '$rootScope', '$cookieStore', function ($scope, $http, $q, NgTableParams, $timeout, staffService, $filter, $rootScope, $cookieStore) {

    $scope.noNum=false;
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

    $scope.req.startTime = staffService.getStartTime();
    $scope.req.endTime = staffService.getEndTime();
    /***日期范围选择控件 开始 ****/
    var start = moment(staffService.getStartTime());
    var end =  moment(staffService.getEndTime());
    function rangeShow(start, end) {
        $('#punch_range span').html(start.format('MMMM D, YYYY') + ' ~ ' + end.format('MMMM D, YYYY'));
    }
    $('#punch_range').daterangepicker({
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
            console.log("params的url内容", params.url());
            var allParams = params.url();

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;

            console.log("req的值为：", $scope.req);

            var promise = $http.post("rest/punch/page", $scope.req);
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
        var rangeInput = $('#punch_range').val();
        $scope.req.startTime = rangeInput.substring(0, rangeInput.indexOf("~") - 1);
        $scope.req.endTime = rangeInput.substring(rangeInput.indexOf("~") + 2);
        self.tableParams.reload();
        self.tableParams._params.page = 1;
    };

    $scope.forgetPunch = function (index) {
        $scope.formData = {};
        if (index != undefined && ($scope.tableData[index].status=='00'||$scope.tableData[index].status=='03')) {
            $scope.formData = angular.copy($scope.tableData[index]);
            $("#forgetPunchModal").modal({show: true, keyboard: false, backdrop: 'static'});
        } else if (index != undefined && ($scope.tableData[index].status=='01'||$scope.tableData[index].status=='02'||$scope.tableData[index].status=='99')) {
            showOkModal('提示', "处理中或已通过的流程不能修改！");
        }else{
            let loginDepartName = $cookieStore.get("loginDepartName");
            if (loginDepartName.indexOf("研发") > -1) {
                showOkModal('提示', "研发中心请前往新OA系统提交考勤申请！");
                return;
            }
            $http.get("rest/punch/getId", {}).success(function (data) {
                $scope.formData.id = data;
                $("#forgetPunchModal").modal({show: true, keyboard: false, backdrop: 'static'});
            }).error(function (data) {
            });
        }
    };

    $scope.saveAndSubmitPunch = function () {
        $scope.hideFrom=true;
        $http.post("rest/punch/save", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    var staffId = "undefined";
                    if(data.data.length<=1){
                        if(data.data.length==1){
                            staffId = data.data[0].staffId;
                            var applyName=data.data[0].name;
                        }
                        $http.post("rest/punch/" + staffId + "/submit", $scope.formData).success(function (data) {
                            $scope.hideFrom=false;
                            if (data != null && data != undefined) {
                                if (data.resultCode == "RESULT_SUCCESS") {
                                    $("#forgetPunchModal").modal('hide');
                                    if(applyName!=undefined){
                                        showOkModal('提示', "提交成功，下一审批人为&nbsp;&nbsp;&nbsp;"+applyName);
                                    }
                                    self.tableParams.reload();
                                } else {
                                    showOkModal('提示', data.message);
                                }
                            }
                        });
                    } else if (data.data.length > 1) {
                        $scope.hideFrom=false;
                        $scope.formData.processors = data.data;
                        self.tableParams.reload();
                        var chooseId = $scope.formData.processor;
                        if(chooseId==undefined){
                            $scope.disSub=true;
                        }
                        $("#forgetPunchModal").modal('hide');
                        $("#punchChooseModal").modal({show: true, keyboard: false, backdrop: 'static'});
                    }
                } else if(data.resultCode == "RESULT_NOTICE") {
                    var staffId = "undefined";
                    if(data.data.length<=1){
                        if(data.data.length==1){
                            staffId = data.data[0].staffId;
                            var applyName=data.data[0].name;
                        }
                        $http.post("rest/punch/" + staffId + "/notice", $scope.formData).success(function (data) {
                            $scope.hideFrom=false;
                            if (data != null && data != undefined) {
                                if (data.resultCode == "RESULT_SUCCESS") {
                                    $("#forgetPunchModal").modal('hide');
                                    self.tableParams.reload();
                                } else {
                                    showOkModal('提示', data.message);
                                }
                            }
                        });
                    } else if (data.data.length > 1) {
                        $scope.hideFrom=false;
                        $scope.formData.notices = data.data;
                        self.tableParams.reload();
                        var chooseId = $scope.formData.notice;
                        if(chooseId==undefined){
                            $scope.disSubN=true;
                        }
                        $("#forgetPunchModal").modal('hide');
                        $("#punchNoticeModal").modal({show: true, keyboard: false, backdrop: 'static'});
                    }
                } else {
                    $scope.hideFrom=false;
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $scope.hideFrom=false;
            $("#forgetPunchModal").modal({show: true});
        });
    };

    $scope.radioChange=function () {
        var chooseId = $scope.formData.processor;
        if(chooseId!==undefined){
            $scope.disSub=false;
        }
    };

    $scope.radioChangeN=function () {
        var chooseId = $scope.formData.notice;
        if(chooseId!==undefined){
            $scope.disSubN=false;
        }
    };

    $scope.submitPunch = function () {
        $scope.hideFrom=true;
        var staffId = $scope.formData.processor;
        var name=document.getElementById("punch"+staffId).innerText;
        $http.post("rest/punch/" + staffId + "/submit", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                $scope.hideFrom=false;
                if (data.resultCode == "RESULT_SUCCESS") {
                    $("#punchChooseModal").modal('hide');
                    showOkModal('提示', "提交成功，下一审批人为&nbsp;&nbsp;&nbsp;"+name);
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    $scope.noticePunch = function () {
        $scope.hideFrom=true;
        var staffId = $scope.formData.notice;
        var name=document.getElementById("punchn"+staffId).innerText;
        $http.post("rest/punch/" + staffId + "/notice", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                $scope.hideFrom=false;
                if (data.resultCode == "RESULT_SUCCESS") {
                    $("#punchNoticeModal").modal('hide');
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    //弹出确认撤销模态框
    $scope.showRevokePunch = function (index) {
        if($scope.tableData[index].status=='00'||$scope.tableData[index].status=='03'){
            var actId = $scope.tableData[index].actId;
            $http.delete("rest/punch/" + actId + "/revoke").success(function (response) {
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
    $scope.showDeletePunch = function (index) {
        var time=$scope.tableData[index].punchDate.replace(/-/g,'');
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
            $http.delete("rest/punch/" + actId + "/delete").success(function (response) {
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

    $scope.tgPunch = function () {
        $http.post("rest/punch/{user}/{msg}/submit", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    $("#tgPunchModal").modal('hide');
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#tgPunchModal").modal({show: true});
        });
    };

    $scope.viewHistoryPunch = function(index){
        var actId = $scope.tableData[index].actId;
        window.open('viewHistory.html?actId='+actId,'','width=800,height=400,left=500,top=200,scrollbars=yes,status=yes')
    };

    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    //导出忘打卡pdf
    $scope.downLoadPunch = function (index) {
        console.log("下载:", $scope.tableData[index]);
        if ($scope.tableData[index].status === '99' || $scope.tableData[index].status === '97') {
            var id = $scope.tableData[index].id;
            $http.post("rest/attendance/punch/"+ id +"/export", JSON.stringify($scope.singles), {responseType: 'blob'}).success(function (data, status, headers) {
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

    $scope.formatterTime = function () {
        if ($scope.formData.punchDate) {
            $scope.formData.punchDate = DateToString2(new Date($scope.formData.punchDate));
            var time=$scope.formData.punchDate.replace(/-/g,'');
            var date = new Date;
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            month = (month < 10 ? "0" + month : month);
            var date = date.getDate();
            var mydate = (year.toString() + '-' + month.toString()+'-'+date.toString());
            var nowTime=mydate.replace(/-/g,'');

            var inputDate=time.substr(-2);
            var inputMonth=time.substr(0, 6).substr(-2);
            if(time>=nowTime){
                $scope.formData.punchDate='';
                alert("请选择今天以前的日期！")
            }
            /*if(inputDate<16 && inputMonth<month && date<16||inputMonth<month-1 && date<16){
                $scope.formData.punchDate='';
                alert("上个月的不能申请！")
            };
            if(inputDate<16 && date>16 || inputMonth<month && date>16){
                $scope.formData.punchDate='';
                alert("已过结算日，不能申请!")
            };*/
        }
    };


}]);

//忘打卡管理controller
