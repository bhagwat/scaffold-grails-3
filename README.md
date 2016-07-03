# Angular Scaffold Grails 3 plugin
`***Warning: Plugin is still in development mode. You can test it locally.***`

Creates a separate gradle project for managing fronend app. Uses gulp as build tool. Project can be built using Gradle as well. Plugin creates a build.gradle file  with tasks for runnig app locally as a separate application. It updates the root project build.gradle to include the newly added subproject as a dependency and updates jar/war task accordingly to build the frontend app as well. 
## Install plugin

Add dependency

```
    console "org.grails:ng-scaffold:0.1"
```

## Configuration

Change default configurations

```
ng-scaffold:
      cors:
          enabled: true
      module:
          name: myApplication
          description: My Awesome angular application
      base:
          dir: helloapp
```

Config  | Description | Default
------------- | ------------- | ------
ng-scaffold.cors.enabled  | Enable/Disable plugin provided CORS filter | true
ng-scaffold.module.name  | Angular module name | public
ng-scaffold.module.description  | Module description | public
ng-scaffold.base.dir  | Subproject folder name. Used as Gradle sub project | public

## ng-init

Describe ng-init task. Folder structure.

```
grails> help ng-init
| Command: ng-init
| Description:
Copies angular resources in 'public' or the folder path specified in config property 'ng-scaffold.base.dir' of project root dir

| Usage:
grails ng-init

| Flags:
* force - Whether to overwrite existing files

```


```
├── build.gradle
├── favicon.ico
├── gradle.properties
├── gradlew
├── gradlew.bat
├── gulpfile.js
├── karma.conf.js
├── package.json
├── protractor.conf.js
└── src
    ├── 404.html
    ├── app
    │   ├── app.js
    │   ├── components
    │   │   ├── directives
    │   │   │   ├── contentTableWidget.js
    │   │   │   ├── navWidget.js
    │   │   │   ├── relationSelector.js
    │   │   │   ├── sortable.js
    │   │   │   └── views
    │   │   │       ├── contentTableWidget.html
    │   │   │       ├── deleteModel.html
    │   │   │       ├── navWidget.html
    │   │   │       └── relationSelector.html
    │   │   ├── filters
    │   │   │   └── camelCaseToHuman.js
    │   │   └── provider
    │   │       ├── DomainServiceFactory.js
    │   │       └── navServiceProvider.js
    │   ├── index.js
    │   ├── index.scss
    │   ├── layout.html
    │   ├── modules
    │   │   └── application
    │   │       ├── application.controller.js
    │   │       ├── application.resource.js
    │   │       ├── application.route.js
    │   │       └── views
    │   │           ├── jvm.html
    │   │           ├── placeholder.html
    │   │           └── plugins.html
    │   └── table.scss
    ├── favicon.ico
    └── index.html
```


## ng-generate-all

Generate angular artifacts for a grails domain class. Why its slow.

```
grails> help ng-generate-all
| Command: ng-generate-all
| Description:
Generates Angular module with the CRUD artifacts for a domain class

| Usage:
grails ng-generate-all [NAME]

| Arguments:
* Domain Class - Domain class to create required module angular artifact (REQUIRED)

| Flags:
* force - Whether to overwrite existing files

```

## ng-controller

Adds angular controller artifact for a domain

## ng-resource

Adds angular resource service for a grails domain

## ng-route

Adds angular route for a grails domain class

## ng-directive

Adds angular directive

## ng-filter

Adds angular filter

