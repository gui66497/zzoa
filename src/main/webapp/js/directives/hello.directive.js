var helloModule = angular.module('helloModule', []);

helloModule.directive('hello', function() {
    return {
        restrict:'AE',
        //template:'<div>Hello Angular</div>',
        replace:true,
        template :  "<input type='button' value='clickme' ng-click='toggle()' />",
        link : function(scope, element, attrs, ngModel) {
            scope.toggle = function toggle() {
                ngModel.school = "cslg";
                alert(111);
            }
        }
    }
});