'use strict';

angular.module('app')
    .directive('footer', [function () {
        return {
            restrict: 'E',
            replace:true,
            templateUrl: 'app/components/directives/views/footer.html'
        };
    }]);
