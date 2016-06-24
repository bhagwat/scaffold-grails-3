(function () {
'use strict';

angular.module('app')
    .factory("${className}", ["DomainServiceFactory", function (DomainServiceFactory) {
        return DomainServiceFactory("/${propertyName}/:action/:id", {"id": "@id"});
    }]);
})();