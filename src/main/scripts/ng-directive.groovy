description("Creates a new angular directive") {
    usage "grails ng-directive [Directive Name]"
    argument name: 'Directive Name', description: "A valid angular directive name"
}

model = model(args[0])

render template: "angular/directive.gsp",
        destination: file("/public/src/app/components/directives/${model.propertyName}.js"),
        model: model
