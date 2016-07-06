'use strict';

angular.module("${moduleName}")
    .directive('remoteUrl', ["Configuration", "\$resource",
        function (Configuration, resource) {
            return {
                restrict: 'A',
                require: 'ngModel',
                scope: {
                    ngModel: "=",
                    remoteUrl: '@',
                    options: "="
                },
                link: function (scope) {
                    scope.options = resource(Configuration.API + scope.remoteUrl).query();
                }
            }
        }]);
