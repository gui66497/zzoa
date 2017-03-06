'use strict';

indexModule.factory("staffService", function ($http,$q) {

    return {
        query : function() {
            var req = {};

            req.paging = {};
            req.paging.pageNo = 1;
            req.paging.pageSize = 5;
            var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行
            $http.post("rest/staff/page", req).success(function (data) {
                if (data != null && data != undefined) {
                    console.log(data.data);
                    var staffs = data.data;
                    var otherData = data.otherData;
                    /*$scope.otherData = data.otherData;
                    $scope.totalRecordCount = $scope.otherData[0];
                    $scope.totalPage = $scope.otherData[1];*/
                    /*params.total(otherData[1]);*/
                    deferred.resolve(staffs);  // 声明执行成功，即http请求数据成功，可以返回数据了
                }
                //$scope.setPageButtonStatus();
            }).error(function (data) {
                deferred.reject(data.data);   // 声明执行失败，即服务器返回错误
                $("#serverErrorModal").modal({show: true});
            });
            return deferred.promise;   // 返回承诺，这里并不是最终数据，而是访问最终数据的API
        } // end query
    };

});