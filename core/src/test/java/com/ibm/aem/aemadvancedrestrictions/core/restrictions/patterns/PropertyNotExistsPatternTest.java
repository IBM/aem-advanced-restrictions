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

import org.apache.jackrabbit.oak.api.PropertyState;
import org.apache.jackrabbit.oak.api.Tree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static junit.framework.Assert.assertTrue;
import static junitx.framework.Assert.assertFalse;

/**
 * Tests PropertyNotExistsPattern.
 *
 * @author Roland Gruber
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PropertyNotExistsPatternTest {

    private static final String METADATA = "metadata";
    private static final String CONFIDENTIAL = "confidential";
    private static final String PROP_NAME = METADATA + "/" + CONFIDENTIAL;

    private PropertyNotExistsPattern pattern = null;

    @Mock
    private PropertyState propertyState;
    private Tree asset;

    @BeforeEach
    void setup() {
        asset = UtilityFunctions.createAssetWithMetadataPropertyAndRoot(CONFIDENTIAL, "val");
    }

    @Test
    void matches_notPresent() {
        pattern = new PropertyNotExistsPattern("invalid");

        assertTrue(pattern.matches(asset, null));
    }

    @Test
    void matches_notPresent_level2() {
        pattern = new PropertyNotExistsPattern(METADATA + "/" + "invalid");

        assertTrue(pattern.matches(asset, null));
    }

    @Test
    void matches_present() {
        pattern = new PropertyNotExistsPattern(PROP_NAME);

        assertFalse(pattern.matches(asset, null));
    }

}
