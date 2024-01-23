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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static junit.framework.Assert.assertTrue;
import static junitx.framework.Assert.assertFalse;
import static org.mockito.Mockito.when;

/**
 * Tests NodeExistsPattern.
 *
 * @author Roland Gruber
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NodeExistsPatternTest {

    private static final String METADATA = "metadata";
    private static final String CONFIDENTIAL = "confidential";
    private static final String NODE_NAME = METADATA + "/" + CONFIDENTIAL;

    private NodeExistsPattern pattern = null;

    @Mock
    private PropertyState propertyState;
    private Tree asset;

    @BeforeEach
    void setup() {
        asset = UtilityFunctions.createAssetWithMetadataProperty("prop", "val");
    }

    @Test
    void matches_noNodePresent() {
        pattern = new NodeExistsPattern("invalid");

        assertFalse(pattern.matches(asset, null));
    }

    @Test
    void matches_nodePresentLevel1() {
        pattern = new NodeExistsPattern(METADATA);

        assertTrue(pattern.matches(asset, null));
    }

    @Test
    void matches_nodePresentLevel2() {
        Tree confidential = Mockito.mock(Tree.class);
        Tree metadata = asset.getChild(JcrConstants.JCR_CONTENT).getChild(METADATA);
        when(metadata.hasChild(CONFIDENTIAL)).thenReturn(true);
        when(metadata.getChild(CONFIDENTIAL)).thenReturn(confidential);

        pattern = new NodeExistsPattern(NODE_NAME);

        assertTrue(pattern.matches(asset, null));
    }

}
