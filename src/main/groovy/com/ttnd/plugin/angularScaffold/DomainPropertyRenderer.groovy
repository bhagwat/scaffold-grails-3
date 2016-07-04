package com.ttnd.plugin.angularScaffold

import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import grails.util.GrailsClassUtils
import grails.validation.ConstrainedProperty
import groovy.text.markup.MarkupTemplateEngine
import groovy.transform.CompileStatic
import org.grails.validation.DomainClassPropertyComparator
import org.springframework.core.GenericCollectionTypeResolver

import java.sql.Time

@CompileStatic
class DomainPropertyRenderer {
    private ScaffoldTemplateCache scaffoldTemplateCache
    private MarkupTemplateEngine markupTemplateEngine

    private GrailsDomainClassProperty property
    private ConstrainedProperty constrainedProperty
    private GrailsDomainClass domainClass
    private String domainInstanceName

    DomainPropertyRenderer(GrailsDomainClassProperty property, ScaffoldTemplateCache scaffoldTemplateCache, MarkupTemplateEngine markupTemplateEngine) {
        this.scaffoldTemplateCache = scaffoldTemplateCache
        this.markupTemplateEngine = markupTemplateEngine
        this.property = property
        this.domainClass = property.domainClass
        this.constrainedProperty = (ConstrainedProperty) domainClass.constrainedProperties[property.name]
        this.domainInstanceName = domainClass.propertyName
    }

    static List<DomainPropertyRenderer> getDomainProperties(GrailsDomainClass grailsDomainClass, ScaffoldTemplateCache scaffoldTemplateCache, MarkupTemplateEngine markupTemplateEngine) {
        getProperties(grailsDomainClass).collect {
            new DomainPropertyRenderer(it, scaffoldTemplateCache, markupTemplateEngine)
        }
    }

    static List<GrailsDomainClassProperty> getProperties(GrailsDomainClass domainClass) {
        List<GrailsDomainClassProperty> properties = domainClass.persistentProperties as List
        List<String> blacklist = []
        blacklist << 'dateCreated' << 'lastUpdated'
        Map scaffoldProp = (Map) GrailsClassUtils.getStaticPropertyValue(domainClass.clazz, 'scaffold')
        if (scaffoldProp) {
            blacklist.addAll((List) scaffoldProp.get("exclude"))
        }
        properties.removeAll { it.name in blacklist }
        properties.removeAll { !((ConstrainedProperty) it.domainClass.constrainedProperties[it.name])?.display }
        properties.removeAll { it.derived }

        Collections.sort(properties, new DomainClassPropertyComparator(domainClass))
        properties
    }

    public Writer renderEdit() {
        Writer out = new StringWriter()
        if (property.type == Boolean || property.type == boolean)
            out << renderBooleanEditor()
        else if (property.type && Number.isAssignableFrom(property.type) || (property.type?.isPrimitive() && property.type != boolean))
            out << renderNumberEditor()
        else if (property.type == String)
            out << renderStringEditor()
        else if (property.type == Date || property.type == java.sql.Date || property.type == Time || property.type == Calendar)
            out << renderDateEditor()
        else if (property.type == URL)
            out << renderStringEditor()
        else if (property.type && property.isEnum())
            out << renderEnumEditor()
        else if (property.type == TimeZone)
            out << renderSelectTypeEditor("time-zone")
        else if (property.type == Locale)
            out << renderSelectTypeEditor("locale")
        else if (property.type == Currency)
            out << renderSelectTypeEditor("currency")
        else if (property.type == ([] as Byte[]).class) //TODO: Bug in groovy means i have to do this :(
            out << renderByteArrayEditor()
        else if (property.type == ([] as byte[]).class) //TODO: Bug in groovy means i have to do this :(
            out << renderByteArrayEditor()
        else if (property.manyToOne || property.oneToOne)
            out << renderManyToOne()
        else if ((property.oneToMany && !property.bidirectional) || (property.manyToMany && property.isOwningSide())) {
            def str = renderManyToMany()
            if (str != null) {
                out << str
            }
        } else if (property.oneToMany) {
            out << renderOneToMany()
        }
        out
    }

