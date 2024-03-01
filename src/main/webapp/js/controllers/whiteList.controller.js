'use strict';

indexModule.controller('whiteListController', ['$scope', '$http', '$q','NgTableParams','staffService',function ($scope, $http, $q, NgTableParams,staffService) {
    moment.locale('zh-cn');

    var self = this;
    $scope.tableData = {};
    $scope.req = {};
    $scope.section = {};

    function initialRequest() {
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }
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

            var promise = $http.post("rest/whiteList/page", $scope.req);
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
                    $scope.noNum=false;
                }
                return response.data;

            });
        }
    });

    //获取所有员工(在职)
    $scope.getAllUser = function () {
        var promise = staffService.queryAllStaff("onJob");
        return promise.then(function (result) {
            $scope.allStaff = result;
            return result;
        });
    };

    $scope.getAllUser();

    //select框赋值
    $scope.selected = { value: $scope.allStaff };

    //select选中事件
    $scope.onSelectStaff = function (item, model) {
        $scope.formData.userId = item.staffId;
        $scope.formData.userName = item.name;
    };


    $scope.editList=function (index) {
        $scope.idName=true;
        $scope.amendOrAdd=false;
        $scope.formData=angular.copy($scope.tableData[index]);
        $("#whiteListModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    $scope.whiteShow=function () {
        if($scope.req.type=='' || $scope.req.userName==''){
            $scope.req={}
        }
        self.tableParams.reload();
        self.tableParams._params.page=1;
    };


    //修改
    $scope.submitList=function () {
        $http.post("rest/whiteList/save", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    self.tableParams.reload();
                    $("#whiteListModal").modal('hide');
                    showOkModal('提示', "成功！");
                } else {
                    $scope.hideFrom=false;
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#addAndUpdateProviderModal").modal({show: true});
        });
    };


    //新增
    $scope.addAndUpdateList=function () {
        $scope.amendOrAdd=true;
        $scope.formData={};
        $("#whiteListModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    $scope.changeVacation=function () {
        if($scope.formData.type=='1'){
            $scope.formData.description='哺乳假期间上午可晚一小时签到';
        }
        if($scope.formData.type=='2'){
            $scope.formData.description='哺乳假期间上午可提前一小时签退';
        }
        if($scope.formData.type=='3'){
            $scope.formData.description='哺乳假期间下午可晚一小时签到';
        }
        if($scope.formData.type=='4'){
            $scope.formData.description='哺乳假期间下午可提前一小时签退';
        }
    };

    //删除
    $scope.deleteList=function (index) {
        showConfirmModal("确定要删除吗？", function () {
            $http.delete("rest/whiteList/" + id + "/delete").success(function (response) {
                    if (response.resultCode == "RESULT_SUCCESS") {
                        self.tableParams.reload();
                    }
                }
            ).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        });
    };

    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    $scope.formatterTime = function () {
        if ($scope.formData.endDate) {
            $scope.formData.endDate = DateToString2(new Date($scope.formData.endDate));
        }
        if ($scope.formData.beginDate) {
            $scope.formData.beginDate = DateToString2(new Date($scope.formData.beginDate));
        }
    };

}]);





























