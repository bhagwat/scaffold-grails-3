'use strict';

angular.module("${moduleName}")
    .directive('fieldValidationErrors', [function () {
        return {
            restrict: 'EA',
            scope: {fieldValidationErrors: "="},
            templateUrl: 'app/components/directives/views/fieldValidationErrors.html'
        };
    }]);