    public Writer renderRead() {
        Writer out = new StringWriter()
        if (property.type == Boolean || property.type == boolean)
            out << "${domainInstanceName}.${property.name}" //TODO: as asBoolean filter
        else if (property.type && Number.isAssignableFrom(property.type) || (property.type?.isPrimitive() && property.type != boolean))
            out << "${domainInstanceName}.${property.name}|number"
        else if (property.type == String)
            out << "${domainInstanceName}.${property.name}"
        else if (property.type == Date || property.type == java.sql.Date || property.type == Time || property.type == Calendar)
            out << "${domainInstanceName}.${property.name}|date"
        else if (property.type == URL)
            out << "${domainInstanceName}.${property.name}"
        else if (property.type && property.isEnum())
            out << "${domainInstanceName}.${property.name}" //TODO: asEnum filter
        else if (property.type == TimeZone)
            out << "${domainInstanceName}.${property.name}"
        else if (property.type == Locale)
            out << "${domainInstanceName}.${property.name}"
        else if (property.type == Currency)
            out << renderSelectTypeEditor("currency")  //TODO: as currency filter
        else if (property.type == ([] as Byte[]).class) //TODO: Bug in groovy means i have to do this :(
            out << "${domainInstanceName}.${property.name}"
        else if (property.type == ([] as byte[]).class) //TODO: Bug in groovy means i have to do this :(
            out << ""
        else if (property.manyToOne || property.oneToOne)
            out << ""
        else if ((property.oneToMany && !property.bidirectional) || (property.manyToMany && property.isOwningSide())) {
            out << ""
        } else if (property.oneToMany) {
            out << ""
        }
        out
    }

    String getMarkup(Map model, String template) {
        updateWithCommonAttributes(model)
        markupTemplateEngine.createTemplate(template).make(attributes: model)
    }

    private String renderTextInputEditor(Map attributes) {
        String template = attributes.type == 'textarea' ? "textarea(attributes){}" : "input(attributes)"
        if (isRequired()) {
            attributes.put("required", "")
        }
        getMarkup(attributes, template)
    }

    private String renderNumberInputEditor() {
        Map model = [type: 'number']
        model.putAll(getConstraints())
        return renderTextInputEditor(model)
    }

    private void updateWithCommonAttributes(Map attributes) {
        String propertyName = property.name
        if (!attributes['id']) {
            attributes['id'] = propertyName
        }
        if (!attributes['name']) {
            attributes['name'] = propertyName
        }
        if (!attributes['data-ng-model']) {
            attributes['data-ng-model'] = "formCtrl.${domainInstanceName}.${propertyName}"
        }
        if (!attributes['class']) {
            attributes['class'] = "form-control col-md-7 col-xs-12"
        }
    }

    private String renderStringEditor() {
        if (!constrainedProperty) {
            return renderTextInputEditor(type: "text")
        }
        if (constrainedProperty.inList) {
            return renderInListEditor()
        }
        Map<String, String> attributes = ["type": "text"]
        if (constrainedProperty.password) {
            attributes.put('type', "password")
        } else if (constrainedProperty.url) {
            attributes.putAll('type': "url")
        } else if (constrainedProperty.email) {
            attributes.putAll(type: 'email')
        }
        attributes.putAll(getConstraints())
        if ("textarea" == constrainedProperty.widget || (constrainedProperty.maxSize > 250 && !constrainedProperty.password && !constrainedProperty.inList)) {
            attributes.put("type", "textarea")
        }
        return renderTextInputEditor(attributes)
    }

    private Map getConstraints() {
        Map attrs = [:]
        if (!constrainedProperty.editable) {
            attrs.put("readonly", "")
        }
        if (constrainedProperty.maxSize) {
            attrs.put("maxlength", constrainedProperty.maxSize.toString())
        }
        if (constrainedProperty.minSize) {
            attrs.put("minlength", constrainedProperty.minSize.toString())
        }
        if (constrainedProperty.min) {
            attrs.put("min", constrainedProperty.min.toString())
        }
        if (constrainedProperty.max) {
            attrs.put("max", constrainedProperty.max.toString())
        }
        if (constrainedProperty.range) {
            attrs.put("max", constrainedProperty.range.to.toString())
            attrs.put("min", constrainedProperty.range.from.toString())
        }
        if (constrainedProperty.scale != null) {
            attrs.put("step", BigDecimal.valueOf(1).movePointLeft(constrainedProperty.scale).toString())
        }
        if (constrainedProperty.matches) {
            attrs.put("pattern", constrainedProperty.matches)
        }
        if (constrainedProperty.creditCard) {
            attrs.put("credit-card-pattern", "")
        }
        attrs
    }

    private String renderByteArrayEditor() {
        renderTextInputEditor(type: "file")
    }

    private String renderManyToOne() {
        if (property.association) {
            Map attributes = [
                    "data-ng-model"    : "formCtrl.${domainInstanceName}.${property.fieldName}.id",
                    "relation-selector": "",
                    "remote-url"       : "/${property.fieldName}/list",
                    "many-to-many"     : "false"
            ]
            return renderSelectBox(attributes, [])
        } else {
            ""
        }
    }

