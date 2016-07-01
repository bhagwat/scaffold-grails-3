(function () {
'use strict';

angular.module("$moduleName")
    .factory("${className}", ["DomainServiceFactory", function (DomainServiceFactory) {
        return DomainServiceFactory("/${propertyName}/:action/:id", {"id": "@id"});
    }]);
})();