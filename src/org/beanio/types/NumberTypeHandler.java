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

import java.math.*;
import java.text.*;
import java.util.Properties;

/**
 * Base class for type handlers that parse objects extending from <code>Number</code>.  If a
 * <code>pattern</code> is set, a <code>DecimalFormat</code> is used to parse and format the value.
 * Otherwise, the value is parsed and formatted using the <code>Number</code> subclass
 * specific to this type handler.
 * 
 * @author Kevin Seim
 * @since 1.0
 * @see DecimalFormat
 */
public abstract class NumberTypeHandler extends LocaleSupport implements ConfigurableTypeHandler, Cloneable {

    private String pattern;
    
    // the same format instance can be reused if this type handler is not shared
    // by multiple unmarshallers/marshallers, this can lead to significant
    // performance improvements if parsing thousands of records
    private transient ThreadLocal<DecimalFormat> format = new ThreadLocal<>();

    /**
     * Parses a <code>Number</code> from the given text.
     * @param text the text to parse
     * @return the parsed Number, or null if <code>text</code> was <code>null</code>
     *    or an empty string
     * @throws TypeConversionException if the text is not a valid number
     */
    @Override
    public final Number parse(String text) throws TypeConversionException {
        if (text == null || "".equals(text)) {
            return null;
        }

        if (pattern == null) {
            
            try {
                return createNumber(text);
            }
            catch (NumberFormatException ex) {
                throw new TypeConversionException("Invalid " + getType().getSimpleName() +
                    " value '" + text + "'", ex);
            }
            
        }
        else {
            // create a DecimaFormat for parsing the number
            DecimalFormat df = format.get();
            if (df == null) {
                df = createDecimalFormat();
                df.setParseBigDecimal(true);
            }
            
            // parse the number using the DecimalFormat
            ParsePosition pp = new ParsePosition(0);
            Number number = df.parse(text, pp);
            if (pp.getErrorIndex() >= 0 || 
                pp.getIndex() != text.length() ||
                !(number instanceof BigDecimal))
            {
                throw new TypeConversionException("Number value '" + text + 
                    "' does not match pattern '" + pattern + "'");
            }
            
            try {
                // convert the BigDecimal to a number
                return createNumber((BigDecimal)number);
            }
            catch (ArithmeticException ex) {
                throw new TypeConversionException("Invalid " + getType().getSimpleName() + 
                    " value '" + text + "'");
            }
            
        }
    }
    
    /**
     * Parses a <code>Number</code> from text.
     * @param text the text to convert to a Number
     * @return the parsed <code>Number</code>
     * @throws NumberFormatException if the text is not a valid number
     */
    protected abstract Number createNumber(String text) throws NumberFormatException;

    /**
     * Parses a <code>Number</code> from a <code>BigDecimal</code>.
     * @param bg the <code>BigDecimal</code> version of the number
     * @return the parsed <code>Number</code>
     * @throws ArithmeticException if the <code>BigDecimal</code> cannot be converted
     *   to the <code>Number</code> type supported by this handler
     */
    protected abstract Number createNumber(BigDecimal bg) throws ArithmeticException;
    
    /*
     * (non-Javadoc)
     * @see org.beanio.types.ConfigurableTypeHandler#newInstance(java.util.Properties)
     */
    @Override
    public TypeHandler newInstance(Properties properties) throws IllegalArgumentException {
        String pattern = properties.getProperty(FORMAT_SETTING);
        if (pattern == null || "".equals(pattern)) {
            return this;
        }
        
        try {
            NumberTypeHandler handler = (NumberTypeHandler) this.clone();
            handler.setPattern(pattern);
            handler.format = ThreadLocal.withInitial(() -> {
                DecimalFormat df = handler.createDecimalFormat();
                df.setParseBigDecimal(true);
                return df;
            });
            return handler;
        }
        catch (CloneNotSupportedException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Creates a <code>DecimalFormat</code> for parsing and formatting the number value.
     * @return the new <code>DecimalFormat</code>
     */
    protected DecimalFormat createDecimalFormat() {
        return new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale));
    }
    
    /**
     * Formats a <code>Number</code> by calling <code>toString()</code>.  If <code>value</code> is
     * null, <code>null</code> is returned.
     * @param value the number to format
     * @return the formatted number
     */
    @Override
    public String format(Object value) {
        if (value == null)
            return null;
        else if (pattern == null)
            return ((Number) value).toString();
        else if (format.get() != null)
            return format.get().format(value);
        else
            return createDecimalFormat().format(value);
    }

    /**
     * Returns the <code>DecimalFormat</code> pattern to use to parse and format the
     * number value.  May be <code>null</code> if a <code>DecimalFormat</code> is not used.
     * @return the <code>DeimcalFormat</code> pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Sets the <code>DeimcalFormat</code> pattern to use to parse and format the number
     * value.  By default, the pattern is set to <code>null</code> and a <code>Number</code>
     * subclass is used to parse and format the number value. 
     * @param pattern the <code>DecimalFormat</code> pattern
     */
    public void setPattern(String pattern) {
        // validate the pattern
        try {
            if (pattern != null) {
                new DecimalFormat(pattern);
            }
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid decimal format '" + pattern + "': " + ex.getMessage());
        }
        
        this.pattern = pattern;
    }
}
