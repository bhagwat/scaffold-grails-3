'use strict';

angular.module("${moduleName}")
    .directive('contentTableWidget', [function () {
        return {
            restrict: 'E',
            replace: true,
            scope: {data: "=", title: '@'},
            templateUrl: 'app/components/directives/views/contentTableWidget.html'
        };
    }]);
