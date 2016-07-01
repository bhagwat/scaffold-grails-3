(function () {
    'use strict';
    angular.module("${moduleName}")
        .factory('DomainServiceFactory', ["\$resource", "Configuration", function (\$resource, Configuration) {
            return function (url, paramDefaults, actions, options) {
                var resourceActions = {
                    create: {method: 'GET', params: {action: 'create'}},
                    edit: {method: 'GET', params: {action: 'edit'}},
                    save: {method: 'POST', params: {action: 'save'}},
                    read: {method: 'GET', params: {action: 'show'}},
                    delete: {method: 'DELETE', params: {action: 'delete'}},
                    list: {method: 'GET', params: {action: 'index'}}
                };
                angular.extend(resourceActions, actions);

                return \$resource(
                    Configuration.API + url,
                    paramDefaults || null,
                    resourceActions,
                    options || {}
                );
            };
        }
        ]);
})();