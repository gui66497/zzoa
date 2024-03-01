'use strict';

indexModule.factory("staffService", ['$http','$q','$cookieStore', function ($http,$q,$cookieStore) {

    return {
        query : function() {
            var req = {};

            req.paging = {};
            req.paging.pageNo = 1;
            req.paging.pageSize = 5;
            var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行
            $http.post("rest/staff/page", req).success(function (data) {
                if (data != null && data != undefined) {
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
                angular.element("#serverErrorModal").modal({show: true});
            });
            return deferred.promise;   // 返回承诺，这里并不是最终数据，而是访问最终数据的API
        },// end query
        //获取所有员工信息（只包含简单信息）
        queryAllStaff : function(val) {
            var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行
            $http.get("rest/staff/" + val + "/allSimple").success(function (data) {
                if (data != null && data != undefined) {
                    var staffs = data.data;
                    deferred.resolve(staffs);  // 声明执行成功，即http请求数据成功，可以返回数据了
                }
            }).error(function (data) {
                deferred.reject(data.data);   // 声明执行失败，即服务器返回错误
                angular.element("#serverErrorModal").modal({show: true});
            });
            return deferred.promise;   // 返回承诺，这里并不是最终数据，而是访问最终数据的API
        },// end query
        /**
         * 获取所有组
         * @returns {*}
         */
        getAllGroup : function () {
            var defer=$q.defer();  //声明延后执行

            $http.get("rest/identity/group/list").success(function (data) {
                defer.resolve(data.data);
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });

            return defer.promise;

            /*var promise = $http.get("rest/identity/group/list");
            return promise.then(function (response) {
                var result = response.data;
                if(result == null || result == undefined) {
                    return null;
                }
                console.log("result.data :", result.data);
                return result.data;
            });*/
        },
        getGroupsByDepartId : function(departId){
            var defer=$q.defer();  //声明延后执行
            $http.get("rest/groupDepart/"+departId+"/getListByDepartId").success(function (data) {
                defer.resolve(data);
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });

            return defer.promise;
        },

        //部门添加角色
        addAuthority : function(obj){
            var defer=$q.defer();  //声明延后执行
            $http.post("rest/groupDepart/add",obj).success(function (data) {
                defer.resolve(data);
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });

            return defer.promise;
        },

        //用户添加角色
        addUserDepartAuthority : function(obj){
            var defer=$q.defer();  //声明延后执行
            $http.post("rest/staff/setDepartRole",obj).success(function (data) {
                defer.resolve(data);
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });

            return defer.promise;
        },

        //根据用户ID获取该用户的角色list
        getUserAuthorityByStaffId : function(staffId){
            var defer = $q.defer();  //声明延后执行
            $http.get("rest/groupDepart/"+staffId+"/getGroupList ",null).success(function (data) {
                defer.resolve(data);
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });

            return defer.promise;
        },

        //获取所有部门信息
        getAllDeparts : function () {
            var defer = $q.defer();  //声明延后执行
            $http.get("rest/depart/allList", null).success(function (data) {
                defer.resolve(data);
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
            return defer.promise;
        },

        //获取当前登录用户在统一权限管理系统中的所有权限  (返回权限名称数组)
        getAllPermissions : function () {
            var defer = $q.defer();  //声明延后执行
            var res = {};
            //统一权限管理系统 获取指定系统指定用户的所有权限
            res.url = "permissionInfo/permissionListByUserName";
            var params = {"systemName":"君智OA", "systemSign":"zzoa", "targetId":"-1", "paging":{"pageSize":100,"pageNo":1}};
            var userName = $cookieStore.get("loginUserName");
            if (userName) {
                params.userName = userName;
            }
            res.js = JSON.stringify(params);
            $http.post("rest/util/link", res).success(function (data) {
                var response = JSON.parse(data);
                var permissions;
                if(response.data) {
                    permissions = response.data;
                }
                var permissionArr = [];
                angular.forEach(permissions, function (data, index) {
                    permissionArr.push(data.name);
                });
                defer.resolve(permissionArr);
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
            return defer.promise;
        },

        //考勤 获取开始时间
        getStartTime : function () {
            var date = new Date;
            var year = date.getFullYear();
            // 考勤周期改为自然月
            var month = date.getMonth() + 1;
            month = (month < 10 ? "0"+month : month);
            return (year.toString() + '-' + month.toString() + '-' + '01');
        },

        //考勤 获取结束时间
        getEndTime : function () {
            var date = new Date;
            var year = date.getFullYear();
            // 考勤周期改为自然月
            var month = date.getMonth() + 1;
            var lastDay = new Date(year, month, 0);
            var day = lastDay.getDate();
            month = (month < 10 ? "0" + month : month);
            return (year.toString() + '-' + (month).toString() + '-' + day);
        }
    };

}]);