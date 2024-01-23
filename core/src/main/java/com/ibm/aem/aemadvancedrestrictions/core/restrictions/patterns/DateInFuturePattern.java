/**
 * Copyright 2024 IBM iX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ibm.aem.aemadvancedrestrictions.core.restrictions.patterns;

import org.apache.jackrabbit.oak.api.PropertyState;
import org.apache.jackrabbit.oak.api.Type;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Restriction pattern to check if a property date value is in the future.
 *
 * @author Roland Gruber
 */
public class DateInFuturePattern extends BasePattern {

    public static final String ID = "aarDateInFuture";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    protected String propertyName;

    /**
     * Constructor
     *
     * @param restrictionValue restriction value (PROPERTY_NAME:PROPERTY_VALUE)
     */
    public DateInFuturePattern(String restrictionValue) {
        this.propertyName = restrictionValue;
    }

    @Override
    protected boolean singleValuePropertyMatches(PropertyState valueState) {
        try {
            return DATE_FORMAT.parse(valueState.getValue(Type.DATE)).after(new Date());
        }
        catch (ParseException e) {
            return false;
        }
    }

    @Override
    protected boolean multiValuePropertyMatches(PropertyState valueState) {
        Iterable<String> valueIt = valueState.getValue(Type.DATES);
        for (String value: valueIt) {
            try {
                if (DATE_FORMAT.parse(value).after(new Date())) {
                    return true;
                }
            }
            catch (ParseException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    protected String getPropertyName() {
        return propertyName;
    }

}
