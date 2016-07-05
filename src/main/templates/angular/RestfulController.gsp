package ${packagePath}

import grails.rest.*
import grails.converters.*

class ${className}Controller extends RestfulController<${className}> {
    static responseFormats = ['json']

    ${className}Controller() {
        super(${className})
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond instances: ${className}.list(params), total: ${className}.count()
    }

    def autoComplete(Integer max) {
        params.max = Math.min(max ?: 50, 100)
        respond ${className}.createCriteria().list(params){}.collect{
            [id: it.id, display: it.toString()]
        }
    }
}