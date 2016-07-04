description("Creates a new angular directive") {
    usage "grails ng-directive [Directive Name]"
    argument name: 'Directive Name', description: "A valid angular directive name"
    flag name: 'force', description: "Whether to overwrite existing files"
}

def model = model(args[0])
final Boolean overwrite = flag('force')
final String moduleName = config.getProperty("ng-scaffold.module.name", String) ?: "public"
final String moduleDescription = config.getProperty("ng-scaffold.module.description", String) ?: "Public application"
final String publicFolderPath = config.getProperty("ng-scaffold.base.dir", String) ?: "public"
Map mapModel=[moduleName: moduleName, moduleDescription: moduleDescription]
mapModel.putAll(model.asMap())

render template: "angular/directive.gsp",
        destination: file("${publicFolderPath}/src/app/components/directives/${model.propertyName}.js"),
        model: mapModel,
        overwrite: overwrite
