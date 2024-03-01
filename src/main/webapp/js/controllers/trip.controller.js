'use strict';

//出差管理模块
indexModule.controller('tripController', ['$scope', '$http', '$q','NgTableParams','$timeout', 'staffService', '$filter', '$rootScope', '$cookieStore', function ($scope, $http, $q, NgTableParams, $timeout, staffService, $filter, $rootScope, $cookieStore) {

    moment.locale('zh-cn');

    var self = this;

    $scope.tableData = {};

    $scope.req = {};
    function initialRequest() {
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }

    //开始日期：如果是今天1-15号则月份为上个月16号 如果是16或者之后则为当月16号
    //结束日期 开始日期月份+1 天数为当天
    $scope.req.startTime = staffService.getStartTime();
    $scope.req.endTime = staffService.getEndTime();
    /***日期范围选择控件 开始 ****/
    var start = moment(staffService.getStartTime());
    var end =  moment(staffService.getEndTime());
    function rangeShow(start, end) {
        $('#trip_range span').html(start.format('MMMM D, YYYY') + ' ~ ' + end.format('MMMM D, YYYY'));
    }
    $('#trip_range').daterangepicker({
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

            var promise = $http.post("rest/trip/page", $scope.req);
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
        var rangeInput = $('#trip_range').val();
        $scope.req.startTime = rangeInput.substring(0, rangeInput.indexOf("~") - 1);
        $scope.req.endTime = rangeInput.substring(rangeInput.indexOf("~") + 2);
        self.tableParams.reload();
        self.tableParams._params.page = 1;
    };

    //新增出差信息，完成初始化信息
    $scope.addAndUpdate = function (index) {
        $scope.formData = {};
        if (index != undefined && ($scope.tableData[index].status=='00'||$scope.tableData[index].status=='03')) {
            $scope.formData = angular.copy($scope.tableData[index]);
            $("#AddAndUpdateTripModal").modal({show: true, keyboard: false, backdrop: 'static'});
        } else if (index != undefined && ($scope.tableData[index].status=='01'||$scope.tableData[index].status=='02'||$scope.tableData[index].status=='98'||$scope.tableData[index].status=='99')) {
            showOkModal('提示', "处理中或已通过的流程不能修改！");
        }else{
            let loginDepartName = $cookieStore.get("loginDepartName");
            if (loginDepartName.indexOf("研发") > -1) {
                showOkModal('提示', "研发中心请前往新OA系统提交考勤申请！");
                return;
            }
            $http.get("rest/trip/getId", {}).success(function (data) {
                $scope.formData.id=data;
                $("#AddAndUpdateTripModal").modal({show: true, keyboard: false, backdrop: 'static'});
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        }
    };

    //销差
    $scope.forTrip = function (index) {
        if (index != undefined && $scope.tableData[index].status=='99') {
            $scope.formData = angular.copy($scope.tableData[index]);
            $("#forTripModal").modal({show: true, keyboard: false, backdrop: 'static'});
        }else {
            showOkModal('提示', "只能操作已通过的流程！");
        }
    };

    //显示图片
    $scope.showImage = function (index) {
        console.log($scope.tableData[index]);
        if($scope.tableData[index].status == "99") {
            showOkModal("提示", "该流程已通过！")
            return;
        }
        var actId = $scope.tableData[index].actId;
        $http.get("rest/trip/getImage/" + actId, {}).success(function (data) {
            console.log(data);
            if(data) {
                $("#activitiImg").attr("src", data);
                $("#imageModal").modal({show: true, keyboard: false, backdrop: 'static'});
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //保存并提交
    $scope.saveAndSubmit = function () {
        $scope.hideFrom=true;
        $http.post("rest/common/"+$scope.formData.tripDate+"/"+$scope.formData.backDate+"/"+$scope.formData.tripPart+"/"+$scope.formData.backPart+"/check",{}).success(function (data){
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    $http.post("rest/trip/save", $scope.formData).success(function (data) {
                        if (data != null && data != undefined) {
                            if (data.resultCode == "RESULT_SUCCESS") {
                                var staffId = null;
                                if(data.data.length<=1){
                                    if(data.data.length==1){
                                        staffId = data.data[0].staffId;
                                        var applyName=data.data[0].name;
                                    }
                                    $http.post("rest/trip/" + staffId + "/submit", $scope.formData).success(function (data) {
                                        $scope.hideFrom=false;
                                        if (data != null && data != undefined) {
                                            if (data.resultCode == "RESULT_SUCCESS") {
                                                $("#AddAndUpdateTripModal").modal('hide');
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
                                    $("#AddAndUpdateTripModal").modal('hide');
                                    $("#tripChooseModal").modal({show: true, keyboard: false, backdrop: 'static'});
                                }

                            } else {
                                $scope.hideFrom=false;
                                showOkModal('提示', data.message);
                            }
                        }
                    }).error(function (data) {
                        $scope.hideFrom=false;
                        $("#AddAndUpdateTripModal").modal({show: true});
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

    //选人提交
    $scope.submitTrip = function () {
        $scope.hideFrom=true;
        var staffId = $scope.formData.processor;
        var name=document.getElementById("trip"+staffId).innerText;
        $http.post("rest/trip/" + staffId + "/submit", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    $("#tripChooseModal").modal('hide');
                    showOkModal('提示', "提交成功，下一审批人为&nbsp;&nbsp;&nbsp;"+name);
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
            $scope.hideFrom=false;
        });
    };

    $scope.forTripSubmit = function () {
        var date = new Date();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + "-" + month + "-" + strDate + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        if($scope.formData.endTime>currentdate){
            showOkModal('提示', "销差结束日期不能晚于当前时间！");
        } else {
            $scope.hideFrom=true;
            $http.post("rest/common/"+$scope.formData.startTime+"/"+$scope.formData.endTime+"/"+$scope.formData.id+"/forCheck",{}).success(function (data){
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        $http.post("rest/trip/forTrip", $scope.formData).success(function (data) {
                            if (data != null && data != undefined) {
                                $scope.hideFrom=false;
                                if (data.resultCode == "RESULT_SUCCESS") {
                                    $("#forTripModal").modal('hide');
                                    self.tableParams.reload();
                                } else {
                                    showOkModal('提示', data.message);
                                }
                            }
                        }).error(function (data) {
                            $scope.hideFrom=false;
                            $("#forTripModal").modal({show: true});
                        });
                    }else {
                        $scope.hideFrom=false;
                        showConfirmModal(data.message, function () {
                            $scope.hideFrom=true;
                            $http.post("rest/trip/forTrip", $scope.formData).success(function (data) {
                                if (data != null && data != undefined) {
                                    $scope.hideFrom=false;
                                    if (data.resultCode == "RESULT_SUCCESS") {
                                        $("#forTripModal").modal('hide');
                                        self.tableParams.reload();
                                    } else {
                                        showOkModal('提示', data.message);
                                    }
                                }
                            }).error(function (data) {
                                $scope.hideFrom=false;
                                $("#forTripModal").modal({show: true});
                            });
                        });
                    }
                }
            });
        }
    };

    //弹出确认撤销模态框
    $scope.showRevokeTrip = function (index) {
        if($scope.tableData[index].status=='00'||$scope.tableData[index].status=='03'){
            var actId = $scope.tableData[index].actId;
            $http.delete("rest/trip/" + actId + "/revoke").success(function (response) {
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
    $scope.showDeleteTrip = function (index) {
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
            $http.delete("rest/trip/" + actId + "/delete").success(function (response) {
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

    //根据出差信息计算日期
    $scope.changDate = function (){
        $scope.formatterTime();
        if($scope.formData.tripDate>$scope.formData.backDate){
            $scope.formData.backDate='';
        }else{
            $http.post("rest/trip/init", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    if (data.resultCode == "RESULT_SUCCESS") {
                        $scope.formData = data.data[0];
                    } else {
                        $scope.formData = data.data[0];
                        $("#serverErrorModal").modal({show: true});
                    }
                }
            });
        }

    };

    //根据销差信息计算日期
    $scope.forChangDate = function (){
        $scope.formatterTime3();
        $http.post("rest/trip/forInit", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    $scope.formData = data.data[0];
                } else {
                    $scope.formData = data.data[0];
                    $("#serverErrorModal").modal({show: true});
                }
            }
        });
    };

    $scope.viewHistoryTrip = function(index){
        var actId = $scope.tableData[index].actId;
        window.open('viewHistory.html?actId='+actId,'','width=800,height=400,left=500,top=200,scrollbars=yes,status=yes')
    };

    /*-----------------------------------------------*/
    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };
    $scope.onTimeSet3 = function (newDate, oldDate) {
        $scope.formatterTime3();
    };

    $scope.formatterTime = function () {
        if ($scope.formData.tripDate) {
            $scope.formData.tripDate = DateToString2(new Date($scope.formData.tripDate));
        }
        if ($scope.formData.backDate) {
            $scope.formData.backDate = DateToString2(new Date($scope.formData.backDate));
        }
        if($scope.formData.tripDate==$scope.formData.backDate){
            if($scope.formData.tripPart==1){
                $scope.formData.backPart=1
            }
            if($scope.formData.backPart==0){
                $scope.formData.tripPart=0
            }
        }
    };

    $scope.formatterTime3 = function () {
        if ($scope.formData.startTime) {
            $scope.formData.startTime = DateToString3(new Date($scope.formData.startTime));
        }
        if ($scope.formData.endTime) {
            $scope.formData.endTime = DateToString3(new Date($scope.formData.endTime));
        }
    };

    //刷新列表页面
    var refreshListView = function () {
        $scope.getVacationListBySystemId($scope.currentSystemId);
    };

    //导出出差pdf
    $scope.exportTrip = function(index){
        if($scope.tableData[index].status === '98' || $scope.tableData[index].status === '97'){
            var id = $scope.tableData[index].id;
            $http.post("rest/attendance/trip/"+id+"/export", JSON.stringify($scope.singles), {responseType: 'blob'}).success(function (data, status, headers) {
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
            showOkModal('提示', "核销后才能导出！");
        }
    };

}]);

//出差管理controller
