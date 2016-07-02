(function () {
    'use strict';

    angular.module("$moduleName")
        .controller("ApplicationCtrl", ["Application", "toastr",
            function (Application, toastr) {
                var self = this;

                self.list = function () {
                    Application.get({}, function (applicationInfo) {
                        self.applicationInfo = applicationInfo;
                        console.log(self.applicationInfo);
                    }, function () {
                        toastr.error("Failed to load data. Please try again.", "Application");
                    });
                };
            }
        ]);
})();
