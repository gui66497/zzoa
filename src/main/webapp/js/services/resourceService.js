'use strict';

indexModule.factory("resourceService", ['$http','$q',function ($http,$q) {

    return {
        //获取指定目录下的所有目录集合
        getCatalogs : function (catalogId) {
            var defer = $q.defer();  //声明延后执行
            $http.post("rest/catalog/"+catalogId+"/list", {}).success(function (data) {
                defer.resolve(data);
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
            return defer.promise;
        },
        //获取指定目录下的所有文件
        getResourceByCatalogId : function (catalogId, staffId) {
            var defer = $q.defer();  //声明延后执行
            $http.post("rest/resource/"+catalogId+"/"+staffId+"/resourceList", {}).success(function (data) {
                defer.resolve(data);
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
            return defer.promise;
        }
    };

}]);