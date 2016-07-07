var MenuItem = function (index) {
    var _self = this;

    _self.index = index;
    _self.parent = null;
    _self.state = "";
    _self.icon = "";
    _self.title = "";
    _self.hidden = false;
    _self.items = [];

    _self.add = function (item) {
        item.parent = _self;
        _self.items.push(item);
        return _self;
    };

    _self.setState = function (state, config) {
        _self.state = state;
        _self.icon = config.icon || '';
        _self.title = config.title || state;
        _self.hidden = angular.isDefined(config.hidden) ? config.hidden : false;
        _self.config = config;
        return _self;
    };

    return _self;
};

(function () {
    'use strict';
    angular.module("${moduleName}", ['ngResource', 'ngAnimate', 'ngMessages', 'toastr', 'ui.bootstrap', 'ui.router','mwl.confirm'])
        .config(["\$urlRouterProvider", "\$stateProvider", "navServiceProvider",
            function (urlRouterProvider, stateProvider, navServiceProvider) {
            navServiceProvider.setStateProvider(stateProvider);
                navServiceProvider.setRoot(
                    new MenuItem(10)
                        .setState('home', {
                            templateUrl: 'app/layout.html',
                            title: "General",
                            icon: 'home'
                        })
                );
                urlRouterProvider.when('', '/application/info');
        }])
        .constant('_', window._)
        .service("Configuration", [function () {
            if (/localhost:3000/.test(window.location.host)) {
                return this.API = 'http://localhost:8080';
            } else {
                return this.API = '';
            }
        }]);
})();