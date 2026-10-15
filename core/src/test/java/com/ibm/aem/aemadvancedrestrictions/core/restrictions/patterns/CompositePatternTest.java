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

import java.util.Map;

import org.apache.jackrabbit.oak.api.PropertyState;
import org.apache.jackrabbit.oak.api.Tree;
import org.apache.jackrabbit.oak.api.Type;
import org.apache.jackrabbit.oak.spi.security.authorization.restriction.CompositePattern;
import org.apache.jackrabbit.oak.spi.security.authorization.restriction.RestrictionPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.ibm.aem.aemadvancedrestrictions.core.restrictions.provider.AarRestrictionProvider;

import static junit.framework.Assert.assertTrue;
import static junitx.framework.Assert.assertEquals;
import static junitx.framework.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests PropertyMatchPattern.
 *
 * @author Roland Gruber
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CompositePatternTest {

    private AarRestrictionProvider provider;

    @Mock
    Tree tree;

    @Mock
    private PropertyState propertyState;

    @BeforeEach
    void setup() {
        provider = new AarRestrictionProvider();
    }

    @Test
    void matches_multiplePropertiesMatch() {
        when(tree.hasProperty(PropertyEndsWithPattern.ID)).thenReturn(true);
        when(tree.hasProperty(PropertyStartsWithPattern.ID)).thenReturn(true);
        PropertyState propertyStateEnds = mock(PropertyState.class);
        when(propertyStateEnds.getValue(Type.STRING)).thenReturn("metadata/prop" + BasePattern.DELIMITER + "end");
        when(tree.getProperty(PropertyEndsWithPattern.ID)).thenReturn(propertyStateEnds);
        PropertyState propertyStateStarts = mock(PropertyState.class);
        when(propertyStateStarts.getValue(Type.STRING)).thenReturn("metadata/prop2" + BasePattern.DELIMITER + "start");
        when(tree.getProperty(PropertyStartsWithPattern.ID)).thenReturn(propertyStateStarts);

        RestrictionPattern pattern = provider.getPattern("/content", tree);
        Assertions.assertTrue(pattern instanceof CompositePattern);

        Tree asset = UtilityFunctions.createAssetWithMetadataPropertyAndRoot(Map.of("prop", "1end", "prop2", "start2"));
        assertTrue(pattern.matches(asset, null));
    }

    @Test
    void matches_multiplePropertiesPartialMatch() {
        when(tree.hasProperty(PropertyEndsWithPattern.ID)).thenReturn(true);
        when(tree.hasProperty(PropertyStartsWithPattern.ID)).thenReturn(true);
        PropertyState propertyStateEnds = mock(PropertyState.class);
        when(propertyStateEnds.getValue(Type.STRING)).thenReturn("metadata/prop" + BasePattern.DELIMITER + "end");
        when(tree.getProperty(PropertyEndsWithPattern.ID)).thenReturn(propertyStateEnds);
        PropertyState propertyStateStarts = mock(PropertyState.class);
        when(propertyStateStarts.getValue(Type.STRING)).thenReturn("metadata/prop2" + BasePattern.DELIMITER + "start");
        when(tree.getProperty(PropertyStartsWithPattern.ID)).thenReturn(propertyStateStarts);

        RestrictionPattern pattern = provider.getPattern("/content", tree);
        Assertions.assertTrue(pattern instanceof CompositePattern);

        Tree asset = UtilityFunctions.createAssetWithMetadataPropertyAndRoot(Map.of("prop", "1end", "prop2", "xxx"));
        assertFalse(pattern.matches(asset, null));
    }

    @Test
    void matches_multiplePropertiesNoMatch() {
        when(tree.hasProperty(PropertyEndsWithPattern.ID)).thenReturn(true);
        when(tree.hasProperty(PropertyStartsWithPattern.ID)).thenReturn(true);
        PropertyState propertyStateEnds = mock(PropertyState.class);
        when(propertyStateEnds.getValue(Type.STRING)).thenReturn("metadata/prop" + BasePattern.DELIMITER + "end");
        when(tree.getProperty(PropertyEndsWithPattern.ID)).thenReturn(propertyStateEnds);
        PropertyState propertyStateStarts = mock(PropertyState.class);
        when(propertyStateStarts.getValue(Type.STRING)).thenReturn("metadata/prop2" + BasePattern.DELIMITER + "start");
        when(tree.getProperty(PropertyStartsWithPattern.ID)).thenReturn(propertyStateStarts);

        RestrictionPattern pattern = provider.getPattern("/content", tree);
        Assertions.assertTrue(pattern instanceof CompositePattern);

        Tree asset = UtilityFunctions.createAssetWithMetadataPropertyAndRoot(Map.of("prop", "xxx", "prop2", "xxx"));
        assertFalse(pattern.matches(asset, null));
    }

}
