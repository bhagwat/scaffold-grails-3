'use strict';

angular.module("${moduleName}")
    .directive('topNav', [function () {
        return {
            restrict: 'E',
            replace:true,
            templateUrl: 'app/components/directives/views/topNav.html'
        };
    }]);
