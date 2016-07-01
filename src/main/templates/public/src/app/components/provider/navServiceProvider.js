'use strict';

angular.module("${moduleName}")
    .provider('navService', function () {

        var _root = {items: []};
        var subMenus = [];
        var _sateProvider = null;

        function registerState(menu) {
            _sateProvider.state(menu.state, menu.config);
            if (menu.items && menu.items.length) {
                angular.forEach(menu.items, function (subMenu) {
                    _sateProvider.state(subMenu.state, subMenu.config);
                })
            }
        }

        var self = this;

        self.setStateProvider = function (\$stateProvider) {
            _sateProvider = \$stateProvider
        };

        self.getRoot = function () {
            return _root;
        };

        self.setRoot = function (menu) {
            _root = menu;
            _root.items = [];
            registerState(menu);
            _root.items = subMenus;
        };

        self.addMenu = function (menu) {
            menu.parent = _root;
            subMenus.push(menu);
            registerState(menu);
        };

        self.findMenu = function (state) {
            if (!(state && state.length)) {
                return null;
            }
            var temp = [].concat(subMenus);
            while (temp.length > 0) {
                var item = temp.pop();
                if (item.state == state) {
                    return item;
                } else {
                    temp = temp.concat(item.items);
                }
            }
            return null;
        };

        this.\$get = function () {
            return self;
        };
    });
