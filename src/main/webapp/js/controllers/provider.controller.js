'use strict';

//加班管理模块
indexModule.controller('providerController',['$scope', '$http', '$q', 'staffService', 'NgTableParams', '$filter','$timeout', function ($scope, $http, $q, staffService, NgTableParams, $filter,$timeout) {

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

    /*$scope.req.startTime = staffService.getStartTime();
    $scope.req.endTime = staffService.getEndTime();*/

    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            //清空请求
            initialRequest();
            console.log("params的url内容", params.url());
            var allParams = params.url();

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;
            $scope.req.name = $scope.searchName;

            console.log("req的值为：", $scope.req);

            var promise = $http.post("rest/provider/page", $scope.req);
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
                $timeout(function () {
                    aFixed=document.querySelectorAll('.fixedIndex');
                    aFixedName=document.querySelectorAll('.fixedName');
                    aFixedMake=document.querySelectorAll('.fixedMake');
                });
                return response.data;

            });
        }
    });

    var aFixed;
    var aFixedName;
    var aFixedMake;
    var aProviderListTable=document.getElementById("providerListTable");
    var aInside=document.querySelectorAll('.inside');
    aProviderListTable.onscroll=function () {
        var leftLong=aProviderListTable.scrollLeft;
        var topLong=aProviderListTable.scrollTop;
        for(var i=0;i<aFixed.length;i++){
            aFixed[i].style.left=leftLong+'px';
            aFixedName[i].style.left=leftLong+40+'px';
            aFixedMake[i].style.right=-leftLong+'px';
            aFixed[0].style.top=topLong+'px';
            aFixedName[0].style.top=topLong+'px';
            aFixedMake[0].style.top=topLong+'px';
        }
        for(var a=0;a<aInside.length;a++){
            aInside[a].style.top=topLong+'px';
        }

    };

    //搜索栏查询
    $scope.search = function () {
        self.tableParams._params.page = 1;
        self.tableParams.reload();
    };

    function allFalse() {
        $scope.myColor0=false;
        $scope.myColor1=false;
        $scope.myColor2=false;
        $scope.myColor3=false;
        $scope.myColor4=false;
        $scope.myColor5=false;
        $scope.myColor6=false;
        $scope.myColor7=false;
        $scope.myColor8=false;
        $scope.myColor9=false;
        $scope.myColor10=false;
        $scope.myColor11=false;
        $scope.myColor12=false;
        $scope.myColor13=false;
        $scope.myColor14=false;
        $scope.myColor15=false;
        $scope.myColor16=false;
        $scope.myColor17=false;
    }
    function judge() {
        if($scope.formData.threeCe==="√"){
            $scope.myColor0=true;
        }else if($scope.formData.threeCe==="×"){
            $scope.myColor1=true;
        }else if($scope.formData.threeCe==="O"){
            $scope.myColor2=true;
        }
        if($scope.formData.isoCe==="√"){
            $scope.myColor3=true;
        }else if($scope.formData.isoCe==="×"){
            $scope.myColor4=true;
        }else if($scope.formData.isoCe==="O"){
            $scope.myColor5=true;
        }
        if($scope.formData.agentCe==="√"){
            $scope.myColor6=true;
        }else if($scope.formData.agentCe==="×"){
            $scope.myColor7=true;
        }else if($scope.formData.agentCe==="O"){
            $scope.myColor8=true;
        }
        if($scope.formData.technicalParam==="√"){
            $scope.myColor9=true;
        }else if($scope.formData.technicalParam==="×"){
            $scope.myColor10=true;
        }else if($scope.formData.technicalParam==="O"){
            $scope.myColor11=true;
        }
        if($scope.formData.aType==="√"){
            $scope.myColor12=true;
        }else if($scope.formData.aType==="-"){
            $scope.myColor13=true;
        }
        if($scope.formData.bType==="√"){
            $scope.myColor14=true;
        }else if($scope.formData.bType==="-"){
            $scope.myColor15=true;
        }
        if($scope.formData.cType==="√"){
            $scope.myColor16=true;
        }else if($scope.formData.cType==="-"){
            $scope.myColor17=true;
        }
    }
    $scope.provider=function () {
        var temp = document.getElementById("emailProvider");
        var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        if(!myreg.test(temp.value)){
            alert("邮箱输入内容错误");
            return false
        }
    };

    //新增
    $scope.addAndUpdateProvider = function (index) {
        $scope.addSupp=false;
        $scope.providerDate=true;
        $scope.addHide=false;
        $scope.disPersonal=false;
        $scope.supplierActive=true;
        $scope.formData = {};
        allFalse();
        $scope.changeColor=function () {
            allFalse();
            judge();
        };
        $scope.provider;
        if (index != undefined) {
            $scope.formData = angular.copy($scope.tableData[index]);
            $("#addAndUpdateProviderModal").modal({show: true, keyboard: false, backdrop: 'static'});
        }else {
            $("#addAndUpdateProviderModal").modal({show: true, keyboard: false, backdrop: 'static'});
        }
    };
    //修改
    $scope.reviseProvider=function (index) {
        allFalse();
        $("#addAndUpdateProviderModal").modal({show: true, keyboard: false, backdrop: 'static'});
        $scope.formData=$scope.tableData[index];
        $scope.providerDate=false;
        $scope.disPersonal=true;
        $scope.supplierActive=false;
        $scope.addSupp=true;
        $scope.addHide=true;
        judge();
        $scope.provider;
        $scope.editSupplier=function () {
            $scope.disPersonal=!$scope.disPersonal;
            $scope.supplierActive=!$scope.supplierActive;
            $scope.providerDate=!$scope.providerDate;
            $scope.addHide=!$scope.addHide;
        }
    };

    $scope.saveAndSubmitProvider=function () {
        $scope.hideFrom=true;
        $http.post("rest/provider/save", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                $scope.hideFrom=false;
                if(data.resultCode=="RESULT_SUCCESS") {
                    $("#addAndUpdateProviderModal").modal('hide');
                    showOkModal('提示', "保存成功！");
                } else {
                    $scope.hideFrom=false;
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#addAndUpdateProviderModal").modal({show: true});
        });
    };

    $scope.radioChange=function () {
        var chooseId = $scope.formData.processor;
        if(chooseId!==undefined){
            $scope.disSub=false;
        }
    };

    //弹出确认删除模态框
    $scope.showDeleteProvider = function (index) {
        var id = $scope.tableData[index].id;
        showConfirmModal("确定要删除吗？", function () {
            $http.delete("rest/provider/" + id + "/delete").success(function (response) {
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

    $scope.fileChanged=function (ele) {
        var filesContent = document.getElementById("file").value;
        document.getElementById("fileValues").value = filesContent.substr(filesContent.lastIndexOf("\\") + 1);
    };

    //导入供应商信息
    $scope.importProvider = function () {
        angular.element("#uploadProvider").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //执行导入
    $scope.upload = function () {
        $scope.hideFrom=true;
        var files = document.getElementsByName('file')[0].files;
        if (files.length < 1) {
            $scope.hideFrom=false;
            showOkModal("提示信息", "请先选择文件！");
            return;
        }
        $http({
            method: 'POST',
            url: 'rest/provider/upload',
            headers: {
                'Content-Type': undefined
            },
            data: {
                file: files[0]
            },
            transformRequest: function (data) {
                var formData = new FormData();
                formData.append('file', data.file);
                return formData;
            }
        }).success(function (data) {
            //请求成功
            $scope.hideFrom=false;
            console.log("data:", data);
            showOkModal("提示", data.message);
            if (data.resultCode === 'RESULT_SUCCESS') {
                self.tableParams.reload();
                angular.element("#uploadProvider").modal('hide');
            }

        }).error(function (err, status) {
            $scope.hideFrom=false;
            console.log(err);
        });
    };

    //导出
    $scope.outputProvider = function () {
        $http.post("rest/provider/export", $scope.req, {responseType: 'blob'}).success(function (data, status, headers) {
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            var fileName = headers()["filename"];
            fileName = decodeURI(fileName);
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };

    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    $scope.formatterTime = function () {
        if ($scope.formData.overDate) {
            $scope.formData.overDate = DateToString2(new Date($scope.formData.overDate));
        }
        if ($scope.formData.contactTime) {
            $scope.formData.contactTime = DateToStringMonth(new Date($scope.formData.contactTime));
        }
    };
}]);
