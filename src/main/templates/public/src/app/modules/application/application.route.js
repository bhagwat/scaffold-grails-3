(function () {
    'use strict';

    angular.module("${moduleName}")
        .config(["navServiceProvider", function (navServiceProvider) {
            var base = "app/modules/application/views/";

            navServiceProvider.addMenu(
                new MenuItem(1)
                    .setState("home.application", {
                        url: "/application",
                        templateUrl: base + "placeholder.html",
                        title: "Application",
                        icon: 'table'
                    })
                    .add(new MenuItem(10)
                        .setState("home.application.info", {
                            url: '/info',
                            template: '<content-table-widget data="applicationCtrl.applicationInfo.info" title="Info"></content-table-widget>',
                            title: "Info"
                        }))
                    .add(new MenuItem(20)
                        .setState("home.application.artifacts", {
                            url: '/artifacts',
                            template: '<content-table-widget data="applicationCtrl.applicationInfo.artefacts" title="Artefacts"></content-table-widget>',
                            title: "Artifacts"
                        }))
                    .add(new MenuItem(30)
                        .setState("home.application.controllers", {
                            url: '/controllers',
                            template: '<content-table-widget data="applicationCtrl.applicationInfo.controllers" title="Controllers"></content-table-widget>',
                            title: "Controllers"
                        }))
                    .add(new MenuItem(40)
                        .setState("home.application.plugins", {
                            url: '/plugins',
                            templateUrl: base + "plugins.html",
                            title: "Plugins"
                        }))
                    .add(new MenuItem(50)
                        .setState("home.application.jvm", {
                            url: '/jvm',
                            templateUrl: base + "jvm.html",
                            title: "JVM"
                        }))
            );
        }]);
}());