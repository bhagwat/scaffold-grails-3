package com.ttnd.plugin.angularScaffold

import grails.core.GrailsApplication
import grails.core.GrailsDomainClass
import grails.dev.commands.ApplicationCommand
import grails.dev.commands.ExecutionContext
import groovy.text.markup.MarkupTemplateEngine
import org.springframework.beans.factory.annotation.Autowired

class AngularScaffoldCommand implements ApplicationCommand {
    @Autowired
    GrailsApplication grailsApplication

    @Autowired
    ScaffoldTemplateCache scaffoldTemplateCache

    @Autowired
    MarkupTemplateEngine markupTemplateEngine

    boolean handle(ExecutionContext ctx) {
        String[] args = ctx.commandLine.remainingArgsArray
        if (args.length < 1) {
            println "Usage:"
            println "\tgrails angular-scaffold [Domain Class]"
            println "\tgrails angular-generate-all [Domain Class] #with domain name auto complete"
            return false
        }
        List<GrailsDomainClass> grailsDomainClasses = args.collect { String className ->
            GrailsDomainClass grailsDomainClass = (GrailsDomainClass) grailsApplication.getArtefact("Domain", className)
            if (!grailsDomainClass) {
                println "No such domain class found: ${className}"
            }
            grailsDomainClass
        }
        if (grailsDomainClasses.any { !it }) {
            return false
        }

        grailsDomainClasses.each { GrailsDomainClass grailsDomainClass ->
            println "Generating angular artifacts for domain for ${grailsDomainClass.naturalName} ..."
            List<DomainPropertyRenderer> domainProperties = DomainPropertyRenderer.getDomainProperties(grailsDomainClass, scaffoldTemplateCache, markupTemplateEngine)
            Map model = [domainClass: grailsDomainClass, className: grailsDomainClass.naturalName, propertyName: grailsDomainClass.propertyName, fields: domainProperties]
            scaffoldTemplateCache.renderAsString(model, "list",
                    "modules/${grailsDomainClass.propertyName}/views/${grailsDomainClass.propertyName}.list.html")
            scaffoldTemplateCache.renderAsString(model, "form",
                    "modules/${grailsDomainClass.propertyName}/views/${grailsDomainClass.propertyName}.form.html")
        }
        return true
    }
}
