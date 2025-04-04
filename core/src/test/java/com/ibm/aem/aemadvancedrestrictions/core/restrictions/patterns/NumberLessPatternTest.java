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
import static junitx.framework.Assert.assertEquals;
import static junitx.framework.Assert.assertFalse;

/**
 * Tests NumberLessPattern.
 *
 * @author Roland Gruber
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NumberLessPatternTest {

    private static final String PREFIX = "metadata/";
    private static final String PROP_NAME = "confidential";
    private static final Long PROP_VALUE = 1L;

    private NumberLessPattern pattern = null;

    @Mock
    private PropertyState propertyState;

    @BeforeEach
    void setup() {
        pattern = new NumberLessPattern( PREFIX + PROP_NAME + BasePattern.DELIMITER + PROP_VALUE);

        assertEquals(PREFIX + PROP_NAME, pattern.propertyName);
        assertEquals(PROP_VALUE, pattern.propertyValue);
    }

    @Test
    void matches_noPropertyPresent() {
        Tree asset = UtilityFunctions.createAssetWithMetadataPropertyAndRoot(PROP_NAME, null);
        assertFalse(pattern.matches(asset, null));
    }

    @Test
    void matches_propertyMatches() {
        Tree asset = UtilityFunctions.createAssetWithMetadataPropertyAndRoot(PROP_NAME, 0L);
        assertTrue(pattern.matches(asset, null));
    }

    @Test
    void matches_propertyMatchesNot() {
        Tree asset = UtilityFunctions.createAssetWithMetadataPropertyAndRoot(PROP_NAME, 2L);
        assertFalse(pattern.matches(asset, null));
    }

    @Test
    void matches_multiPropertyMatches() {
        Tree asset = UtilityFunctions.createAssetWithMetadataPropertyAndRoot(PROP_NAME, new Long[] {0L, 2L});
        assertTrue(pattern.matches(asset, null));
    }

    @Test
    void matches_multiPropertyMatchesNot() {
        Tree asset = UtilityFunctions.createAssetWithMetadataPropertyAndRoot(PROP_NAME, new Long[] {2L, 3L});
        assertFalse(pattern.matches(asset, null));
    }

}
