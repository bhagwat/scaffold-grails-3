package com.ttnd.plugin.angularScaffold

import grails.core.ArtefactHandler
import grails.core.GrailsApplication
import grails.core.GrailsClass
import grails.plugins.GrailsPlugin
import grails.plugins.GrailsPluginManager
import grails.plugins.PluginManagerAware
import grails.util.Environment
import grails.util.GrailsUtil
import org.grails.core.artefact.ControllerArtefactHandler

class ApplicationMetricController implements PluginManagerAware {
    static responseFormats = ["json"]

    GrailsApplication grailsApplication
    GrailsPluginManager pluginManager

    def index() {
        render(contentType: "application/json") {
            info(
                    "Environment": Environment.current.name,
                    "App version": grailsApplication.metadata.getApplicationVersion(),
                    "Grails version": GrailsUtil.grailsVersion,
                    "App Profile": grailsApplication.config.getProperty('grails.profile'),
                    "Groovy version": GroovySystem.getVersion(),
                    "JVM version": System.getProperty('java.version'),
                    "Reloading agent enabled": Environment.reloadingAgentEnabled
            )
            artefacts grailsApplication.artefactHandlers, { ArtefactHandler artefactHandler ->
                type artefactHandler.type
                size grailsApplication.getArtefactInfo(artefactHandler.type).classesByName.size()
            }
            controllers grailsApplication.getArtefacts(ControllerArtefactHandler.TYPE), { GrailsClass c ->
                name c.fullName
                logicalPropertyName c.logicalPropertyName
            }
            plugins pluginManager.allPlugins, { GrailsPlugin plugin ->
                Map prop = plugin.properties
                name plugin.name
                version plugin.version
                enabled plugin.enabled
                title prop?.title
                description prop?.description
                link prop?.documentation
            }
        }
    }
}