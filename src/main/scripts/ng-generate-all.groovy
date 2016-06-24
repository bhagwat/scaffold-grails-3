description("Generates Angular artifacts for a domain class") {
    usage "grails ng-generate-all [NAME]"
    completer org.grails.cli.interactive.completers.DomainClassCompleter
    argument name: 'Domain Class', description: "The name of the Domain class to generate artifacts for", required: true
    flag name: 'force', description: "Whether to overwrite existing files"
}

def model = model(args[0])

ngRoute(args.join(' '))
ngResource(args.join(' '))
ngController(args.join(' '))

angularScaffold("${model.fullName}")