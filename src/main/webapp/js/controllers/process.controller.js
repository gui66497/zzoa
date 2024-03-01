'use strict';

//流程配置模块
indexModule.controller('processController', ['$scope', '$http', '$q','NgTableParams','staffService',function ($scope, $http, $q, NgTableParams, staffService) {

    //列表下的分页
    moment.locale('zh-cn');

    var self = this;

    $scope.tableData = {};

    $scope.req = {};

    //form表单
    $scope.formData = {};
    $scope.processSections = {};
    $scope.nodeSections = {};

    function initialRequest() {
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }

    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            //清空请求
            initialRequest();
            console.log("params的url内容", params.url());
            var allParams = params.url();

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;

            console.log("req的值为：", $scope.req);

            var promise = $http.post("rest/process/page", $scope.req);
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

    $scope.autoQuery=function () {
        if($scope.req.processSign=='' || $scope.req.nodeSign==''){
            $scope.req={}
        }
        self.tableParams.reload();
        self.tableParams._params.page = 1;
    };
    $scope.myKeyup = function (e) {
        var keycode = window.event?e.keyCode:e.which;
        if(keycode == 13){
            $scope.autoQuery();
        }
    };

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

    $scope.addProcess = function () {
        $scope.formData = {};
        $scope.processSections = {};
        $scope.nodeSections = {};
        loadSelections();
        $("#addProcessModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    function loadSelections() {
        $http.post("rest/process/getSelections", {}).success(function (data) {
            if (data != null && data != undefined) {
                $scope.processSections = data.data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }

    $http.post("rest/process/getSelections", $scope.formData).success(function (data) {
        if (data != null && data != undefined) {
            $scope.nodeSections = data.data;
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    $scope.changeProcess = function () {
        $scope.nodeSections = {};
        $scope.formData.nodeId=null;
        $http.post("rest/process/getSelections", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                $scope.nodeSections = data.data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    $scope.updateProcess = function (index) {
        $scope.formData = $scope.tableData[index];
        $("#addProcessModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    $scope.saveAndSubmitProcess = function () {
        $scope.formData.processSign = document.getElementById("processIds"+$scope.formData.processId).innerText;
        $scope.formData.nodeSign = document.getElementById("nodeIds"+$scope.formData.nodeId).innerText;
        if($scope.formData.id!=null){
            $http.post("rest/process/update", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        $("#addProcessModal").modal('hide');
                        self.tableParams.reload();
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            });
        } else {
            $http.post("rest/process/add", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        $("#addProcessModal").modal('hide');
                        self.tableParams.reload();
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            });
        }
    };

    //弹出确认删除模态框
    $scope.showDeleteProcess = function (index) {
        var id = $scope.tableData[index].id;
        showConfirmModal("删除后，此流程配置将删除，是否删除？", function () {
            $http.delete("rest/process/" + id + "/delete").success(function (response) {
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
}]);

//字典项管理controller
