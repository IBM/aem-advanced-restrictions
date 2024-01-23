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
import org.apache.jackrabbit.oak.api.Tree;

/**
 * Restriction pattern to check if a property does not exist.
 *
 * @author Roland Gruber
 */
public class PropertyNotExistsPattern extends BasePattern {

    public static final String ID = "aarPropertyNotExists";

    private String propertyName;

    /**
     * Constructor
     *
     * @param restrictionValue restriction value (NODE_NAME)
     */
    public PropertyNotExistsPattern(String restrictionValue) {
        this.propertyName = restrictionValue;
    }

    @Override
    public boolean matches(Tree tree, PropertyState propertyState) {
        Tree baseNode = findBaseNode(tree);
        if (baseNode == null) {
            return false;
        }
        return readPropertyFromBase(baseNode, getPropertyName()) == null;
    }

    @Override
    protected String getPropertyName() {
        return propertyName;
    }

}
