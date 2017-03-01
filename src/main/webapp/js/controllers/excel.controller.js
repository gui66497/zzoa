'use strict';

//excel controller
indexModule.controller('excelController', function ($scope, $http) {
    /**
     * 下载
     */
    $scope.testDownload = function () {
        $http.post("rest/excel/down", $scope.req, {responseType: 'blob'}).success(function (data, status, headers) {
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            var fileName = headers()["filename"];
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    /**
     * 上传
     */
    $scope.testUpload = function () {
        console.log(document.getElementsByName('file')[0].files[0]);
        $http({
            method: 'POST',
            url: 'rest/excel/upload',
            headers: {
                'Content-Type': undefined
            },
            data: {
                file: document.getElementsByName('file')[0].files[0]
            },
            transformRequest: function (data) {
                var formData = new FormData();
                formData.append('file', data.file);
                return formData;
            },
        }).success(function (data) {
            //请求成功
            showOkModal('提示', data.message);
        }).error(function (err, status) {
            console.log(err);
        });
    };

});