    private String renderManyToMany() {
        Class cls = property.referencedDomainClass?.clazz
        if (cls == null) {
            if (property.type instanceof Collection) {
                cls = GenericCollectionTypeResolver.getCollectionType(property.type)
            }
        }

        if (cls != null) {
            Map attributes = [
                    "relation-selector": "",
                    "remote-url"       : "/${cls.name}/list",
                    "multiple"         : "multiple"
            ]
            return renderSelectBox(attributes, [])
        }
        ""
    }

    private String renderOneToMany() {
        //TODO: to be implemented
/*
        StringWriter sw = new StringWriter()
        PrintWriter pw = new PrintWriter(sw)
        pw.println()
        pw.println '<ul class="one-to-many">'
        pw.println "<g:each in=\"\${${domainInstanceName}?.${property.name}?}\" var=\"${property.name[0]}\">"
        pw.println "    <li><g:link controller=\"${property.referencedDomainClass.propertyName}\" action=\"show\" id=\"\${${property.name[0]}.id}\">\${${property.name[0]}?.encodeAsHTML()}</g:link></li>"
        pw.println '</g:each>'
        pw.println '<li class="add">'
        pw.println "<g:link controller=\"${property.referencedDomainClass.propertyName}\" action=\"create\" params=\"['${domainClass.propertyName}.id': ${domainInstanceName}?.id]\">\${message(code: 'default.add.label', args: [message(code: '${property.referencedDomainClass.propertyName}.label', default: '${property.referencedDomainClass.shortName}')])}</g:link>"
        pw.println '</li>'
        pw.println '</ul>'
        return sw.toString()
*/
        return ""
    }

    private String renderRangeSelector(Range range) {
        Map model = ["ng-options": "number for number in [] | range:${range.from}:${range.to} track by \$index".toString()]
        if (required) {
            model.put("required", "")
        }
        return getMarkup(model, "select(attributes)")
    }

    private String renderNumberEditor() {
        if (!constrainedProperty) {
            if (property.type == Byte) {
                return renderRangeSelector(-128..127)
            } else {
                return renderNumberInputEditor()
            }
        } else {
            if (constrainedProperty.range) {
                return renderRangeSelector(constrainedProperty.range)
            } else if (constrainedProperty.inList) {
                return renderInListEditor()
            } else {
                return renderNumberInputEditor()
            }
        }
    }

    private String renderBooleanEditor() {
        Map model = ["type": "checkbox", "ng-true-value": "true", "ng-false-value": "false"]
        if (constrainedProperty && constrainedProperty.widget) {
            model.putAll([widget: constrainedProperty.widget])
            model.putAll(constrainedProperty.attributes)
        }
        return renderTextInputEditor(model)
    }

    private String renderDateEditor() {
        String propertyName = property.name
        scaffoldTemplateCache.renderWidget(attributes: [
                "type"                : "text",
                "id"                  : propertyName,
                "name"                : propertyName,
                "data-ng-model"       : "formCtrl.${domainInstanceName}.${propertyName}",
                "uib-datepicker-popup": "yyyy-MM-dd",
                "is-open"             : "formCtrl.datePopupOpen.${propertyName}",
                "data-ng-click"       : "formCtrl.datePopupOpen.${propertyName}=!formCtrl.datePopupOpen.${propertyName}"
        ], "widget/datepicker",)
    }

    private String renderSelectTypeEditor(type) {
        Map model = [:]
        if (required) {
            model.put("required", "")
        }
        return getMarkup(model, "'${type}-selector'(attributes){}")
    }

    private String renderEnumEditor() {
        renderSelectBox(property.type.getEnumConstants() as List) {
            [id: ((Enum) it).name(), name: ((Enum) it).toString()]
        }
    }

    private String renderInListEditor() {
        renderSelectBox(constrainedProperty.inList) {
            [id: it.toString(), name: it.toString()]
        }
    }

    private String renderSelectBox(List items, Closure closure) {
        renderSelectBox([:], items.collect(closure))
    }

    private String renderSelectBox(Map attributes, List options) {
        updateWithCommonAttributes(attributes)
        if (isRequired()) {
            attributes.put("required", "")
        }
        scaffoldTemplateCache.renderWidget(attributes: attributes, options: options, "widget/selectbox",)
    }

    public boolean isRequired() {
        !isOptional()
    }

    private boolean isOptional() {
        if (!constrainedProperty) {
            return false
        } else {
            constrainedProperty.nullable || (constrainedProperty.propertyType == String && constrainedProperty.blank) || constrainedProperty.propertyType in [boolean, Boolean]
        }
    }
}
