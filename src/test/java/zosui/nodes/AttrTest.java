package zosui.nodes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import zosui.parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

public class AttrTest {
    private static String html = "<html><head></head><body id=\"myBody\"><div class=\"baz\"><a href=\"foo\" class=\"bar\">first</a></div></body></html>";

    private static org.w3c.dom.Document document;
    private static org.w3c.dom.Element body, anchor;
    private static org.w3c.dom.NamedNodeMap bodyAttrs, anchorAttrs;

    @BeforeAll
    public static void setUp() {
        document = Parser.parse(html, "");
        System.out.println("document parsed successfully");
        body = (Element) document.getElementsByTagName("body").item(0);
        bodyAttrs = body.getAttributes();
        anchor = (Element) document.getElementsByTagName("a").item(0);
        anchorAttrs = anchor.getAttributes();
    }

    @Test
    public void testGetNodeName() {
        assertEquals(1, bodyAttrs.getLength());
        assertEquals("id", bodyAttrs.item(0).getNodeName());
        assertEquals(2, anchorAttrs.getLength());
        String name0 = anchorAttrs.item(0).getNodeName();
        String name1 = anchorAttrs.item(1).getNodeName();
        assertTrue((name0.equals("href") && name1.equals("class")) || (name0.equals("class") && name1.equals("href")));
    }

    @Test
    public void testGetNodeValue() {
        assertEquals("myBody", bodyAttrs.getNamedItem("id").getNodeValue());
        assertEquals("foo", anchorAttrs.getNamedItem("href").getNodeValue());
        assertEquals("bar", anchorAttrs.getNamedItem("class").getNodeValue());
    }

    @Test
    public void testGetNodeType() {
        assertEquals(org.w3c.dom.Node.ATTRIBUTE_NODE, bodyAttrs.getNamedItem("id").getNodeType());
        assertEquals(org.w3c.dom.Node.ATTRIBUTE_NODE, anchorAttrs.getNamedItem("href").getNodeType());
        assertEquals(Node.ATTRIBUTE_NODE, anchorAttrs.getNamedItem("class").getNodeType());
    }

    @Test
    public void testGetParentNode() {
        assertNull(bodyAttrs.getNamedItem("id").getParentNode());
        assertNull(anchorAttrs.getNamedItem("href").getParentNode());
    }

    @Test
    public void testGetChildNodes() {
        org.w3c.dom.NodeList list = bodyAttrs.item(0).getChildNodes();
        assertEquals(0, list.getLength());
    }

    @Test
    public void testGetFirstChild() {
        assertNull(bodyAttrs.item(0).getFirstChild());
    }

    @Test
    public void testGetLastChild() {
        assertNull(bodyAttrs.item(0).getLastChild());
    }

    @Test
    public void testGetPreviousSibling() {
        assertNull(bodyAttrs.item(0).getPreviousSibling());
    }

    @Test
    public void testGetAttributes() {
        assertNull(bodyAttrs.item(0).getAttributes());
    }

    @Test
    public void testGetOwnerDocument() {
        assertEquals(document, bodyAttrs.item(0).getOwnerDocument());
    }

    @Test
    public void testHasChildNodes() {
        assertFalse(bodyAttrs.item(0).hasChildNodes());
    }

    @Test
    public void testCloneNode() {
        org.w3c.dom.Node node = bodyAttrs.item(0).cloneNode(true);
        assertInstanceOf(org.w3c.dom.Attr.class, node);
        org.w3c.dom.Attr attr = (org.w3c.dom.Attr)node;
        assertNotSame(attr, bodyAttrs.item(0));
        assertSame(attr.getNodeValue(), bodyAttrs.item(0).getNodeValue());
    }

    @Test
    public void testIsSupported() {
        assertFalse(bodyAttrs.item(0).isSupported("feature", "version"));
    }

    @Test
    public void testGetNamespaceURI() {
        assertNull(bodyAttrs.item(0).getNamespaceURI());
    }

    @Test
    void testGetPrefix() {
        assertNull(bodyAttrs.item(0).getPrefix());
    }

    @Test
    void testGetLocalName() {
        assertNull(bodyAttrs.item(0).getLocalName());
    }

    @Test
    void testHasAttributes() {
        assertFalse(bodyAttrs.item(0).hasAttributes());
    }

