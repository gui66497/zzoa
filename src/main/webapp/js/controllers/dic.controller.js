'use strict';

//加班管理模块
indexModule.controller('dicController',['$scope', '$http', '$q', 'NgTableParams', '$timeout',  function ($scope, $http, $q, NgTableParams, $timeout) {

    //列表下的分页
    moment.locale('zh-cn');

    var self = this;

    $scope.tableData = {};

    $scope.req = {};

    //form表单
    $scope.formData = {};
    $scope.dicContent = {};

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

            var promise = $http.post("rest/dic/page", $scope.req);
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
        self.tableParams.reload();
        self.tableParams._params.page = 1;
    };
    $scope.myKeyup = function (e) {
        var keycode = window.event?e.keyCode:e.which;
        if(keycode == 13){
            $scope.autoQuery();
        }
    };

    $http.get("rest/dic/title").success(function (data) {
        if (data != null && data != undefined) {
            $scope.dicContent= data.data;
        }else {
            showOkModal('提示', data.message);
        }
    })

    $scope.addAndUpdateDic = function (index) {
        $scope.formData = {};
        $("#addDicModal").modal({show: true, keyboard: false, backdrop: 'static'});
        $http.get("rest/dic/title").success(function (data) {
            if (data != null && data != undefined) {
                $scope.dicContent= data.data;
            }else {
                showOkModal('提示', data.message);
            }
        })
    };

    $scope.saveAndSubmitDic = function () {
        $http.post("rest/dic/add", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    $("#addDicModal").modal('hide');
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    //弹出确认删除模态框
    $scope.showDeleteDic = function (index) {
        var dicId = $scope.tableData[index].dicId;
        var dicKey = $scope.tableData[index].dicKey;
        showConfirmModal("删除后，此字典项将删除，是否删除？", function () {
            $http.delete("rest/dic/" + dicId + "/" + dicKey + "/delete").success(function (response) {
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

    $scope.submitA = function () {
        console.log("提交", $scope.formTest);
    };
}]);

//字典项管理controller
