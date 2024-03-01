/**
 * Created by wanghao on 2017/5/15.
 */
//var roleModule = angular.module('roleModule', ['roleService']);
indexModule.directive('roleDirective', ["$http", function ($http) {


    return {
        restrict: 'AE',
        scope: {
            param: "=",
            model: "="
        },
        template: '<fieldset id="roleField{{param.departId}}"><legend ng-model="param">{{param.departName}}</legend>' +
                  '<div style="float: right;width: 100%;text-align: right;margin-bottom: 10px">' +
                  '<label><input type="checkbox" id="setting_{{param.departId}}" ng-click="settingDefault(param.departId)" ng-checked="tempChecked"  class="default_check"/>设为默认</label></div>' +
                  '<div id="departDiv{{param.departId}}"><label  ng-repeat="x in  list" style="margin-left: 10px;">' +
                  '<input type="radio" class="roleDepartInput" ng-checked="(tempGroupId==x.groupId)? true:false" name="radio{{param.departId}}"  value="{{x}}" ng-click="addRole(x)"/> <span>{{x.groupName}}</span></label> </div></fieldset>',
        link: function (scope, element, attrs){
            $http.get("./rest/groupDepart/" + scope.param.departId + "/getListByDepartId", null).success(function (data) {
                if (data.list.length>0) {
                    scope.list = data.list;
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
            if(scope.param.userAuthorityData){
                angular.forEach(scope.param.userAuthorityData,function(obj,index){
                    if(obj.departId ==scope.param.departId){
                        scope.tempGroupId = obj.groupId;
                        scope.tempChecked = obj.checked;
                        scope.model ={staffId:scope.param.staffId,groupId: obj.groupId, departId: obj.departId,checked:obj.checked};
                        scope.$emit("toParent",scope.model);
                    }
                })
            }
        },
        controller: function ($scope, $element, $attrs) {
            $scope.model = {staffId:$scope.param.staffId,groupId:"", departId: -1,checked:false};
            $scope.addRole =function(){
                var dom = angular.element("#departDiv" + $scope.param.departId)[0].querySelectorAll("input");
                angular.forEach(dom, function (item) {
                    $scope.model.departId = JSON.parse(item.value).departId;
                    if (item.checked) {
                        var groupId = JSON.parse(item.value).groupId;
                        $scope.model.groupId=groupId;
                    }
                });
                console.log($scope.model);
                if($scope.model.groupId!=""){
                    $scope.$emit("toParent",$scope.model);
                }
            }
            $scope.settingDefault = function(itemId){
                $scope.addRole();
                var doms = angular.element(document.querySelectorAll(".default_check"));
                if(angular.element("#setting_"+itemId)[0].checked ==true){
                    angular.forEach(doms,function(obj,index){
                        obj.checked = false;
                    });
                    angular.element("#setting_"+itemId)[0].checked =true;
                }
                var input = angular.element("#roleField"+$scope.param.departId)[0].querySelector(".default_check");
                if(input.checked ==true){
                    $scope.model.checked =true;
                }else{
                    $scope.model.checked =false;
                }
                $scope.$emit("toParent",$scope.model);
                $scope.model = {staffId:$scope.param.staffId,groupId:"", departId: -1,checked:false};
            }

        }
    }
}]);
