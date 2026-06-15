package zosui.nodes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import zosui.parser.Parser;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class TextTest {
    private static final String html =
            "<html><head></head><body id=\"myBody\"><div class=\"baz\">   content  <a href=\"foo\" class=\"bar\">first</a></div></body></html>";

    private static org.w3c.dom.Document document;
    private static org.w3c.dom.Element div, anchor;
    private static org.w3c.dom.Text divText, anchorText;

    @BeforeAll
    public static void setUp() {
        document = Parser.parse(html, "");
        System.out.println("document parsed successfully");
        div = (Element) document.getElementsByTagName("div").item(0);
        divText = (org.w3c.dom.Text) div.getFirstChild();
        anchor = (Element) document.getElementsByTagName("a").item(0);
        anchorText = (org.w3c.dom.Text) anchor.getFirstChild();
    }

    @Test
    public void testGetNodeName() {
        assertEquals("#text", divText.getNodeName());
        assertEquals("#text", anchorText.getNodeName());
    }

    @Test
    public void testGetNodeValue() {
        assertEquals("   content  ", divText.getNodeValue());
        assertEquals("first", anchorText.getNodeValue());
    }

    @Test
    public void testGetNodeType() {
        assertEquals(org.w3c.dom.Node.TEXT_NODE, divText.getNodeType());
    }

    @Test
    public void testGetParentNode() {
        assertSame(div, divText.getParentNode());
        assertSame(anchor, anchorText.getParentNode());
    }

    @Test
    public void testGetChildNodes() {
        org.w3c.dom.NodeList list = div.getChildNodes();
        assertEquals(2, list.getLength());
        assertInstanceOf(org.w3c.dom.Text.class, list.item(0));
        assertInstanceOf(org.w3c.dom.Element.class, list.item(1));
    }
}
