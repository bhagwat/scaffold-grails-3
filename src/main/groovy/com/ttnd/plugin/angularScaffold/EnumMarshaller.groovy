package com.ttnd.plugin.angularScaffold

import grails.converters.JSON
import org.grails.web.converters.exceptions.ConverterException
import org.grails.web.converters.marshaller.ObjectMarshaller
import org.springframework.beans.BeanUtils

import java.lang.reflect.Method

public class EnumMarshaller implements ObjectMarshaller<JSON> {
    public boolean supports(Object object) {
        return object.getClass().isEnum();
    }

    public void marshalObject(Object obj, JSON json) throws ConverterException {
        try {
            Method nameMethod = BeanUtils.findDeclaredMethod(obj.getClass(), "name", null)
            json.value(nameMethod.invoke(obj))
        } catch (Exception e) {
            json.value("")
            throw new ConverterException("Error converting Enum with class " + obj.getClass().name, e)
        }
    }
}
