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
package com.ibm.aem.aemadvancedrestrictions.core.restrictions.provider;

import com.ibm.aem.aemadvancedrestrictions.core.restrictions.patterns.NodeExistsPattern;
import com.ibm.aem.aemadvancedrestrictions.core.restrictions.patterns.PropertyEndsWithPattern;
import org.apache.jackrabbit.oak.api.PropertyState;
import org.apache.jackrabbit.oak.api.Tree;
import org.apache.jackrabbit.oak.api.Type;
import org.apache.jackrabbit.oak.spi.security.authorization.restriction.Restriction;
import org.apache.jackrabbit.oak.spi.security.authorization.restriction.RestrictionDefinition;
import org.apache.jackrabbit.oak.spi.security.authorization.restriction.RestrictionPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests AarRestrictionProvider
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AarRestrictionProviderTest {

    private AarRestrictionProvider provider;

    @Mock
    Tree tree;

    @BeforeEach
    void setup() {
        provider = new AarRestrictionProvider();
    }

    @Test
    void getSupportedRestrictions() {
         assertFalse(provider.getSupportedRestrictions("/path").isEmpty());
    }

    @Test
    void getPattern_pathNull() {
        assertEquals(RestrictionPattern.EMPTY, provider.getPattern(null, tree));
    }

    @Test
    void getPattern_pathNotNull_noProperty() {
        assertEquals(RestrictionPattern.EMPTY, provider.getPattern("/content", tree));
    }

    @Test
    void getPattern_pathNotNull_property() {
        when(tree.hasProperty(PropertyEndsWithPattern.ID)).thenReturn(true);
        PropertyState propertyState = mock(PropertyState.class);
        when(propertyState.getValue(Type.STRING)).thenReturn("prop=end");
        when(tree.getProperty(PropertyEndsWithPattern.ID)).thenReturn(propertyState);

        RestrictionPattern pattern = provider.getPattern("/content", tree);
        assertTrue(pattern instanceof PropertyEndsWithPattern);
    }

    @Test
    void getPatternPathSet_pathNull() {
        assertEquals(RestrictionPattern.EMPTY, provider.getPattern(null, new HashSet<>()));
    }

    @Test
    void getPatternPathSet_pathNotNull_restriction() {
        HashSet<Restriction> restrictions = new HashSet<>();
        Restriction restriction = mock(Restriction.class);
        RestrictionDefinition definition = mock(RestrictionDefinition.class);
        when(restriction.getDefinition()).thenReturn(definition);
        when(definition.getName()).thenReturn(NodeExistsPattern.ID);
        PropertyState propertyState = mock(PropertyState.class);
        when(propertyState.getValue(Type.STRING)).thenReturn("/content");
        when(restriction.getProperty()).thenReturn(propertyState);
        restrictions.add(restriction);

        assertTrue(provider.getPattern("/content", restrictions) instanceof NodeExistsPattern);
    }

}
