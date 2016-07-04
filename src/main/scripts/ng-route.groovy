description("Generates Angular route for a domain class") {
    usage "grails ng-route [NAME]"
    completer org.grails.cli.interactive.completers.DomainClassCompleter
    argument name: 'Domain Class', description: "Domain class to create required module angular artifact", required: true
    flag name: 'force', description: "Whether to overwrite existing files"
}

def model = model(args[0])
final Boolean overwrite = flag('force')
final String moduleName = config.getProperty("ng-scaffold.module.name", String) ?: "public"
final String moduleDescription = config.getProperty("ng-scaffold.module.description", String) ?: "Public application"
final String publicFolderPath = config.getProperty("ng-scaffold.base.dir", String) ?: "public"
Map mapModel=[moduleName: moduleName, moduleDescription: moduleDescription]
mapModel.putAll(model.asMap())

render template: "angular/route.gsp",
        destination: file("${publicFolderPath}/src/app/modules/${model.propertyName}/${model.propertyName}.route.js"),
        model: mapModel,
        overwrite: overwrite