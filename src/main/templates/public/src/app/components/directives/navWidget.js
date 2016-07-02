'use strict';

angular.module("${moduleName}")
    .directive('navWidget', [function () {
        return {
            restrict: 'E',
            scope: {},
            templateUrl: 'app/components/directives/views/navWidget.html',
            controller: ["navService", "\$state", function (navService, state) {
                var vm = this;
                vm.root = navService.getRoot();
                var activePageMenu = navService.findMenu(state.current.name);
                if (activePageMenu) {
                    vm.activePageStat = activePageMenu.state;
                    vm.activeMenuStat = activePageMenu.parent ? activePageMenu.parent.state : '';
                }
            }],
            controllerAs: 'navMenuCtrl'
        };
    }]);
