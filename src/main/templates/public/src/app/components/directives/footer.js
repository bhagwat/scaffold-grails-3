'use strict';

angular.module("${moduleName}")
    .directive('footer', [function () {
        return {
            restrict: 'E',
            replace:true,
            templateUrl: 'app/components/directives/views/footer.html'
        };
    }]);
