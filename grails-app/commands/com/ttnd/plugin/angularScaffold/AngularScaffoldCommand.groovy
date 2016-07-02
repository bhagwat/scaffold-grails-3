package com.ttnd.plugin.angularScaffold

import grails.core.GrailsApplication
import grails.core.GrailsDomainClass
import grails.dev.commands.ApplicationCommand
import grails.dev.commands.ExecutionContext
import grails.util.BuildSettings
import groovy.text.markup.MarkupTemplateEngine
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

@CompileStatic
class AngularScaffoldCommand implements ApplicationCommand {
    @Autowired
    GrailsApplication grailsApplication

    @Autowired
    ScaffoldTemplateCache scaffoldTemplateCache

    @Autowired
    MarkupTemplateEngine markupTemplateEngine

    @Value('${ng-scaffold.module.name}')
    String moduleName

    @Value('${ng-scaffold.module.description}')
    String moduleDescription

    @Value('${ng-scaffold.base.dir:public}')
    String publicBaseDir

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
            println "| Generating angular artifacts for domain: ${grailsDomainClass.naturalName} ..."
            List<DomainPropertyRenderer> domainProperties = DomainPropertyRenderer.getDomainProperties(grailsDomainClass, scaffoldTemplateCache, markupTemplateEngine)
            Map model = [
                    domainClass      : grailsDomainClass,
                    className        : grailsDomainClass.naturalName,
                    propertyName     : grailsDomainClass.propertyName,
                    fields           : domainProperties,
                    moduleName       : moduleName,
                    moduleDescription: moduleDescription
            ]
            renderTemplate(model, "list", grailsDomainClass, ctx.commandLine.hasOption("force"))
            renderTemplate(model, "form", grailsDomainClass, ctx.commandLine.hasOption("force"))
        }
        return true
    }

    private void renderTemplate(Map model, String template, GrailsDomainClass grailsDomainClass, Boolean override) {
        String destinationFilePath = "$publicBaseDir/src/app/modules/${grailsDomainClass.propertyName}/views/${grailsDomainClass.propertyName}.${template}.html"
        File destinationFile = new File(BuildSettings.BASE_DIR, destinationFilePath)
        if (!override && destinationFile.exists()) {
            println "| Warning Destination file $destinationFilePath already exists, skipping..."
            return
        }
        destinationFile.parentFile.mkdirs()
        scaffoldTemplateCache.renderAsString(model, template, destinationFile)
        println "| Rendered template ${template}.gsp to destination $destinationFilePath"
    }
}
