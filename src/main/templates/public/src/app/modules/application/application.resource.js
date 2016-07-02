(function () {
'use strict';

angular.module("$moduleName")
    .factory("Application", ["DomainServiceFactory", function (DomainServiceFactory) {
        return DomainServiceFactory("/applicationMetric/:action/:id", {"id": "@id"});
    }]);
})();