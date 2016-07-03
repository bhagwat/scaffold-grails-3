'use strict';

angular.module("myApplication")
    .directive('contentTableWidget', [function () {
        return {
            restrict: 'E',
            replace: true,
            scope: {data: "=", title: '@'},
            templateUrl: 'app/components/directives/views/contentTableWidget.html'
        };
    }]);
