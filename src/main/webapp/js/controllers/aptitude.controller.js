'use strict';

//加班管理模块
indexModule.controller('AptitudeController',['$scope', '$http', '$q', 'NgTableParams', '$timeout',  function ($scope, $http, $q, NgTableParams, $timeout) {

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

    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            //清空请求
            initialRequest();
            console.log("params的url内容", params.url());
            var allParams = params.url();

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;

            console.log("req的值为：", $scope.req);

            var promise = $http.post("rest/aptitude/page", $scope.req);
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

    // 资质类型
    $http.get("rest/dic/APTITUDE_TYPE/getDicInfo",null).success(function (data) {
        if (data != null && data != undefined) {
            $scope.staffAttribute=data.data;
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    $scope.aptitudeAll=false;
    $scope.aptitudeSearch=function () {
        $scope.aptitudeAll=!$scope.aptitudeAll;
    };

    /*******************************资质信息导出 开始*********************************/

    $http.get("rest/aptitude/allCol", {}).success(function (res) {
        $scope.myCols = [];
        if (res !== null && res.resultCode === "RESULT_SUCCESS") {
            var allColumns = res.data;
            angular.forEach(allColumns, function (col) {
                var one = {"columnName": col.columnName, "isShow": col.isShow == 1};
                $scope.myCols.push(one);
            });
        }
    });

    //导出资质信息前操作
    $scope.toExportAptitude = function () {
        angular.element("#aptitudeExportSelect").modal({show: true});
    };

    //资质导出列定制 点击checkbox触发
    $scope.exportCheck = function (name) {
        angular.forEach($scope.myCols, function (col) {
            if (col.columnName === name) {
                col.checked = !col.checked;
            }
        });
    };

    //导出资质信息全选按钮
    $scope.exportCheckAll = function () {
        angular.forEach($scope.myCols, function (col) {
            col.checked = $scope.exportAllModel;
        });
    };

    //导出资质信息
    $scope.exportAptitude = function () {
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
        $http.post("rest/aptitude/export", $scope.req, {responseType: 'blob'}).success(function (data, status, headers) {
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            var fileName = headers()["filename"];
            fileName = decodeURI(fileName);
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();
            angular.element("#aptitudeExportSelect").modal('hide');
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };
    /*******************************资质信息导出 结束*********************************/
}]);

//字典项管理controller
