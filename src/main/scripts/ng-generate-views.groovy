description("Generates Angular html templates for a domain class") {
    usage "grails ng-generate-views [NAME]"
    completer org.grails.cli.interactive.completers.DomainClassCompleter
    argument name: 'Domain Class', description: "Domain class to create required HTML templates", required: true
    flag name: 'force', description: "Whether to overwrite existing files"
}

def model = model(args[0])

angularScaffold("${model.fullName}")