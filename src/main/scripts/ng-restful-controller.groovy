description("Creates a REST controller") {
    usage "grails create-restful-controller [controller name]"
    completer org.grails.cli.interactive.completers.DomainClassCompleter
    argument name: 'Domain Class', description: "The name of the domain class", required: true
    flag name: 'force', description: "Whether to overwrite existing files"
}

def sourceClass = source(args[0])
boolean overwrite = flag('force')
if (sourceClass) {
    def model = model(sourceClass)
    render template: template('angular/RestfulController.gsp'),
            destination: file("grails-app/controllers/${model.packagePath}/${model.convention('Controller')}.groovy"),
            model: [packagePath: model.packagePath, className: model.simpleName],
            overwrite: overwrite
} else {
    error "Domain class not found for name $arg"
}