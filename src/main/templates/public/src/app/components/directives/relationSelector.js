'use strict';

angular.module("${moduleName}")
    .directive('relationSelector', [function () {
        return {
            restrict: 'A',
            scope: {ngModel: "=", remoteUrl: '@', ngClass: '@', manyToMany: "="},
            require: 'ngModel',
            templateUrl: 'app/components/directives/views/relationSelector.html',
            controller: ["\$http", "Configuration", "\$scope", function (http, Configuration, scope) {
                var vm = this;
                http({
                    method: 'GET',
                    url: Configuration.API + scope.remoteUrl,
                    params: {max: 30}
                }).then(function successCallback(response) {
                    vm.entities = response.data;
                }, function errorCallback(response) {
                    console.log("error", response);
                });
            }],
            controllerAs: 'relationSelectorCtrl'
        };
    }]);
