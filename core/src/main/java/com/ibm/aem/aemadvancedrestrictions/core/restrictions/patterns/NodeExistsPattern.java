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

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.oak.api.PropertyState;
import org.apache.jackrabbit.oak.api.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Restriction pattern to check if a node exists.
 *
 * @author Roland Gruber
 */
public class NodeExistsPattern extends BasePattern {

    public static final String ID = "aarNodeExists";

    private String nodeName;

    /**
     * Constructor
     *
     * @param restrictionValue restriction value (NODE_NAME)
     */
    public NodeExistsPattern(String restrictionValue) {
        this.nodeName = restrictionValue;
    }

    @Override
    public boolean matches(Tree tree, PropertyState propertyState) {
        Tree baseNode = findBaseNode(tree);
        if (baseNode == null) {
            return false;
        }
        if (!baseNode.hasChild(JcrConstants.JCR_CONTENT)) {
            return false;
        }
        Tree node = baseNode.getChild(JcrConstants.JCR_CONTENT);
        return subnodeExists(node);
    }

    protected boolean subnodeExists(Tree node) {
        List<String> parts = new ArrayList<>(Arrays.asList(nodeName.split("/")));
        while (parts.size() > 0) {
            String child = parts.remove(0);
            if (!node.hasChild(child)) {
                return false;
            }
            node = node.getChild(child);
        }
        return true;
    }

}
