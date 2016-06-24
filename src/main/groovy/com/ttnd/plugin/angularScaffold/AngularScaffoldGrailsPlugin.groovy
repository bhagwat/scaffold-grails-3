package com.ttnd.plugin.angularScaffold

import grails.plugins.Plugin
import groovy.text.GStringTemplateEngine
import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration

class AngularScaffoldGrailsPlugin extends Plugin {
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1.6 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def title = "Angular Scaffold" // Headline display name of the plugin
    def author = "Bhagwat Kumar"
    def authorEmail = "bhagwat.kumar@tothenew.com"
    def description = '''\
Generates scaffolded code for SPA using Angularjs
'''
    def profiles = ['plugin']

    def documentation = "http://grails.org/plugin/angular-scaffold"

    def license = "APACHE"

    def organization = [name: "TO THE NEW Digital", url: "http://www.tothenew.com"]

    def developers = [[name: "Sagarmal Sankara", email: "sagarmal.sankar@tothenew.com"]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
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
        // TODO Implement registering dynamic methods to classes (optional)
    }

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
