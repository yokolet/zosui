package zosui.select;

import java.util.function.Predicate;

import zosui.nodes.Element;
import zosui.nodes.LeafNode;
import zosui.nodes.Node;

public abstract class Evaluator {
    protected Evaluator() {
    }

    /**
     Provides a Predicate for this Evaluator, matching the test Element.
     * @param root the root Element, for match evaluation
     * @return a predicate that accepts an Element to test for matches with this Evaluator
     * @since 1.17.1
     */
    public Predicate<Element> asPredicate(Element root) {
        return element -> matches(root, element);
    }

    Predicate<Node> asNodePredicate(Element root) {
        return node -> matches(root, node);
    }

    /**
     * Test if the element meets the evaluator's requirements.
     *
     * @param root    Root of the matching subtree
     * @param element tested element
     * @return Returns <tt>true</tt> if the requirements are met or
     * <tt>false</tt> otherwise
     */
    public abstract boolean matches(Element root, Element element);

    final boolean matches(Element root, Node node) {
        if (node instanceof Element) {
            return matches(root, (Element) node);
        } else if (node instanceof LeafNode && wantsNodes()) {
            return matches(root, (LeafNode) node);
        }
        return false;
    }

    boolean matches(Element root, LeafNode leafNode) {
        return false;
    }

    boolean wantsNodes() {
        return false;
    }

    protected void reset() {
    }

    /**
     A relative evaluator cost function. During evaluation, Evaluators are sorted by ascending cost as a
     n optimization.
     * @return the relative cost of this Evaluator
     */
    protected int cost() {
        return 5; // a nominal default cost
    }

    /**
     * Evaluator for tag name
     */
    public static final class Tag extends Evaluator {
        private final String tagName;

        public Tag(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public boolean matches(Element root, Element element) {
            return (element.nameIs(tagName));
        }

        @Override protected int cost() {
            return 1;
        }

        @Override
        public String toString() {
            return String.format("%s", tagName);
        }
    }

    /**
     * Evaluator for element id
     */
    public static final class Id extends Evaluator {
        private final String id;

        public Id(String id) {
            this.id = id;
        }

        @Override
        public boolean matches(Element root, Element element) {
            return (id.equals(element.id()));
        }

        @Override protected int cost() {
            return 2;
        }
        @Override
        public String toString() {
            return String.format("#%s", id);
        }
    }
}
