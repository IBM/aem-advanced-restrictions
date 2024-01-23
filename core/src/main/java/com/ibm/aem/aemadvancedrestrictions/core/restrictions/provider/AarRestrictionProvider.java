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

import com.ibm.aem.aemadvancedrestrictions.core.restrictions.patterns.*;
import org.apache.jackrabbit.oak.api.Tree;
import org.apache.jackrabbit.oak.api.Type;
import org.apache.jackrabbit.oak.spi.security.authorization.restriction.*;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Restriction provider for AEM Advanced Restrictions.
 *
 * @author Roland Gruber
 */
@Component(service = RestrictionProvider.class)
public class AarRestrictionProvider extends AbstractRestrictionProvider {

    private static final Logger LOG = LoggerFactory.getLogger(AarRestrictionProvider.class);

    public AarRestrictionProvider() {
        super(getSupportedDefinitions());
    }

    private static Map<String,RestrictionDefinition> getSupportedDefinitions() {
        Map<String,RestrictionDefinition> definitions = new HashMap<>();
        definitions.put(PropertyMatchPattern.ID, new RestrictionDefinitionImpl(PropertyMatchPattern.ID, Type.STRING, false));
        definitions.put(PropertyStartsWithPattern.ID, new RestrictionDefinitionImpl(PropertyStartsWithPattern.ID, Type.STRING, false));
        definitions.put(PropertyEndsWithPattern.ID, new RestrictionDefinitionImpl(PropertyEndsWithPattern.ID, Type.STRING, false));
        definitions.put(PropertyContainsPattern.ID, new RestrictionDefinitionImpl(PropertyContainsPattern.ID, Type.STRING, false));
        definitions.put(PropertyExistsPattern.ID, new RestrictionDefinitionImpl(PropertyExistsPattern.ID, Type.STRING, false));
        definitions.put(PropertyNotExistsPattern.ID, new RestrictionDefinitionImpl(PropertyNotExistsPattern.ID, Type.STRING, false));
        definitions.put(DateInPastPattern.ID, new RestrictionDefinitionImpl(DateInPastPattern.ID, Type.STRING, false));
        definitions.put(DateInFuturePattern.ID, new RestrictionDefinitionImpl(DateInFuturePattern.ID, Type.STRING, false));
        definitions.put(NumberLessPattern.ID, new RestrictionDefinitionImpl(NumberLessPattern.ID, Type.STRING, false));
        definitions.put(NumberGreaterPattern.ID, new RestrictionDefinitionImpl(NumberGreaterPattern.ID, Type.STRING, false));
        definitions.put(NodeExistsPattern.ID, new RestrictionDefinitionImpl(NodeExistsPattern.ID, Type.STRING, false));
        definitions.put(NodeNotExistsPattern.ID, new RestrictionDefinitionImpl(NodeNotExistsPattern.ID, Type.STRING, false));
        definitions.put(PathContainsFolderPattern.ID, new RestrictionDefinitionImpl(PathContainsFolderPattern.ID, Type.STRING, false));
        return definitions;
    }

    @Override
    public RestrictionPattern getPattern(String path, Tree tree) {
        if (path == null) {
            return RestrictionPattern.EMPTY;
        }
        List<RestrictionPattern> patterns = new ArrayList<>();
        if (tree.hasProperty(PropertyMatchPattern.ID)) {
            patterns.add(new PropertyMatchPattern(tree.getProperty(PropertyMatchPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(PropertyStartsWithPattern.ID)) {
            patterns.add(new PropertyStartsWithPattern(tree.getProperty(PropertyStartsWithPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(PropertyEndsWithPattern.ID)) {
            patterns.add(new PropertyEndsWithPattern(tree.getProperty(PropertyEndsWithPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(PropertyContainsPattern.ID)) {
            patterns.add(new PropertyContainsPattern(tree.getProperty(PropertyContainsPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(PropertyExistsPattern.ID)) {
            patterns.add(new PropertyExistsPattern(tree.getProperty(PropertyExistsPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(PropertyNotExistsPattern.ID)) {
            patterns.add(new PropertyNotExistsPattern(tree.getProperty(PropertyNotExistsPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(DateInPastPattern.ID)) {
            patterns.add(new DateInPastPattern(tree.getProperty(DateInPastPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(DateInFuturePattern.ID)) {
            patterns.add(new DateInFuturePattern(tree.getProperty(DateInFuturePattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(NumberLessPattern.ID)) {
            patterns.add(new NumberLessPattern(tree.getProperty(NumberLessPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(NumberGreaterPattern.ID)) {
            patterns.add(new NumberGreaterPattern(tree.getProperty(NumberGreaterPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(NodeExistsPattern.ID)) {
            patterns.add(new NodeExistsPattern(tree.getProperty(NodeExistsPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(NodeNotExistsPattern.ID)) {
            patterns.add(new NodeNotExistsPattern(tree.getProperty(NodeNotExistsPattern.ID).getValue(Type.STRING)));
        }
        else if (tree.hasProperty(PathContainsFolderPattern.ID)) {
            patterns.add(new PathContainsFolderPattern(tree.getProperty(PathContainsFolderPattern.ID).getValue(Type.STRING)));
        }
        return CompositePattern.create(patterns);
    }

    @Override
    public RestrictionPattern getPattern(String path, Set<Restriction> restrictions) {
        if (path == null) {
            return RestrictionPattern.EMPTY;
        }
        List<RestrictionPattern> patterns = new ArrayList<>();
        try {
            for (Restriction restriction : restrictions) {
                switch (restriction.getDefinition().getName()) {
                    case PropertyMatchPattern.ID:
                        patterns.add(new PropertyMatchPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case PropertyExistsPattern.ID:
                        patterns.add(new PropertyExistsPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case PropertyNotExistsPattern.ID:
                        patterns.add(new PropertyNotExistsPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case DateInPastPattern.ID:
                        patterns.add(new DateInPastPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case DateInFuturePattern.ID:
                        patterns.add(new DateInFuturePattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case NumberLessPattern.ID:
                        patterns.add(new NumberLessPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case NumberGreaterPattern.ID:
                        patterns.add(new NumberGreaterPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case NodeExistsPattern.ID:
                        patterns.add(new NodeExistsPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case NodeNotExistsPattern.ID:
                        patterns.add(new NodeNotExistsPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case PropertyStartsWithPattern.ID:
                        patterns.add(new PropertyStartsWithPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case PropertyEndsWithPattern.ID:
                        patterns.add(new PropertyEndsWithPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case PropertyContainsPattern.ID:
                        patterns.add(new PropertyContainsPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                    case PathContainsFolderPattern.ID:
                        patterns.add(new PathContainsFolderPattern(restriction.getProperty().getValue(Type.STRING)));
                        break;
                }
            }
        }
        catch (IllegalStateException e) {
            LOG.error("Unable to build list of restriction patterns for {}: {}", path, e.getMessage());
        }
        return CompositePattern.create(patterns);
    }

}
