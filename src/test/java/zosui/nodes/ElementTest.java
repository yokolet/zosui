package zosui.nodes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import zosui.parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

public class ElementTest {
    private static String html = "<html><head></head><body id=\"myBody\"><div class=\"baz\"><a href=\"foo\" class=\"bar\">first</a></div></body></html>";

    private static org.w3c.dom.Document document;
    private static org.w3c.dom.Element root, body;

    @BeforeAll
    public static void setUp() {
        document = Parser.parse(html, "");
        System.out.println("document parsed successfully");
        root = document.getDocumentElement();
        body = (Element) document.getElementsByTagName("body").item(0);
    }

    @Test
    public void testGetNodeName() {
        assertEquals("html", root.getNodeName());
        assertEquals("body", body.getNodeName());
    }

    @Test
    public void testGetNodeValue() {
        assertNull(root.getNodeValue());
        assertNull(body.getNodeValue());
    }

    @Test
    public void testGetNodeType() {
        assertEquals(Node.ELEMENT_NODE, root.getNodeType());
        assertEquals(Node.ELEMENT_NODE, body.getNodeType());
    }

    @Test
    public void testGetParentNode() {
        assertSame(document, root.getParentNode());
        assertSame(root, body.getParentNode());
    }

    @Test
    public void testGetChildNodes() {
        org.w3c.dom.NodeList childNodes = root.getChildNodes();
        assertEquals(2, childNodes.getLength());
        assertEquals("head", childNodes.item(0).getNodeName());
        assertEquals("body", childNodes.item(1).getNodeName());
        childNodes = body.getChildNodes();
        assertEquals(1, childNodes.getLength());
        assertEquals("div", childNodes.item(0).getNodeName());
    }

    @Test
    public void testGetFirstChild() {
        org.w3c.dom.Node firstChild = root.getFirstChild();
        assertEquals("head", firstChild.getNodeName());
        firstChild = body.getFirstChild();
        assertEquals("div", firstChild.getNodeName());
    }

    @Test
    public void testGetLastChild() {
        org.w3c.dom.Node lastChild = root.getLastChild();
        assertEquals("body", lastChild.getNodeName());
        lastChild = body.getLastChild();
        assertEquals("div", lastChild.getNodeName());
    }

    @Test
    public void testGetPreviousSibling() {
        org.w3c.dom.Node previousSibling = root.getPreviousSibling();
        assertNull(previousSibling);
        previousSibling = body.getPreviousSibling();
        assertEquals("head", previousSibling.getNodeName());
    }

    @Test
    public void testGetNextSibling() {
        org.w3c.dom.Node nextSibling = root.getNextSibling();
        assertNull(nextSibling);
        nextSibling = body.getNextSibling();
        assertNull(nextSibling);
    }

    @Test
    public void testGetAttributes() {
        org.w3c.dom.NamedNodeMap attributes = root.getAttributes();
        assertEquals(0, attributes.getLength());
        attributes = body.getAttributes();
        assertEquals(1, attributes.getLength());
        assertEquals("id", attributes.item(0).getNodeName());
    }
}
