
indexModule.directive('setFocus', ['$timeout', function ($timeout) {
    return {
        scope: false,
        link: function (scope, element){
            scope.$watch("isFocus", function (newValue, oldValue, scope) {
                if (newValue) {
                    $timeout(function () {
                        element[0].focus(); //获取焦点
                    }, 200);
                }
            }, true);
        }
    }
}]);
