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
        else if (property.type == ([] as Byte[]).class || property.type == ([] as byte[]).class)
            out << renderByteArrayEditor()
        else if (property.manyToOne || property.oneToOne)
            out << renderManyToOne()
        else if ((property.oneToMany && !property.bidirectional) || (property.manyToMany && property.isOwningSide())) {
            out << renderManyToMany()
        } else if (property.oneToMany) {
            out << renderOneToMany()
        }
        out
    }

    public Writer renderRead() {
        Writer out = new StringWriter()
        if (property.type == Boolean || property.type == boolean)
            out << "{{${domainInstanceName}.${property.name}}}"
        else if (property.type && Number.isAssignableFrom(property.type) || (property.type?.isPrimitive() && property.type != boolean))
            out << "{{${domainInstanceName}.${property.name}|number}}"
        else if (property.type == String)
            out << "{{${domainInstanceName}.${property.name}}}"
        else if (property.type == Date || property.type == java.sql.Date || property.type == Time || property.type == Calendar)
            out << "{{${domainInstanceName}.${property.name}|date}}"
        else if (property.type == URL)
            out << "{{${domainInstanceName}.${property.name}}}"
        else if (property.type && property.isEnum())
            out << "{{${domainInstanceName}.${property.name}.name}}"
        else if (property.type == TimeZone)
            out << "{{${domainInstanceName}.${property.name}}}"
        else if (property.type == Locale)
            out << "{{${domainInstanceName}.${property.name}}}"
        else if (property.type == Currency)
            out << "{{${domainInstanceName}.${property.name}|currency}}"
        else if (property.type == ([] as Byte[]).class || property.type == ([] as byte[]).class)
            out << "{{${domainInstanceName}.${property.name}}}"
        else if (property.manyToOne || property.oneToOne) {
            out << renderTemplate(
                    domainInstanceName: domainInstanceName,
                    property: property,
                    "widget/association")
        } else if ((property.oneToMany && !property.bidirectional) || (property.manyToMany && property.isOwningSide())) {
            out << renderTemplate(
                    domainInstanceName: domainInstanceName,
                    property: property,
                    "widget/manyToMany")
        } else if (property.oneToMany) {
            out << renderTemplate(
                    domainInstanceName: domainInstanceName,
                    property: property,
                    "widget/manyToMany")
        }
        out
    }

    String renderTemplate(Map model, String template) {
        scaffoldTemplateCache.renderWidget(model, template)
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
            Map attributes = getRelationWidgetAttributes()
            attributes.put("many-to-many", "false")
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
            Map attributes = getRelationWidgetAttributes()
            attributes.put("multiple", "multiple")
            return renderSelectBox(attributes, [])
        } else {
            return ""
        }
    }

    private Map getRelationWidgetAttributes() {
        [
                "data-ng-model"  : "formCtrl.${domainInstanceName}.${property.name}",
                "data-remote-url": "/${property.referencedDomainClass.propertyName}/autoComplete",
                "class"          : "form-control col-md-7 col-xs-12",
                "id"             : property.name,
                "name"           : property.name,
                "data-options"   : "formCtrl.${property.name}.options",
                "data-ng-options": "option as option.display for option in formCtrl.${property.name}.options track by option.id"
        ]
    }

    private String renderOneToMany() {
        renderManyToMany()
    }

    private String renderRangeSelector(Range range) {
        Map model = ["data-ng-options": "number for number in [] | range:${range.from}:${range.to} track by \$index".toString()]
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
        Map model = ["type": "checkbox", "data-ng-true-value": "true", "data-ng-false-value": "false"]
        if (constrainedProperty && constrainedProperty.widget) {
            model.putAll([widget: constrainedProperty.widget])
            model.putAll(constrainedProperty.attributes)
        }
        return renderTextInputEditor(model)
    }

    private String renderDateEditor() {
        String propertyName = property.name
        Map model = [
                "data-ng-model"            : "formCtrl.${domainInstanceName}.${propertyName}",
                "id"                       : propertyName,
                "name"                     : propertyName,
                "type"                     : "text",
                "class"                    : "form-control well well-sm",
                "data-uib-datepicker-popup": "",
                "data-is-open"             : "formCtrl.datePopupOpen.${propertyName}",
                "data-ng-click"            : "formCtrl.datePopupOpen.${propertyName}=!formCtrl.datePopupOpen.${propertyName}"
        ]
        if (required) {
            model.put("required", "")
        }
        scaffoldTemplateCache.renderWidget(attributes: model, "widget/datepicker",)
    }

    private String renderSelectTypeEditor(type) {
        Map model = [:]
        if (required) {
            model.put("required", "")
        }
        return getMarkup(model, "'${type}-selector'(attributes){}")
    }

    private String renderEnumEditor() {
        renderSelectBox('data-ng-model': "formCtrl.${domainInstanceName}.${property.name}.name", property.type.getEnumConstants() as List) {
            [id: ((Enum) it).name(), name: ((Enum) it).toString()]
        }
    }

    private String renderInListEditor() {
        renderSelectBox([:], constrainedProperty.inList) {
            [id: it.toString(), name: it.toString()]
        }
    }

    private String renderSelectBox(Map attributes, List items, Closure closure) {
        renderSelectBox(attributes, items.collect(closure))
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
