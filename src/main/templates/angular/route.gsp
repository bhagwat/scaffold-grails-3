(function(){
    'use strict';

angular.module('app')
    .config(["navServiceProvider", function (navServiceProvider) {
        var base = "app/modules/${propertyName}/views/";

        navServiceProvider.addMenu(
            new MenuItem(5)
                .setState("home.${propertyName}", {
                    abstract: true,
                    url: "/${propertyName}",
                    template: '<ui-view/>',
                    title: "${className}",
                    icon: 'table'
                })
                .add(new MenuItem(10)
                    .setState("home.${propertyName}.list", {
                        url: '/list',
                        templateUrl: base + "${propertyName}.list.html",
                        title: "List"
                    }))
                .add(new MenuItem(20)
                    .setState("home.${propertyName}.create", {
                        url: '/create',
                        templateUrl: base + "${propertyName}.form.html",
                        title: "New ${className}"
                    }))
                .add(new MenuItem(20)
                    .setState("home.${propertyName}.edit", {
                        url: '/edit/:id',
                        templateUrl: base + "${propertyName}.form.html",
                        title: "Edit ${className}",
                        hidden: true
                    }))
        );
    }]);
}());