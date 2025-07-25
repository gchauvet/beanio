/*
 * Copyright 2010-2013 Kevin Seim
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
package org.beanio.types;

import java.text.*;
import java.util.*;

/**
 * This type handler uses a <code>SimpleDateFormat</code> class to parse and format 
 * <code>java.util.Date</code> objects.  If no pattern is set, <code>DateFormat.getInstance()</code> 
 * is used to create a default date format.  By default, <code>lenient</code> is false.
 * 
 * @author Kevin Seim
 * @since 1.0
 * @see Date
 * @see DateFormat
 * @see SimpleDateFormat
 */
public class DateTypeHandler extends DateTypeHandlerSupport {

    /**
     * Constructs a new <code>DateTypeHandler</code>.
     */
    public DateTypeHandler() { }

    /**
     * Constructs a new <code>DateTypeHandler</code>.
     * @param pattern the <code>SimpleDateFormat</code> pattern
     */
    public DateTypeHandler(String pattern) {
        super(pattern);
    }
    
    /*
     * (non-Javadoc)
     * @see org.beanio.types.TypeHandler#parse(java.lang.String)
     */
    @Override
    public Date parse(String text) throws TypeConversionException {
        return super.parseDate(text);
    }
    
    /*
     * (non-Javadoc)
     * @see org.beanio.types.TypeHandler#format(java.lang.Object)
     */
    @Override
    public String format(Object value) {
        return super.formatDate((Date)value);
    }
    
    /*
     * (non-Javadoc)
     * @see org.beanio.types.TypeHandler#getType()
     */
    @Override
    public Class<?> getType() {
        return Date.class;
    }
}
