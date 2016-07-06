(function () {
     'use strict';

angular.module("$moduleName")
.controller("${className}ListCtrl",
    ["${className}", "toastr", function (${className}, toastr) {
            var self = this;
            self.pageSize = 5;
            self.options = {};

            self.sortChanged = function (sort, order) {
                self.options = {sort: sort, order: order};
                self.list();
            };

            self.pageChanged = function () {
                self.options.offset = (self.options.currentPage - 1) * self.pageSize;
                self.list();
            };

            self.list = function () {
                ${className}.list(angular.extend({}, self.options, {max: self.pageSize}), function (listData) {
                    self.listData = listData;
                }, function () {
                    toastr.error("Failed to load data. Please try again.", "List");
                });
            };
        }]);
})();
