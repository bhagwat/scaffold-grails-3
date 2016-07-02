(function () {
'use strict';

angular.module("$moduleName")
    .factory("Application", ["DomainServiceFactory", function (DomainServiceFactory) {
        return DomainServiceFactory("/application/:action/:id", {"id": "@id"});
    }]);
})();