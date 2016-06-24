description("Generates Angular artifacts for a domain class") {
    usage "grails create-ng-directive [NAME]"
    completer org.grails.cli.interactive.completers.DomainClassCompleter
    argument name: 'Domain Class', description: "The name of the Domain class to generate artifacts for", required: true
    flag name: 'force', description: "Whether to overwrite existing files"
}

def model = model(args[0])
boolean overwrite = flag('force')
println model
angularScaffold("${args.join(' ')}")