/*
 * Copyright 2011 Kevin Seim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.beanio.types.xml;

import java.util.*;

import javax.xml.datatype.*;
import javax.xml.namespace.QName;

/**
 * A <code>java.util.Date</code> type handler implementation for parsing dates based on
 * the W3C XML Schema <a href="http://www.w3.org/TR/xmlschema-2/#date">date datatype</a>
 * specification.
 * 
 * @author Kevin Seim
 * @since 1.1
 */
public class XmlDateTypeHandler extends AbstractXmlDateTypeHandler {

    @Override
    public String format(Object value) {
        if (value == null) {
            return null;
        }
        
        Date date = (Date) value;
        if (pattern != null) {
            return super.formatDate(date);
        }
        
        Calendar cal = newCalendar();
        cal.setTime(date);
        
        XMLGregorianCalendar xcal = dataTypeFactory.newXMLGregorianCalendarDate(
            cal.get(Calendar.YEAR), 
            cal.get(Calendar.MONTH) + 1, 
            cal.get(Calendar.DATE), 
            getTimeZoneOffset((Date)value));
        
        return xcal.toXMLFormat();
    }
    
    @Override
    protected QName getDatatypeQName() {
        return DatatypeConstants.DATE;
    }
}
