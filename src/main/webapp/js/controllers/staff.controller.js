'use strict';

//staff controller
indexModule.controller('staffController', function ($scope, $http, $q, NgTableParams, staffService) {

    //moment.locale('en');        // English
    moment.locale('zh-cn');     // Simplified chinese

    var self = this;
    $scope.req = {};
    $scope.req.paging = {};
    $scope.req.paging.pageNo = 1;
    $scope.req.paging.pageSize = 10;

    $scope.formData = {};
    
    /*var data = [{name: "Moroni1", age: 1}, {name: "gu", age: 2},
        {name: "Moroni2", age: 3}, {name: "gu2", age: 4},
        {name: "Moroni1", age: 5}, {name: "gu4", age: 6},
        {name: "Moroni3", age: 7}, {name: "gu5", age: 8},
        {name: "Moroni4", age: 9}, {name: "gu6", age: 10}];
    self.tableParams = new NgTableParams({'count': 5}, {dataset: data});*/

    self.cols = [
        { field: "name", title: "姓名", show: true },
        { field: "department", title: "部门", show: false },
        { field: "position", title: "职位", show: true },
        { field: "entryDate", title: "入职日期", show: true },
        { field: "isFormal", title: "入职日期", show: true }
    ];
    self.tableParams = new NgTableParams({'count': 10} , {
        getData: function (params) {
            console.log("params的url内容", params.url());
            var page = params.url();
            $scope.req.paging.pageNo = page.page;
            $scope.req.paging.pageSize = page.count;

            var promise = $http.post("rest/staff/page", $scope.req);
            return promise.then(function (resut) {
                var response = resut.data;
                var total = response.otherData[0];
                params.total(total);
                return response.data;
            });

        }
    });

    //打开新增员工模态框
    $scope.showAddStaffModal = function () {
        $("#addStaffModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //新增员工
    $scope.addStaff = function () {
        console.log("formdata内容：", $scope.formData);
        if ($scope.formData.entryDate) {
            $scope.formData.entryDate = DateToString3(new Date($scope.formData.entryDate));
        }
        $http.post("rest/staff/add", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
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


});