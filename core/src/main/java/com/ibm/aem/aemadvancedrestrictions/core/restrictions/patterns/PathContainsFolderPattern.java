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
import org.apache.jackrabbit.oak.api.Type;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;

/**
 * Restriction pattern to check if the path contains a specific folder name.
 *
 * @author Roland Gruber
 */
public class PathContainsFolderPattern extends BasePattern {

    public static final String ID = "aarPathContainsFolder";

    private String folderName;

    /**
     * Constructor
     *
     * @param restrictionValue restriction value (NODE_NAME)
     */
    public PathContainsFolderPattern(String restrictionValue) {
        this.folderName = restrictionValue;
    }

    @Override
    public boolean matches(Tree tree, PropertyState propertyState) {
        if (!tree.getPath().contains(folderName)) {
            return false;
        }
        Tree currentNode = tree;
        while (!currentNode.isRoot()) {
            if (currentNode.getName().equals(folderName) && isFolder(currentNode)) {
                return true;
            }
            currentNode = currentNode.getParent();
            if (!currentNode.getPath().contains(folderName)) {
                return false;
            }
        }
        return false;
    }

    /**
     * Returns if the tree is a folder.
     *
     * @param tree node tree
     * @return is folder
     */
    protected boolean isFolder(Tree tree) {
        boolean isFolder = false;
        if (tree != null && tree.getProperty(JcrConstants.JCR_PRIMARYTYPE) != null) {
            String primaryType = tree.getProperty(JcrConstants.JCR_PRIMARYTYPE).getValue(Type.STRING);
            isFolder = JcrConstants.NT_FOLDER.equals(primaryType)
                || JcrResourceConstants.NT_SLING_FOLDER.equals(primaryType)
                || JcrResourceConstants.NT_SLING_ORDERED_FOLDER.equals(primaryType);
        }
        return isFolder;
    }

}
