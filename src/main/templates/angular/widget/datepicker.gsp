p(class: 'input-group'){
    input(attributes)
    span(class:'input-group-btn'){
        button(type: 'button', class: "btn btn-default", "data-ng-click": attributes["data-ng-click"]){
            i(class: "glyphicon glyphicon-calendar"){}
        }
    }
}