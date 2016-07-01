description("Generates Angular artifacts for a domain class") {
    usage "grails ng-resource [NAME]"
    completer org.grails.cli.interactive.completers.DomainClassCompleter
    argument name: 'Domain Class', description: "Domain class to create required module angular artifact", required: true
    flag name: 'force', description: "Whether to overwrite existing files"
}

model = model(args[0])
def overwrite = flag('force') ? true : false
final String moduleName = config.getProperty("ng-scaffold.module.name", String) ?: "public"
final String moduleDescription = config.getProperty("ng-scaffold.module.description", String) ?: "Public application"
final String publicFolderPath = config.getProperty("ng-scaffold.base.dir", String) ?: "public"
Map mapModel=[moduleName: moduleName, moduleDescription: moduleDescription]
mapModel.putAll(model.asMap())

render template: "angular/resource.gsp",
        destination: file("${publicFolderPath}/src/app/modules/${model.propertyName}/${model.propertyName}.resource.js"),
        model: mapModel,
        overwrite: overwrite