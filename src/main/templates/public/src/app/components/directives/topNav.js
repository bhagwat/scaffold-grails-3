'use strict';

angular.module('app')
    .directive('topNav', [function () {
        return {
            restrict: 'E',
            replace:true,
            templateUrl: 'app/components/directives/views/topNav.html'
        };
    }]);
