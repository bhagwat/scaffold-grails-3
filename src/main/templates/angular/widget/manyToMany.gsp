ul{
    li("data-ng-repeat": "item in ${domainInstanceName}.${property?.name}"){
        a("data-ui-sref": "home.${property.referencedDomainClass?.propertyName}.edit({id: item.id})"){
            span( "data-ng-bind": "item|json"){
            }
        }
    }
}