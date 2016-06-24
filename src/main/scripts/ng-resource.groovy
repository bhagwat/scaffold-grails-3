description("Generates Angular artifacts for a domain class") {
    usage "grails ng-resource [NAME]"
    completer org.grails.cli.interactive.completers.DomainClassCompleter
    argument name: 'Domain Class', description: "The name of the Domain class to generate artifacts for", required: true
    flag name: 'force', description: "Whether to overwrite existing files"
}

model = model(args[0])

render template: "angular/resource.gsp",
        destination: file("/public/src/app/modules/${model.propertyName}/${model.propertyName}.resource.js"),
        model: model