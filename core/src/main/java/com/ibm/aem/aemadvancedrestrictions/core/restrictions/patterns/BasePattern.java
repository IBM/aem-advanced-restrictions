/**
 * Copyright 2024 - 2025 IBM iX
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

import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.NameConstants;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.oak.api.PropertyState;
import org.apache.jackrabbit.oak.api.Tree;
import org.apache.jackrabbit.oak.api.Type;
import org.apache.jackrabbit.oak.spi.security.authorization.restriction.RestrictionPattern;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for restriction patterns.
 *
 * @author Roland Gruber
 */
public abstract class BasePattern implements RestrictionPattern {

    /**
     * Delimiter when the restriction value contains multiple parts (e.g. property name + value).
     */
    public static final String DELIMITER = "$";

    @Override
    public boolean matches(Tree tree, PropertyState propertyState) {
        Tree baseNode = findBaseNode(tree);
        if (baseNode == null) {
            return false;
        }
        PropertyState valueState = readPropertyFromBase(baseNode, getPropertyName());
        if (valueState != null) {
            if (!valueState.isArray()) {
                if (singleValuePropertyMatches(valueState)) {
                    return true;
                }
            }
            else {
                if (multiValuePropertyMatches(valueState)) {
                    return true;
                }
            }
        }
        if (checkHierarchy()) {
            return matches(tree.getParent(), propertyState);
        }
        return false;
    }

    /**
     * Returns the property name if restriction is based on properties.
     *
     * @return property name
     */
    protected String getPropertyName() {
        return null;
    }

    /**
     * Returns if the pattern matches for the given single value.
     * To be implemented by subclasses.
     *
     * @param valueState value state
     * @return matches
     */
    protected boolean singleValuePropertyMatches(PropertyState valueState) {
        return false;
    }

    /**
     * Returns if the pattern matches for the given single value.
     * To be implemented by subclasses.
     *
     * @param valueState value state
     * @return matches
     */
    protected boolean multiValuePropertyMatches(PropertyState valueState) {
        return false;
    }

    /**
     * Finds the base node of the page or asset.
     *
     * @param tree current tree node
     * @return base tree
     */
    protected Tree findBaseNode(Tree tree) {
        if (isValidBaseNode(tree)) {
            return tree;
        }
        if (!tree.getPath().contains(JcrConstants.JCR_CONTENT)) {
            return null;
        }
        Tree currentNode = tree;
        while (!currentNode.isRoot()) {
            currentNode = currentNode.getParent();
            if (isValidBaseNode(currentNode)) {
                return currentNode;
            }
        }
        return null;
    }

    /**
     * Returns if the tree belongs to a page, asset or Sling folder.
     *
     * @param tree node tree
     * @return is page or asset
     */
    protected boolean isValidBaseNode(Tree tree) {
        boolean isValid = false;
        if (tree != null && tree.getProperty(JcrConstants.JCR_PRIMARYTYPE) != null) {
            String primaryType = tree.getProperty(JcrConstants.JCR_PRIMARYTYPE).getValue(Type.STRING);
            isValid = DamConstants.NT_DAM_ASSET.equals(primaryType)
                      || NameConstants.NT_PAGE.equals(primaryType)
                      || JcrResourceConstants.NT_SLING_FOLDER.equals(primaryType)
                      || JcrResourceConstants.NT_SLING_ORDERED_FOLDER.equals(primaryType);
        }
        return isValid;
    }

    /**
     * Reads the needed property from the base node (e.g. dam:Asset).
     * Slashes in property name are treated as subnodes.
     *
     * @param base base node
     * @param propertyName property name
     * @return property if found
     */
    protected PropertyState readPropertyFromBase(Tree base, String propertyName) {
        if (!base.hasChild(JcrConstants.JCR_CONTENT)) {
            return null;
        }
        Tree node = base.getChild(JcrConstants.JCR_CONTENT);
        List<String> parts = new ArrayList<>(Arrays.asList(propertyName.split("/")));
        while (parts.size() > 1) {
            String child = parts.remove(0);
            if (!node.hasChild(child)) {
                return null;
            }
            node = node.getChild(child);
        }
        return node.getProperty(parts.get(0));
    }

    /**
     * Checks also parent nodes and not only base.
     *
     * @return check hierarchy
     */
    protected boolean checkHierarchy() {
        return false;
    }

    @Override
    public boolean matches(String path) {
        // no path level permission
        return false;
    }

    @Override
    public boolean matches() {
        // no repository level permission
        return false;
    }

}
