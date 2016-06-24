(function () {
'use strict';

angular.module("public")
    .directive("${propertyName}", [function () {
        return {
            restrict: 'A',
            scope: {},
            link: function (scope, element) {
            },
            controller: ["\$scope", function (\$scope) {
                var vm = this;
            }],
            controllerAs: "${propertyName}Ctrl",
            templateUrl: "public/app/components/directives/views/${propertyName}.html"
        };
    }]);
}());