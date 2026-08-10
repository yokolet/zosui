package w3c;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class TextTest {
    private static final String html =
            "<html><head></head><body id=\"myBody\"><div class=\"baz\">   content  <b>here</b>   " +
                    "<a href=\"foo\" class=\"bar\">first</a></div></body></html>";

    private static org.w3c.dom.Document document;
    private static org.w3c.dom.Element div, anchor;
    private static org.w3c.dom.Text divText1, divText2, anchorText;

    @BeforeAll
    public static void setUp() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(html));
            document = builder.parse(inputSource);
            System.out.println("document parsed successfully");
            div = (Element) document.getElementsByTagName("div").item(0);
            divText1 = (org.w3c.dom.Text) div.getChildNodes().item(0);
            divText2 = (org.w3c.dom.Text) div.getChildNodes().item(2);
            anchor = (Element) document.getElementsByTagName("a").item(0);
            anchorText = (org.w3c.dom.Text) anchor.getFirstChild();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetNodeName() {
        assertEquals("#text", divText1.getNodeName());
        assertEquals("#text", anchorText.getNodeName());
    }

    @Test
    public void testGetNodeValue() {
        assertEquals("   content  ", divText1.getNodeValue());
        assertEquals("   ", divText2.getNodeValue());
        assertEquals("first", anchorText.getNodeValue());
    }

    @Test
    public void testGetNodeType() {
        assertEquals(org.w3c.dom.Node.TEXT_NODE, divText1.getNodeType());
    }

    @Test
    public void testGetParentNode() {
        assertSame(div, divText1.getParentNode());
        assertSame(anchor, anchorText.getParentNode());
    }

    @Test
    public void testGetChildNodes() {
        org.w3c.dom.NodeList list = divText1.getChildNodes();
        assertEquals(0, list.getLength());
        list = anchorText.getChildNodes();
        assertEquals(0, list.getLength());
    }

    @Test
    public void testGetFirstChild() {
        assertNull(divText1.getFirstChild());
        assertNull(anchorText.getFirstChild());
    }

    @Test
    public void testGetLastChild() {
        assertNull(divText1.getLastChild());
        assertNull(anchorText.getLastChild());
    }

    @Test
    public void testGetPreviousSibling() {
        assertNull(divText1.getPreviousSibling());
        assertNotNull(divText2.getPreviousSibling());
        assertSame(div.getChildNodes().item(1), divText2.getPreviousSibling());
        assertNull(anchorText.getPreviousSibling());
    }

    @Test
    public void testGetNextSibling() {
        assertNotNull(divText1.getNextSibling());
        assertSame(div.getChildNodes().item(1), divText1.getNextSibling());
        assertNotNull(divText2.getNextSibling());
        assertSame(div.getChildNodes().item(3), divText2.getNextSibling());
        assertNull(anchorText.getNextSibling());
    }

    @Test
    public void testGetAttributes() {
        assertNull(divText1.getAttributes());
        assertNull(anchorText.getAttributes());
    }

    @Test
    public void testGetOwnerDocument() {
        assertSame(document, divText1.getOwnerDocument());
        assertSame(document, anchorText.getOwnerDocument());
    }

    @Test
    public void testHasChildNodes() {
        assertFalse(divText1.hasChildNodes());
        assertFalse(divText2.hasChildNodes());
        assertFalse(anchorText.hasChildNodes());
    }

    @Test
    public void testCloneNode() {
        org.w3c.dom.Node clone = divText1.cloneNode(true);
        assertInstanceOf(org.w3c.dom.Text.class, clone);
        assertEquals("   content  ", ((org.w3c.dom.Text) clone).getNodeValue());
        assertNotSame(divText1, clone);
    }

    @Test
    public void testGetNamespaceURI() {
        assertNull(divText1.getNamespaceURI());
    }

    @Test
    public void testGetPrefix() {
        assertNull(divText1.getPrefix());
    }

    @Test
    public void testGetLocalName() {
        assertNull(divText1.getLocalName());
    }

    @Test
    public void testHasAttributes() {
        assertFalse(divText1.hasAttributes());
        assertFalse(anchorText.hasAttributes());
    }

    @Test
    public void testGetBaseURI() {
        assertNull(divText1.getBaseURI());
    }

    @Test
    public void testCompareDocumentPosition() {
        short position = divText1.compareDocumentPosition(divText2);
        assertEquals(Node.DOCUMENT_POSITION_FOLLOWING, position);
        position = anchorText.compareDocumentPosition(divText2);
        assertEquals(Node.DOCUMENT_POSITION_PRECEDING, position);
    }

    @Test
    public void testGetTextContent() {
        assertEquals("   content  ", divText1.getTextContent());
        assertEquals("   ", divText2.getTextContent());
        assertEquals("first", anchorText.getTextContent());
    }

    @Test
    public void testIsSameNode() {
        assertTrue(divText1.isSameNode(divText1));
        assertFalse(divText1.isSameNode(divText2));
        org.w3c.dom.Node cloned = divText1.cloneNode(true);
        assertFalse(divText1.isSameNode(cloned));
    }

    @Test
    public void testLookupPrefix() {
        assertNull(divText1.lookupPrefix(null));
    }

    @Test
    public void testIsDefaultNamespace() {
        assertTrue(divText1.isDefaultNamespace(null));
        assertFalse(divText1.isDefaultNamespace(""));
    }

    @Test
    public void testLookupNamespaceURI() {
        assertNull(divText1.lookupNamespaceURI(null));
        assertNull(divText1.lookupNamespaceURI(""));
    }

    @Test
    public void testIsEqualNode() {
        org.w3c.dom.Node cloned = divText1.cloneNode(true);
        assertTrue(divText1.isEqualNode(cloned));
        cloned = anchorText.cloneNode(false);
        assertFalse(divText1.isEqualNode(cloned));
        assertFalse(divText1.isEqualNode(divText2));
    }

    @Test
    public void testUserData() {
        divText1.setUserData("key", "value", null);
        assertEquals("value", divText1.getUserData("key"));
        divText1.setUserData("key", null, null);
        assertNull(divText1.getUserData("key"));
    }

    @Test
    public void testGetData() {
        assertEquals("   content  ", divText1.getData());
        assertEquals("   ", divText2.getData());
        assertEquals("first", anchorText.getData());
    }

    @Test
    public void testGetLength() {
        assertEquals(12, divText1.getLength());
        assertEquals(3, divText2.getLength());
        assertEquals(5, anchorText.getLength());
    }

    @Test
    public void testSubstringData() {
        String substring = divText1.substringData(3, 100);
        assertEquals("content  ", substring);
        substring = divText2.substringData(2, 100);
        assertEquals(" ", substring);
    }

    @Test
    public void testIsElementContentWhitespace() {
        assertFalse(divText1.isElementContentWhitespace());
        assertFalse(divText2.isElementContentWhitespace());
        assertFalse(anchorText.isElementContentWhitespace());
    }

    @Test
    public void testGetWholeText() {
        assertEquals("   content  ", divText1.getWholeText());
        assertEquals("   ", divText2.getWholeText());
        assertEquals("first", anchorText.getWholeText());
    }
}
