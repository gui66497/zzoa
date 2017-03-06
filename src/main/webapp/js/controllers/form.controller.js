'use strict';

//表单测试 controller
indexModule.controller('formController', function ($scope, $http) {

    $scope.openLeave = function () {
        $("#addLeaveRequest").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    $scope.openLeave2 = function () {
        $("#addLeaveRequest2").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    $scope.submitForm = function(isValid) {

        // check to make sure the form is completely valid
        if (isValid) {
            alert('our form is amazing');
        }

    };


    /* Bindable functions
     -----------------------------------------------*/
    $scope.endDateBeforeRender = endDateBeforeRender
    $scope.endDateOnSetTime = endDateOnSetTime
    $scope.startDateBeforeRender = startDateBeforeRender
    $scope.startDateOnSetTime = startDateOnSetTime

    function startDateOnSetTime () {
        $scope.$broadcast('start-date-changed');
    }

    function endDateOnSetTime () {
        $scope.$broadcast('end-date-changed');
    }

    function startDateBeforeRender ($dates) {
        if ($scope.dateRangeEnd) {
            var activeDate = moment($scope.dateRangeEnd);

            $dates.filter(function (date) {
                return date.localDateValue() >= activeDate.valueOf()
            }).forEach(function (date) {
                date.selectable = false;
            })
        }
    }

    function endDateBeforeRender ($view, $dates) {
        if ($scope.dateRangeStart) {
            var activeDate = moment($scope.dateRangeStart).subtract(1, $view).add(1, 'minute');

            $dates.filter(function (date) {
                return date.localDateValue() <= activeDate.valueOf()
            }).forEach(function (date) {
                date.selectable = false;
            })
        }
    }
});
