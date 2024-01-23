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

import com.day.cq.dam.api.DamConstants;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.oak.api.PropertyState;
import org.apache.jackrabbit.oak.api.Tree;
import org.apache.jackrabbit.oak.api.Type;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Utility functions for tests.
 *
 * @author Roland Gruber
 */
class UtilityFunctions {

    public static Tree createAssetWithMetadataProperty(String propertyName, Object value) {
        Tree asset = mock(Tree.class);
        Tree jcrContent = mock(Tree.class);
        PropertyState primaryType = mock(PropertyState.class);
        when(asset.getProperty(JcrConstants.JCR_PRIMARYTYPE)).thenReturn(primaryType);
        when(primaryType.getValue(Type.STRING)).thenReturn(DamConstants.NT_DAM_ASSET);
        when(asset.hasChild(JcrConstants.JCR_CONTENT)).thenReturn(true);
        when(asset.getChild(JcrConstants.JCR_CONTENT)).thenReturn(jcrContent);
        Tree metadata = mock(Tree.class);
        if (value != null) {
            PropertyState property = mock(PropertyState.class);
            when(metadata.getProperty(propertyName)).thenReturn(property);
            if (value instanceof String) {
                when(property.getValue(Type.STRING)).thenReturn((String) value);
                when(property.getValue(Type.DATE)).thenReturn((String) value);
            }
            if (value instanceof String[]) {
                when(property.getValue(Type.STRINGS)).thenReturn(Arrays.asList((String[]) value));
                when(property.getValue(Type.DATES)).thenReturn(Arrays.asList((String[]) value));
                when(property.isArray()).thenReturn(true);
            }
            else if (value instanceof Long) {
                when(property.getValue(Type.LONG)).thenReturn((Long) value);
            }
            if (value instanceof Long[]) {
                when(property.getValue(Type.LONGS)).thenReturn(Arrays.asList((Long[]) value));
                when(property.isArray()).thenReturn(true);
            }
        }
        when(jcrContent.hasChild("metadata")).thenReturn(true);
        when(jcrContent.getChild("metadata")).thenReturn(metadata);
        return asset;
    }

    public static Tree createEmptyAssetWithParentFolder(String assetName, String parentName, String parentPrimaryType) {
        Tree asset = createAssetWithMetadataProperty("key", "value");
        when(asset.getPath()).thenReturn("/prefix/" + parentName + "/" + assetName);
        when(asset.getName()).thenReturn(assetName);
        PropertyState primaryTypeAsset = mock(PropertyState.class);
        when(primaryTypeAsset.getValue(Type.STRING)).thenReturn(DamConstants.NT_DAM_ASSET);
        when(asset.getProperty(JcrConstants.JCR_PRIMARYTYPE)).thenReturn(primaryTypeAsset);

        Tree parentFolder = mock(Tree.class);
        when(parentFolder.getPath()).thenReturn("/prefix/" + parentName);
        when(parentFolder.getName()).thenReturn(parentName);
        PropertyState primaryTypeFolder = mock(PropertyState.class);
        when(primaryTypeFolder.getValue(Type.STRING)).thenReturn(parentPrimaryType);
        when(parentFolder.getProperty(JcrConstants.JCR_PRIMARYTYPE)).thenReturn(primaryTypeFolder);
        when(asset.getParent()).thenReturn(parentFolder);

        Tree root = mock(Tree.class);
        when(root.isRoot()).thenReturn(true);
        when(root.getPath()).thenReturn("/prefix");
        when(root.getName()).thenReturn("prefix");
        when(root.getProperty(JcrConstants.JCR_PRIMARYTYPE)).thenReturn(primaryTypeFolder);
        when(parentFolder.getParent()).thenReturn(root);
        return asset;
    }

}
