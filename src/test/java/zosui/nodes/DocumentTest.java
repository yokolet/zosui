package zosui.nodes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zosui.parser.Parser;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentTest {
    private static String html = "<html><head></head><body><div class=\"baz\"><a href=\"foo\" class=\"bar\">first</a></div></body></html>";

    private static org.w3c.dom.Document document;

    @BeforeAll
    public static void setUp() {
        document = Parser.parse(html, "");
    }

    @Test
    public void testGetNodeName() {
        assertEquals("#document", document.getNodeName());
    }

    @Test
    public void testGetNodeValue() {
        assertNull(document.getNodeValue());
    }

    @Test
    public void testGetNodeType() {
        assertEquals(Node.DOCUMENT_NODE, document.getNodeType());
    }

    @Test
    public void testGetParentNode() {
        assertNull(document.getParentNode());
    }

    @Test
    public void testGetChildNodes() {
        NodeList children = document.getChildNodes();
        assertEquals(1, children.getLength()); // html is the first child
    }

    @Test
    public void testGetFirstChild() {
        Node node = document.getFirstChild();
        assertEquals("html", node.getNodeName());
    }

    @Test
    public void testGetLastChild() {
        Node node = document.getLastChild();
        assertEquals("html", node.getNodeName());
    }

    @Test
    public void testGetPreviousSibling() {
        Node node = document.getPreviousSibling();
        assertNull(node);
    }

    @Test
    public void testGetNextSibling() {
        Node node = document.getNextSibling();
        assertNull(node);
    }

    @Test
    public void testGetAttributeNode() {
        NamedNodeMap attributes = document.getAttributes();
        assertNull(attributes);
    }

    @Test
    public void testGetOwnerDocument() {
        assertNull(document.getOwnerDocument());
    }

    @Test
    public void testHasChildNodes() {
        assertTrue(document.hasChildNodes());
    }

    @Test
    public void testIsSupported() {
        assertFalse(document.isSupported("feature", "version"));
    }

    @Test
    public void testGetNamespaceURI() {
        assertNull(document.getNamespaceURI());
    }

    @Test
    public void testGetPrefix() {
        assertNull(document.getPrefix());
    }

    @Test
    public void testGetLocalName() {
        assertNull(document.getLocalName());
    }

    @Test
    public void testHasAttributes() {
        assertFalse(document.hasAttributes());
    }

    @Test
    public void testGetBaseURI() {
        assertNull(document.getBaseURI());
    }

    @Test
    public void testCompareDocumentPosition() {
        try {
            org.w3c.dom.Node node = document.getFirstChild();
            short position = document.compareDocumentPosition(node);
            assertEquals(Node.DOCUMENT_POSITION_CONTAINED_BY | Node.DOCUMENT_POSITION_FOLLOWING, position);
        } catch (DOMException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetTextContent() {
        assertNull(document.getTextContent());
    }

    @Test
    public void testIsSameNode() {
        assertTrue(document.isSameNode(document));
        assertFalse(document.isSameNode(document.getFirstChild()));
    }

    @Test
    public void testLookupPrefix() {
        String namespaceURI = document.getNamespaceURI();
        assertNull(document.lookupPrefix(namespaceURI));
    }

    @Test
    public void testIsDefaultNamespace() {
        String namespace = "http://www.w3.org/1999/xhtml";
        assertFalse(document.isDefaultNamespace(namespace));
    }

    @Test
    public void testLookupNamespaceURI() {
        String namespaceURI = document.lookupNamespaceURI(null);
        assertNull(document.lookupNamespaceURI(namespaceURI));
        namespaceURI = document.lookupNamespaceURI("");
        assertNull(document.lookupNamespaceURI(namespaceURI));
    }
}
