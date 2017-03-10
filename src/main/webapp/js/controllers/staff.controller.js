'use strict';

//staff controller
indexModule.controller('staffController', function ($scope, $http, $q, NgTableParams, $timeout) {

    //moment.locale('en');        // English
    moment.locale('zh-cn');     // Simplified chinese

    var self = this;

    $scope.tableData = {};

    function initialRequest() {
        $scope.req = {};
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }

    //form表单
    $scope.formData = {};

    self.formalArr = [{id: 1, title: '是'}, {id: 0, title: '否'}];

    self.genderArr = [{id: 1, title: '男'}, {id: 0, title: '女'}];

    self.politicalArr = [{id: '团员', title: '团员'}, {id: '党员', title: '党员'}, {id: '群众', title: '群众'}];

    self.educationArr = [{id: '本科', title: '本科'}, {id: '硕士', title: '硕士'}, {id: '博士', title: '博士'}];

    /*self.cols = [
     { field: "name", title: "姓名", show: true },
     { field: "department", title: "部门", show: false },
     { field: "position", title: "职位", show: true },
     { field: "entryDate", title: "入职日期", show: true },
     { field: "isFormal", title: "是否转正", show: true },
     { field: "formalDate", title: "转正日期", show: true },
     { field: "companyAge", title: "司龄", filter: { companyAge: "number" }, show: true },
     { field: "idNumber", title: "身份证号", filter: { idNumber: "text" }, show: true },
     { field: "birth", title: "出生日期", show: true },
     { field: "age", title: "年龄", show: true, filter: { age: "number" } },
     { field: "gender", title: "性别", filter: { gender: "select"}, filterData: self.genderArr, show: true },
     { field: "preSeniority", title: "入职前工龄", filter: { preSeniority: "number"}, show: true },
     { field: "seniority", title: "工龄", filter: { seniority: "number"}, show: true },
     { field: "payCard", title: "工资卡号", filter: { payCard: "text"}, show: true },
     { field: "politicalStatus", title: "政治面貌", filter: { politicalStatus: "select"}, filterData: self.politicalArr, show: true },
     { field: "jobNumber", title: "工号", filter: { jobNumber: "text"}, show: true },
     { field: "contractStart", title: "合同起始时间", show: true },
     { field: "contractEnd", title: "合同结束时间", show: true },
     { field: "tel", title: "电话", show: true },
     { field: "graduateSchool", title: "毕业院校", filter: { graduateSchool: "text"}, show: true },
     { field: "major", title: "专业", filter: { major: "text"}, show: true },
     { field: "education", title: "学历", filter: { education: "select"}, filterData: self.educationArr, show: true },
     { field: "graduateDate", title: "毕业日期", filter: { jobNumber: "text"}, show: true }

     ];*/


    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            //清空请求
            initialRequest();
            console.log("params的url内容", params.url());
            var allParams = params.url();

            for (var key in allParams) {
                //是筛选值
                if (key.indexOf("filter") > -1) {
                    var columnName = key.substring(key.indexOf("[") + 1, key.length - 1);
                    var value = decodeURI(allParams[key]);
                    $scope.req[columnName] = value;
                }
            }

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;

            console.log("req的值为：", $scope.req);

            columnInitial();

            var promise = $http.post("rest/staff/page", $scope.req);
            return promise.then(function (resut) {
                var response = resut.data;
                var total = response.otherData[0];
                params.total(total);
                $scope.tableData = response.data;
                return response.data;
            });

        }
    });

    $scope.myCols = [];

    /**
     * 显示列初始化
     */
    function columnInitial() {
        console.log("显示列初始化");
        var localCols = [];
        //不显示的列
        var unShowColumnArr = [];
        $http.get("rest/staff/allCol", {}).success(function (res) {
            if (res.resultCode == "RESULT_SUCCESS") {
                var allColumns = res.data;
                angular.forEach(allColumns, function (col) {
                    var one = {"columnName": col.columnName, "isShow": col.isShow == 1};
                    localCols.push(one);
                    if (col.isShow == 0) {
                        unShowColumnArr.push(col.columnName);
                    }
                });
                $scope.myCols = localCols;
                //使其隐藏
                angular.forEach(self.cols, function (col, index) {
                    if (unShowColumnArr.indexOf(col.title()) != -1) {
                        self.cols[index].show = function () {
                            return false;
                        };
                    } else {
                        self.cols[index].show = function () {
                            return true;
                        };
                    }
                });
            }
        });
    }

    /**
     * 监听checkbox变化
     */
    $scope.$watch('myCols', function (newValue, oldValue, scope) {
        if (newValue.length == 0 || oldValue.length == 0) {
            return;
        }
        var keepGoing = true;
        angular.forEach(newValue, function (data, index) {
            if (keepGoing) {
                //找到改变的列
                if (data.isShow != oldValue[index].isShow) {
                    columnUpdate(data);
                    keepGoing = false;
                }
            }
        });
    }, true);

    /**
     * 列修改
     * @param col
     */
    function columnUpdate(col) {
        console.log("需要修改的column：" + col);
        var column = {"columnName": col.columnName, "isShow": col.isShow ? 1 : 0};
        $http.put("rest/staff/updateCol", column).success(function (res) {
            console.log(res);
            if (res.resultCode == "RESULT_SUCCESS") {
                self.tableParams.reload();
            }
        });

    }

    $scope.isShow = false;
    $scope.hideOrShow = function () {
        $scope.isShow = !$scope.isShow;
    };

    //打开新增员工模态框
    $scope.showAddStaffModal = function () {
        $("#addStaffModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //新增员工
    $scope.addStaff = function () {
        console.log("formdata内容：", $scope.formData);
        console.log($scope.formData);
        $http.post("rest/staff/add", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    showOkModal('提示', "添加成功！");
                    //refreshListView();
                    $("#addStaffModal").modal('hide');

                } else {
                    showOkModal('提示', data.message);
                }
            }
            //$scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //弹出确认删除模态框
    $scope.showDeleteStaff = function (index) {
        console.log("当前tableData的值为：", $scope.tableData);
        var staffId = $scope.tableData[index].staffId;
        showConfirmModal("确认要删除吗？", function () {
            $http.delete("rest/staff/" + staffId + "/del").success(function (response) {
                if (response.resultCode == "RESULT_SUCCESS") {
                    showOkModal('提示', "删除成功！");
                    self.tableParams.reload();
                    $("#confirmModal").modal('hide');
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        });
    };

    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    $scope.formatterTime = function () {
        if ($scope.formData.entryDate) {
            $scope.formData.entryDate = DateToString3(new Date($scope.formData.entryDate));
        }
        if ($scope.formData.formalDate) {
            $scope.formData.formalDate = DateToString3(new Date($scope.formData.formalDate));
        }
        if ($scope.formData.birth) {
            $scope.formData.birth = DateToString3(new Date($scope.formData.birth));
        }
        if ($scope.formData.contractStart) {
            $scope.formData.contractStart = DateToString3(new Date($scope.formData.contractStart));
        }
        if ($scope.formData.contractEnd) {
            $scope.formData.contractEnd = DateToString3(new Date($scope.formData.contractEnd));
        }
        if ($scope.formData.graduateDate) {
            $scope.formData.graduateDate = DateToString3(new Date($scope.formData.graduateDate));
        }
    };

    /* 合同开始时间 结束时间组合选择框
     -----------------------------------------------*/
    $scope.endDateBeforeRender = endDateBeforeRender
    $scope.endDateOnSetTime = endDateOnSetTime
    $scope.startDateBeforeRender = startDateBeforeRender
    $scope.startDateOnSetTime = startDateOnSetTime

    function startDateOnSetTime() {
        $scope.$broadcast('start-date-changed');
        $scope.formatterTime();
    }

    function endDateOnSetTime() {
        $scope.$broadcast('end-date-changed');
        $scope.formatterTime();
    }

    function startDateBeforeRender($dates) {
        if ($scope.formData.contractEnd) {
            var activeDate = moment($scope.formData.contractEnd);

            $dates.filter(function (date) {
                return date.localDateValue() >= activeDate.valueOf()
            }).forEach(function (date) {
                date.selectable = false;
            })
        }
    }

    function endDateBeforeRender($view, $dates) {
        if ($scope.formData.contractStart) {
            var activeDate = moment($scope.formData.contractStart).subtract(1, $view).add(1, 'minute');

            $dates.filter(function (date) {
                return date.localDateValue() <= activeDate.valueOf()
            }).forEach(function (date) {
                date.selectable = false;
            })
        }
    }

    /*-----------------------------------------------*/

});