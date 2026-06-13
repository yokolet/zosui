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
}