    @Test
    void testGetBaseURI() {
        assertNull(bodyAttrs.item(0).getBaseURI());
    }

    @Test
    public void testCompareDocumentPosition() {
        short result = bodyAttrs.item(0).compareDocumentPosition(body);
        assertEquals(Node.DOCUMENT_POSITION_CONTAINS | Node.DOCUMENT_POSITION_PRECEDING, result);
        result = anchorAttrs.item(0).compareDocumentPosition(anchorAttrs.item(1));
        assertEquals(Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC | Node.DOCUMENT_POSITION_FOLLOWING, result);
        result = bodyAttrs.item(0).compareDocumentPosition(anchorAttrs.item(0));
        assertEquals(Node.DOCUMENT_POSITION_CONTAINED_BY | Node.DOCUMENT_POSITION_FOLLOWING, result);
    }

    @Test
    public void testGetTextContent() {
        assertEquals("myBody", bodyAttrs.item(0).getTextContent());
        assertEquals("foo", anchorAttrs.getNamedItem("href").getTextContent());
    }

    @Test
    public void testIsSameNode() {
        org.w3c.dom.Attr attr = (org.w3c.dom.Attr)bodyAttrs.item(0);
        assertTrue(attr.isSameNode(attr));
        assertFalse(attr.isSameNode(body));
        org.w3c.dom.Attr cloned = (org.w3c.dom.Attr)attr.cloneNode(false);
        assertFalse(attr.isSameNode(cloned));
    }

    @Test
    public void testLookupPrefix() {
        assertNull(bodyAttrs.item(0).lookupPrefix("http://www.w3.org/1999/xhtml"));
    }

    @Test
    public void testIsDefaultNamespace() {
        assertFalse(bodyAttrs.item(0).isDefaultNamespace("http://www.w3.org/1999/xhtml"));
    }

    @Test
    public void testLookupNamespace() {
        assertNull(bodyAttrs.item(0).lookupNamespaceURI(null));
        assertNull(bodyAttrs.item(0).lookupNamespaceURI(""));
    }

    @Test
    public void testIsEqualNode() {
        org.w3c.dom.Attr attrHref = (org.w3c.dom.Attr)anchorAttrs.getNamedItem("href");
        org.w3c.dom.Attr attrClass = (org.w3c.dom.Attr)anchorAttrs.getNamedItem("class");
        assertTrue(attrHref.isEqualNode(attrHref));
        assertFalse(attrHref.isEqualNode(attrClass));
        org.w3c.dom.Attr clonedHref = (org.w3c.dom.Attr)attrHref.cloneNode(true);
        assertFalse(attrHref.isSameNode(clonedHref));
        assertTrue(attrHref.isEqualNode(clonedHref));
    }

    @Test
    public void testUserData() {
        org.w3c.dom.Attr attr = (org.w3c.dom.Attr)bodyAttrs.item(0);
        attr.setUserData("key", "value", null);
        assertEquals("value", attr.getUserData("key"));
        attr.setUserData("key", null, null);
        assertNull(attr.getUserData("key"));
    }

    @Test
    public void testGetName() {
        assertEquals("id", ((org.w3c.dom.Attr)bodyAttrs.item(0)).getName());
    }

    @Test
    public void testGetSpecified() {
        assertTrue(((org.w3c.dom.Attr)bodyAttrs.item(0)).getSpecified());
    }

    @Test
    public void testGetValue() {
        assertEquals("myBody", ((org.w3c.dom.Attr)bodyAttrs.item(0)).getValue());
    }

    @Test
    public void testGetOwnerElement() {
        org.w3c.dom.Attr attr = (org.w3c.dom.Attr)bodyAttrs.item(0);
        assertSame(attr.getOwnerElement(), body);
        attr = (org.w3c.dom.Attr)anchorAttrs.item(0);
        assertSame(attr.getOwnerElement(), anchor);
        assertNotSame(attr.getOwnerElement(), body);
    }

    @Test
    public void testGetSchemaTypeInfo() {
        org.w3c.dom.TypeInfo typeInfo = ((org.w3c.dom.Attr)bodyAttrs.item(0)).getSchemaTypeInfo();
        assertNotNull(typeInfo);
    }

    @Test
    public void testIsId() {
        assertTrue(((org.w3c.dom.Attr)bodyAttrs.item(0)).isId());
    }
}
