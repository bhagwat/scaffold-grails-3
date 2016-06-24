description("Generates Angular artifacts for a domain class") {
    usage "grails ng-controller [Domain]"
    completer org.grails.cli.interactive.completers.DomainClassCompleter
    argument name: 'Domain Class', description: "The name of the Domain class to generate controller for", required: true
    flag name: 'force', description: "Whether to overwrite existing files"
}

def model = model(args[0])
boolean overwrite = flag('force')

render template: "angular/form.controller.gsp",
        destination: file("/public/src/app/modules/${model.propertyName}/${model.propertyName}.form.controller.js"),
        model: model

render template: "angular/list.controller.gsp",
        destination: file("/public/src/app/modules/${model.propertyName}/${model.propertyName}.list.controller.js"),
        model: model
