(function () {
    'use strict';

    angular.module("$moduleName")
        .config(["navServiceProvider", function (navServiceProvider) {
            var base = "app/modules/application/views/";

            navServiceProvider.addMenu(
                new MenuItem(1)
                    .setState("home.application", {
                        url: "/application",
                        templateUrl: base + "application.placeholder.html",
                        title: "Application",
                        icon: 'table'
                    })
                    .add(new MenuItem(10)
                        .setState("home.application.info", {
                            url: '/info',
                            templateUrl: base + "info.html",
                            title: "Info"
                        }))
                    .add(new MenuItem(20)
                        .setState("home.application.artifacts", {
                            url: '/artifacts',
                            templateUrl: base + "artifacts.html",
                            title: "Artifacts"
                        }))
                    .add(new MenuItem(30)
                        .setState("home.application.controllers", {
                            url: '/controllers',
                            templateUrl: base + "controllers.html",
                            title: "Controllers"
                        }))
                    .add(new MenuItem(40)
                        .setState("home.application.plugins", {
                            url: '/plugins',
                            templateUrl: base + "plugins.html",
                            title: "Plugins"
                        }))
            );
        }]);
}());