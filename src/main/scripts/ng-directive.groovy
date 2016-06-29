description("Creates a new angular directive") {
    usage "grails ng-directive [Directive Name]"
    argument name: 'Directive Name', description: "A valid angular directive name"
    flag name: 'force', description: "Whether to overwrite existing files"
}

model = model(args[0])
def overwrite = flag('force') ? true : false

render template: "angular/directive.gsp",
        destination: file("/public/src/app/components/directives/${model.propertyName}.js"),
        model: model,
        overwrite: overwrite
