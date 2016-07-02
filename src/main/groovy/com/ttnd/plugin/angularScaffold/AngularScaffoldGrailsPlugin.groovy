package com.ttnd.plugin.angularScaffold

import grails.plugins.Plugin
import groovy.text.GStringTemplateEngine
import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration
import groovy.util.logging.Slf4j

@Slf4j
class AngularScaffoldGrailsPlugin extends Plugin {
    def grailsVersion = "3.1.0 > *"
    def pluginExcludes = ["grails-app/views/error.gsp"]

    def title = "Angular Scaffold" // Headline display name of the plugin
    def author = "Bhagwat Kumar"
    def authorEmail = "bhagwat.kumar@tothenew.com"
    def description = 'Generates scaffolded code for SPA using Angularjs'
    def profiles = ['plugin']
    def documentation = "http://grails.org/plugin/angular-scaffold"
    def license = "APACHE"
    def organization = [name: "TO THE NEW Digital", url: "http://www.tothenew.com"]
    def developers = [[name: "Sagarmal Sankara", email: "sagarmal.sankar@tothenew.com"]]
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    Closure doWithSpring() {
        { ->
            scaffoldTemplateConfiguration(TemplateConfiguration) {
                autoEscape = true
                autoIndent = true
                autoNewLine = true
                useDoubleQuotes = true
            }

            scaffoldTemplateCache(ScaffoldTemplateCache)
            scaffoldGStringTemplateEngine(GStringTemplateEngine)
            scaffoldMarkupTemplateEngine(MarkupTemplateEngine, ref('scaffoldTemplateConfiguration'))

            if (config.getProperty("ng-scaffold.cors.enabled", Boolean, true)) {
                log.debug "Adding once per request CORS filter"
                applicationCorsFilter(ApplicationCorsFilter)
            } else {
                log.debug "Plugin provided CORS filter is disabled.Set `ng-scaffold.cors.enabled=true` to enable it."
            }
        }
    }
}
