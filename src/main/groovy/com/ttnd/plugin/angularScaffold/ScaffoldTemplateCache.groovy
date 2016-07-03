package com.ttnd.plugin.angularScaffold

import grails.util.BuildSettings
import groovy.text.GStringTemplateEngine
import groovy.text.TemplateEngine
import groovy.text.markup.MarkupTemplateEngine
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ResourceLoaderAware
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader

@CompileStatic
class ScaffoldTemplateCache implements ResourceLoaderAware {
    @Autowired(required = false)
    ResourceLoader resourceLoader

    @Autowired(required = false)
    GStringTemplateEngine scaffoldGStringTemplateEngine

    @Autowired(required = false)
    MarkupTemplateEngine scaffoldMarkupTemplateEngine

    private final String SOURCE_TEMPLATE_FOLDER_PATH = "src/main/templates/angular/"
    private final String SOURCE_TEMPLATE_META_INF_PATH = "classpath:META-INF/templates/angular/"

    public File renderAsString(Map model, String source, File destinationFile) {
        Writable writable = getWritable(scaffoldGStringTemplateEngine, model, source)
        destinationFile.withWriter {
            writable.writeTo(it)
        }
        return destinationFile
    }

    public StringWriter renderWidget(Map model, String widgetPath) {
        return render(scaffoldMarkupTemplateEngine, model, widgetPath)
    }

    private StringWriter render(TemplateEngine templateEngine, Map model, String source) {
        StringWriter stringWriter = new StringWriter()
        getWritable(templateEngine, model, source).writeTo(stringWriter)
        return stringWriter
    }

    private Writable getWritable(TemplateEngine templateEngine, Map model, String source) {
        templateEngine.createTemplate(getResource(source).URL).make(model)
    }

    private Resource getResource(String path) {
        Resource resource = new FileSystemResource(new File(BuildSettings.BASE_DIR, "${SOURCE_TEMPLATE_FOLDER_PATH}${path}.tpl"))
        if (!resource.exists()) {
            resource = resourceLoader.getResource("${SOURCE_TEMPLATE_META_INF_PATH}${path}.gsp")
        }
        if (!resource.exists()) {
            throw new FileNotFoundException("Requested template ${path} does not found.")
        }
        return resource
    }
}
