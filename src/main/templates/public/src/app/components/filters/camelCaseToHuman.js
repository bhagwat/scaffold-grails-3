'use strict';

angular.module("myApplication")
    .filter('camelCaseToHuman', [function () {
        return function (input) {
            return input.charAt(0).toUpperCase() + input.substr(1).replace(/[A-Z]/g, ' \$&');
        }
    }]);