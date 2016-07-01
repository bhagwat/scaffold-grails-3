(function () {
'use strict';

angular.module("$moduleName")
.controller("${className}FormCtrl",
    ["${className}", "\$state", "\$stateParams", "toastr",
        function (${className}, \$state, \$stateParams, toastr) {
            var self = this;

            self.load = function () {
                self.errorData = null;
                self.${propertyName} = \$stateParams.id ? ${className}.edit({id: \$stateParams.id}) : ${className}.create();
            };

            self.save${className} = function () {
                    ${className}.save({${propertyName}: self.${propertyName}},
                    function (${propertyName}) {
                        self.errorData = null;
                        toastr.success("${className} " + (\$stateParams.id ? 'updated' : 'saved') + " successfully.", "Update");
                        \$state.go('^.edit', {id: ${propertyName}.id});
                    }, function (response) {
                        self.errorData = response.data;
                        toastr.error("Invalid data entered. Failed to save.", "Update");
                    }
                );
            };

            self.delete = function () {
                ${className}.delete(self.${propertyName},
                    function () {
                        \$state.go('^.list');
                        toastr.success('${className} deleted successfully.', "Delete");
                    }, function (error) {
                        toastr.error('Failed to delete. Please try  again.', "Delete");
                    }
                );
            };
        }]);
})();
