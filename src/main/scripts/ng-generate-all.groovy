description("Generates Angular module with the CRUD artifacts for a domain class") {
    usage "grails ng-generate-all [NAME]"
    completer org.grails.cli.interactive.completers.DomainClassCompleter
    argument name: 'Domain Class', description: "Domain class to create required module angular artifact", required: true
    flag name: 'force', description: "Whether to overwrite existing files"
}

def model = model(args[0])

ngRoute(*args)
ngResource(*args)
ngController(*args)
ngRestfulController(*args)

angularScaffold("${model.fullName}")