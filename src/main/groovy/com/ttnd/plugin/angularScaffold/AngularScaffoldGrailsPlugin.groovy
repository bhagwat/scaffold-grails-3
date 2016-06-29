package com.ttnd.plugin.angularScaffold

import grails.plugins.Plugin
import groovy.text.GStringTemplateEngine
import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration

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
            scaffoldGStringTemplateEngine(GStringTemplateEngine)
            scaffoldMarkupTemplateEngine(MarkupTemplateEngine, ref('scaffoldTemplateConfiguration'))
            scaffoldTemplateCache(ScaffoldTemplateCache)
        }
    }

    void doWithDynamicMethods() {
    }

    void doWithApplicationContext() {
    }

    void onChange(Map<String, Object> event) {
    }

    void onConfigChange(Map<String, Object> event) {
    }

    void onShutdown(Map<String, Object> event) {
    }
}
