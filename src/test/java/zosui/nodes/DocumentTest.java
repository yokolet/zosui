package zosui.nodes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import zosui.parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentTest {
    private static String html = "<html><head></head><body id=\"myBody\"><div class=\"baz\"><a href=\"foo\" class=\"bar\">first</a></div></body></html>";

    private static org.w3c.dom.Document document;

    @BeforeAll
    public static void setUp() {
        Parser parser = Parser.htmlParser();
        parser.setTrackPosition(true);
        parser.setTrackErrors(100);
        document = parser.parseInput(html, "");
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
    public void testCloneNode() {
        Node cloned = document.cloneNode(true);
        assertInstanceOf(Document.class, cloned);
        Document clonedDoc = (Document) cloned;
        assertFalse(document.isSameNode(clonedDoc));
        assertEquals(1, clonedDoc.getChildNodes().getLength());
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

    @Test
    public void testIsEqualNode() {
        org.w3c.dom.Document cloned = (org.w3c.dom.Document) document.cloneNode(true);
        assertFalse(document.isSameNode(cloned));
        assertTrue(document.isEqualNode(cloned));
    }

    @Test
    public void testUserData() {
        document.setUserData("key", "value", null);
        assertEquals("value", document.getUserData("key"));
        document.setUserData("key", null, null);
        assertNull(document.getUserData("key"));
    }

    @Test
    public void testGetDoctype() {
        assertNull(document.getDoctype());
    }

    @Test
    public void testGetImplementation() {
        DOMImplementation domImpl = document.getImplementation();
        assertNotNull(domImpl);
        assertInstanceOf(DOMImplementation.class, domImpl);
    }

    @Test
    public void testGetDocumentElement() {
        assertNotNull(document.getDocumentElement());
        org.w3c.dom.Element element = document.getDocumentElement();;
        assertEquals("html", element.getNodeName());
    }

    @Test
    public void testGetElementsByTagName() {
        NodeList list = document.getElementsByTagName("html");
        assertEquals(1, list.getLength());
        assertEquals("html", list.item(0).getNodeName());
        list = document.getElementsByTagName("head");
        assertEquals(1, list.getLength());
        assertEquals("head", list.item(0).getNodeName());
        list = document.getElementsByTagName("body");
        assertEquals(1, list.getLength());
        assertEquals("body", list.item(0).getNodeName());
        list = document.getElementsByTagName("div");
        assertEquals(1, list.getLength());
        assertEquals("div", list.item(0).getNodeName());
        list = document.getElementsByTagName("a");
        assertEquals(1, list.getLength());
        assertEquals("a", list.item(0).getNodeName());
    }

    @Test
    public void testGetElementsByTagNameWithStar() {
        NodeList list = document.getElementsByTagName("*");
        assertEquals(5, list.getLength());
    }

    @Test
    public void testGetElementsByTagNameNS() {
        NodeList list = document.getElementsByTagNameNS("*", "*");
        assertEquals(5, list.getLength());
    }

    @Test
    public void testGetElementById() {
        Element element = document.getElementById("myBody");
        assertEquals("myBody", element.getAttribute("id"));
    }

    @Test
    public void testGetInputEncoding() {
        String encoding = document.getInputEncoding();
        assertNull(encoding);
    }

    @Test
    public void testGetDocumentURI() {
        assertNull(document.getDocumentURI());
    }

    @Test
    public void testGetDomConfig() {
        DOMConfiguration domConfig = document.getDomConfig();
        assertNotNull(domConfig);
    }
}
