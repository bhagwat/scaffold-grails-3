description("Creates a new angular directive") {
    usage "grails ng-directive [Directive Name]"
    argument name: 'Directive Name', description: "A valid angular directive name"
    flag name: 'force', description: "Whether to overwrite existing files"
}

model = model(args[0])
def overwrite = flag('force') ? true : false
final String moduleName = config.getProperty("ng-scaffold.module.name", String) ?: "public"
final String moduleDescription = config.getProperty("ng-scaffold.module.description", String) ?: "Public application"
final String publicFolderPath = config.getProperty("ng-scaffold.base.dir", String) ?: "public"
Map mapModel=[moduleName: moduleName, moduleDescription: moduleDescription]
mapModel.putAll(model.asMap())

render template: "angular/directive.gsp",
        destination: file("${publicFolderPath}/src/app/components/directives/${model.propertyName}.js"),
        model: mapModel,
        overwrite: overwrite
