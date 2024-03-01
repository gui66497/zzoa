'use strict';

//turnover controller
indexModule.controller('turnoverController', ['$scope', '$http', '$q','NgTableParams','staffService',function ($scope, $http, $q, NgTableParams, staffService) {

    var self = this;

    //当前员工姓名
    $scope.nowStaffName;

    $scope.formData = {};

    $scope.showAction = false;

    $scope.advancedSearch = false;

    //$scope.typeArr = ["在职情况", "劳动合同", "人员属性", "学历", "岗位"];
    $scope.typeArr = ["入职", "转正", "岗位变动", "离职"];

    $scope.regularArr = ["正常转正","延长转正期1个月","提前转正期1个月"];

    /***日期范围选择控件 开始 ****/
    $('#turn_range').daterangepicker({
        autoUpdateInput: false,
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
    });
    $('#turn_range').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
    });
    $('#turn_range').on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });
    /***日期范围选择控件 结束 ****/

    function initialRequest() {
        $scope.req = {};
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }

    //初始化请求
    initialRequest();


    //当前选中员工id 默认为登录者的id
    $scope.selectedStaffId = getCookie('loginUserId');
    $scope.selectedStaff = {};


    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {

            var allParams = params.url();
            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;
            var promise = $http.post("rest/turnover/page", $scope.req);
            return promise.then(function (result) {
                var response = result.data;
                var total = response.otherData[0];
                params.total(total);
                $scope.tableData = response.data;
                return response.data;
            });
        }
    });

    //获取所有员工(包括离职的)
    $scope.getAllUser = function () {
        var promise = staffService.queryAllStaff("all");
        return promise.then(function (result) {
            $scope.allStaff = result;
            return result;
        });
    };

    $scope.getAllUser();

    //select框赋值
    $scope.selected = { value: $scope.allStaff };

    //select选中事件
    self.onSelectStaff = function (item, model) {
        var selectedStaffId = item.staffId;
        $scope.getStaffInfoById(selectedStaffId);
        initialRequest();
        $scope.req.staffId = selectedStaffId;
        $scope.selectedStaffId = selectedStaffId;
        self.tableParams.reload();
    };


    $scope.toAddTurnover = function () {
        $scope.formData = {};
        setFormData();
        setNowStaffName();
        angular.element("#addTurnoverModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //根据选择的员工设置岗位 职务 部门
    function setFormData () {
        $scope.formData.oldDepart = $scope.selectedStaff.departName;
        $scope.formData.newDepart = $scope.selectedStaff.departName;
        $scope.formData.oldPosition = $scope.selectedStaff.position;
        $scope.formData.newPosition = $scope.selectedStaff.position;
        $scope.formData.oldDuty = $scope.selectedStaff.duty;
        $scope.formData.newDuty = $scope.selectedStaff.duty;
    }

    //设置当前员工姓名
    function setNowStaffName() {
        angular.forEach($scope.allStaff, function (staff) {
            if ($scope.selectedStaffId == staff.staffId) {
                $scope.nowStaffName = staff.name;
                console.log($scope.allStaff);
            }
        });
    }

    //新增和修改异动事项
    $scope.addTurnover = function () {
        //修改
        if ($scope.formData.id) {
            $http.put("rest/turnover/update", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    showOkModal('提示', data.message);
                    if (data.resultCode == "RESULT_SUCCESS") {
                        self.tableParams.reload();
                        angular.element("#addTurnoverModal").modal('hide');
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });

        } else {//新增
            $scope.formData.staffId = $scope.selectedStaffId;
            $http.post("rest/turnover/add", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    showOkModal('提示', data.message);
                    if (data.resultCode == "RESULT_SUCCESS") {
                        self.tableParams.reload();
                        angular.element("#addTurnoverModal").modal('hide');
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        }

    };

    //准备修改弹框
    $scope.toEditTurnover = function (index) {
        setNowStaffName();
        var data = angular.copy($scope.tableData[index]);
        console.log("待修改：", data);
        $scope.formData = data;
        angular.element("#addTurnoverModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //删除异动信息
    $scope.toDelTurnover = function (index) {
        showConfirmModal("确定要删除吗？", function () {
            var id = $scope.tableData[index].id;
            $http.delete("rest/turnover/" + id + "/del").success(function (data) {
                if (data != null && data != undefined) {
                    if (data.resultCode == "RESULT_SUCCESS") {
                        self.tableParams.reload();
                        angular.element("#addTurnoverModal").modal('hide');
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        });
    };

    //根据staffId获取员工信息
    $scope.getStaffInfoById = function (id) {
        $http.get("rest/staff/getStaffWithRole/" + id, null).success(function (response) {
            if (response !== null && response !== undefined) {
                console.log("getStaffWithRole", response);
                $scope.selectedStaff = response.data[0];
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };

    $scope.getDepartList = function () {
        //获取所有部门
        $http.get("rest/depart/allList", null).success(function (data) {
            if (data !== null && data !== undefined) {
                $scope.departList = data;
                console.log("depart/lis", data);
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };
    $scope.getDepartList();

    //异常类型改变触发
    $scope.typeChange = function (val) {
        $scope.showAction = val === "转正";
    };

    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        if ($scope.formData.effectTime) {
            $scope.formData.effectTime = DateToString2(new Date($scope.formData.effectTime));
        }
    };

    //显示高级搜索
    $scope.showAdvancedSearch = function () {
        $scope.advancedSearch = !$scope.advancedSearch;
    };

    //高级搜索点击搜索
    $scope.search = function () {
        var rangeInput = $('#turn_range').val();
        if (rangeInput) {
            $scope.req.startTime = rangeInput.substring(0, rangeInput.indexOf("~") - 1);
            $scope.req.endTime = rangeInput.substring(rangeInput.indexOf("~") + 2);
        } else {
            $scope.req.startTime = null;
            $scope.req.endTime = null;
        }
        self.tableParams.reload();
    };

    //清除uiselect选中
    $scope.clearUISelect = function () {
        $scope.selected.value = undefined;
        $scope.req.staffId = null;
        self.tableParams.reload();
    };


    /*******************************异动信息导出 开始*********************************/

    $http.get("rest/turnover/allCol", {}).success(function (res) {
        $scope.myCols = [];
        console.log("allCol", res);
        if (res !== null && res.resultCode === "RESULT_SUCCESS") {
            var allColumns = res.data;
            angular.forEach(allColumns, function (col) {
                var one = {"columnName": col.columnName, "isShow": col.isShow == 1};
                $scope.myCols.push(one);
            });
        }
    });

    //导出异动信息前操作
    $scope.toExportTurnover = function () {
        angular.element("#turnoverExportSelect").modal({show: true});
    };

    //异动导出列定制 点击checkbox触发
    $scope.exportCheck = function (name) {
        angular.forEach($scope.myCols, function (col) {
            if (col.columnName === name) {
                col.checked = !col.checked;
            }
        });
    };

    //导出员工信息全选按钮
    $scope.exportCheckAll = function () {
        angular.forEach($scope.myCols, function (col) {
            col.checked = $scope.exportAllModel;
        });
    };

    //导出员工信息
    $scope.exportTurnover = function () {
        var exportArr = [];
        angular.forEach($scope.myCols, function (col) {
            if (col.checked) {
                exportArr.push(col.columnName);
            }
        });
        if (exportArr.length < 1) {
            showOkModal("提示", "请至少选择一列");
            return;
        }
        console.log("exportArr", exportArr);
        $scope.req.exportStr = exportArr.join(",");
        $http.post("rest/turnover/export", $scope.req, {responseType: 'blob'}).success(function (data, status, headers) {
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            var fileName = headers()["filename"];
            fileName = decodeURI(fileName);
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();
            angular.element("#turnoverExportSelect").modal('hide');
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };
    /*******************************异动信息导出 结束*********************************/
}]);