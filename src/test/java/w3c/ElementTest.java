package w3c;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class ElementTest {
    private static String html = "<html><head></head><body id=\"myBody\"><div class=\"baz\"><a href=\"foo\" class=\"bar\">first</a></div></body></html>";

    private static org.w3c.dom.Document document;
    private static org.w3c.dom.Element root, body;

    @BeforeAll
    public static void setUp() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(html));
            document = builder.parse(inputSource);
            System.out.println("document parsed successfully");
            root = document.getDocumentElement();
            body = (Element)document.getElementsByTagName("body").item(0);
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

    @Test
    public void testGetAttributesOfDiv() {
        org.w3c.dom.Element div = (Element) document.getElementsByTagName("div").item(0);
        org.w3c.dom.NamedNodeMap attributes = div.getAttributes();
        assertEquals(1, attributes.getLength());
        assertEquals("class", attributes.item(0).getNodeName());
        assertEquals("baz", attributes.item(0).getNodeValue());
    }

    @Test
    public void testGetAttributesOfA() {
        org.w3c.dom.Element a = (Element) document.getElementsByTagName("a").item(0);
        org.w3c.dom.NamedNodeMap attributes = a.getAttributes();
        assertEquals(2, attributes.getLength());
        assertEquals("foo", attributes.getNamedItem("href").getNodeValue());
        assertEquals("bar", attributes.getNamedItem("class").getNodeValue());
    }

    @Test
    public void testGetOwnerDocument() {
        org.w3c.dom.Document ownerDocument = root.getOwnerDocument();
        assertEquals(document, ownerDocument);
        ownerDocument = body.getOwnerDocument();
        assertEquals(document, ownerDocument);
    }

    @Test
    public void testHasChildNodes() {
        assertTrue(root.hasChildNodes());
        assertTrue(body.hasChildNodes());
        org.w3c.dom.Element head = (Element) document.getElementsByTagName("head").item(0);
        assertFalse(head.hasChildNodes());
    }

    @Test
    public void testCloneNodeOfRoot() {
        Node cloned = root.cloneNode(true);
        assertInstanceOf(Element.class, cloned);
        Element clonedElm = (Element) cloned;
        assertFalse(root.isSameNode(clonedElm));
        assertEquals(2, clonedElm.getChildNodes().getLength());
        assertEquals(0, clonedElm.getAttributes().getLength());
    }

    @Test
    public void testCloneNodeOfA() {
        org.w3c.dom.Element a = (Element) document.getElementsByTagName("a").item(0);
        Node cloned = a.cloneNode(true);
        assertInstanceOf(Element.class, cloned);
        Element clonedElm = (Element) cloned;
        assertFalse(a.isSameNode(clonedElm));
        assertEquals(1, clonedElm.getChildNodes().getLength());
        assertEquals(2, clonedElm.getAttributes().getLength());
    }

    @Test
    public void testIsSupported() {
        assertFalse(document.isSupported("feature", "version"));
    }

    @Test
    public void testGetNamespaceURI() {
        assertNull(root.getNamespaceURI());
        assertNull(body.getNamespaceURI());
    }

    @Test
    public void testGetPrefix() {
        assertNull(root.getPrefix());
        assertNull(body.getPrefix());
    }

    @Test
    public void testGetLocalName() {
        assertEquals("html", root.getLocalName());
        assertEquals("body", body.getLocalName());
    }

    @Test
    public void testHasAttributes() {
        assertFalse(root.hasAttributes());
        assertTrue(body.hasAttributes());
    }

    @Test
    public void testGetBaseURI() {
        assertNull(root.getBaseURI());
        assertNull(body.getBaseURI());
    }

    @Test
    public void testCompareDocumentPositions() {
        short position = root.compareDocumentPosition(body);
        assertEquals(Node.DOCUMENT_POSITION_CONTAINED_BY | Node.DOCUMENT_POSITION_FOLLOWING, position);
        position = body.compareDocumentPosition(root);
        assertEquals(Node.DOCUMENT_POSITION_CONTAINS | Node.DOCUMENT_POSITION_PRECEDING, position);
    }

    @Test
    public void testGetTextContent() {
        assertEquals("first", root.getTextContent());
    }

    @Test
    public void testIsSameNode() {
        assertTrue(root.isSameNode(root));
        assertFalse(root.isSameNode(body));
        Element cloned = (Element) root.cloneNode(false);
        assertFalse(root.isSameNode(cloned));
    }

    @Test
    public void testLookupPrefix() {
        assertNull(root.lookupPrefix("http://www.w3.org/1999/xhtml"));
    }

    @Test
    public void testIsDefaultNamespace() {
        assertFalse(root.isDefaultNamespace("http://www.w3.org/1999/xhtml"));
    }

    @Test
    public void testLookupNamespaceURI() {
        assertNull(root.lookupNamespaceURI(""));
        assertNull(root.lookupNamespaceURI(null));
    }

    @Test
    public void testIsEqualNode() {
        assertTrue(root.isEqualNode(root));
        assertFalse(root.isEqualNode(body));
        Element cloned = (Element) root.cloneNode(true);
        assertTrue(root.isEqualNode(cloned));
        cloned = (Element) body.cloneNode(false);
        assertFalse(root.isEqualNode(cloned));
    }

    @Test
    public void testUserData() {
        root.setUserData("key", "value", null);
        assertEquals("value", root.getUserData("key"));
        root.setUserData("key", null, null);
        assertNull(root.getUserData("key"));
    }

    @Test
    public void testGetTagName() {
        assertEquals("html", root.getTagName());
        assertEquals("body", body.getTagName());
    }

    @Test
    public void testGetAttribute() {
        assertEquals("", root.getAttribute(null));
        assertEquals("", root.getAttribute("class"));
        assertEquals("myBody", body.getAttribute("id"));
    }

    @Test
    public void testGetAttributeNode() {
        assertNull(root.getAttributeNode(null));
        assertNull(root.getAttributeNode("class"));
        org.w3c.dom.Attr attr = body.getAttributeNode("id");
        assertEquals("id", attr.getName());
        assertEquals("myBody", attr.getValue());
    }

    @Test
    public void testGetElementsByTagNameOfRoot() {
        org.w3c.dom.NodeList list = root.getElementsByTagName("head");
        assertEquals(1, list.getLength());
        assertEquals("head", list.item(0).getNodeName());
        list = root.getElementsByTagName("a");
        assertEquals(1, list.getLength());
        assertEquals("a", list.item(0).getNodeName());
        list = root.getElementsByTagName("");
        assertEquals(0, list.getLength());
        list = root.getElementsByTagName("*");
        assertEquals(4, list.getLength());
        assertEquals("head", list.item(0).getNodeName());
        assertEquals("body", list.item(1).getNodeName());
        assertEquals("div", list.item(2).getNodeName());
        assertEquals("a", list.item(3).getNodeName());
    }

    @Test
    public void testGetElementsByTagNameOfBody() {
        org.w3c.dom.NodeList list = body.getElementsByTagName("div");
        assertEquals(1, list.getLength());
        assertEquals("div", list.item(0).getNodeName());
        list = body.getElementsByTagName("a");
        assertEquals(1, list.getLength());
        assertEquals("a", list.item(0).getNodeName());
        list = body.getElementsByTagName("");
        assertEquals(0, list.getLength());
        list = body.getElementsByTagName("html");
        assertEquals(0, list.getLength());
        list = body.getElementsByTagName("*");
        assertEquals(2, list.getLength());
        assertEquals("div", list.item(0).getNodeName());
        assertEquals("a", list.item(1).getNodeName());
    }

    @Test
    public void testGetAttributeNS() {
        assertEquals("myBody", body.getAttributeNS(null, "id"));
        assertEquals("", body.getAttributeNS(null, "class"));
        assertEquals("", body.getAttributeNS("http://www.w3.org/1999/xhtml", "id"));
    }

    @Test
    public void testGetAttributeNodeNS() {
        org.w3c.dom.Attr attr = body.getAttributeNodeNS(null, "id");
        assertNotNull(attr);
        assertEquals("myBody", attr.getValue());
        attr = body.getAttributeNodeNS(null, "class");
        assertNull(attr);
        attr = body.getAttributeNodeNS("http://www.w3.org/1999/xhtml", "id");
        assertNull(attr);
    }

    @Test
    public void testGetElementsByTagNameNS() {
        org.w3c.dom.NodeList list = root.getElementsByTagNameNS(null, "div");
        assertEquals(1, list.getLength());
        list = root.getElementsByTagNameNS(null, "*");
        assertEquals(4, list.getLength());
        list = root.getElementsByTagNameNS("*", "div");
        assertEquals(1, list.getLength());
        list = root.getElementsByTagNameNS("*", "*");
        assertEquals(4, list.getLength());
        assertEquals("head", list.item(0).getNodeName());
        assertEquals("body", list.item(1).getNodeName());
        assertEquals("div", list.item(2).getNodeName());
        assertEquals("a", list.item(3).getNodeName());
        list = root.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "*");
        assertEquals(0, list.getLength());
    }

    @Test
    public void testHasAttribute() {
        assertFalse(root.hasAttribute(null));
        assertFalse(root.hasAttribute("id"));
        assertTrue(body.hasAttribute("id"));
    }

    @Test
    public void testHasAttributeNS() {
        assertFalse(root.hasAttributeNS(null, null));
        assertFalse(root.hasAttributeNS(null, "id"));
        assertTrue(body.hasAttributeNS(null, "id"));
        assertFalse(body.hasAttributeNS("http://www.w3.org/1999/xhtml", "id"));
    }

    @Test
    public void testElementNamespace() {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <math xmlns=\"http://www.w3.org/1998/Math/MathML\">\n" +
                //"  <math>\n" +
                "   <annotation-xml encoding=\"MathML-Presentation\" />\n" +
                "  </math>\n" +
                " </body>\n" +
                "</html>";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(html));
            org.w3c.dom.Document docWithMath = builder.parse(inputSource);
            assertNotNull(docWithMath);
            NodeList list = docWithMath.getElementsByTagName("math");
            assertEquals(1, list.getLength());
            assertEquals("math", list.item(0).getNodeName());
            assertEquals("http://www.w3.org/1998/Math/MathML", list.item(0).getNamespaceURI());
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new NamespaceContext() {
                @Override public Iterator getPrefixes(String prefix) { return null; }
                @Override public String getPrefix(String namespaceURI) { return null; }
                @Override public String getNamespaceURI(String prefix) {
                    if ("math".equals(prefix)) { return "http://www.w3.org/1998/Math/MathML"; }
                    return XMLConstants.NULL_NS_URI;
                }
            });
            XPathExpression expression = xPath.compile("//math:annotation-xml");
            NodeList nodeList = (NodeList) expression.evaluate(docWithMath, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            assertEquals("annotation-xml", nodeList.item(0).getNodeName());
            assertEquals("http://www.w3.org/1998/Math/MathML", nodeList.item(0).getNamespaceURI());
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testXpathWithNamespace() {
        String xmlData = "<store xmlns=\"http://example.com\" "
                + "xmlns:prod=\"http://example.com\">"
                + "<name>Central Mega Store</name>"
                + "<prod:item><prod:title>Wireless Mouse</prod:title></prod:item></store>";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlData));
            org.w3c.dom.Document docWithNS = builder.parse(inputSource);
            assertNotNull(docWithNS);
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new NamespaceContext() {
                @Override public Iterator getPrefixes(String prefix) { return null; }
                @Override public String getPrefix(String namespaceURI) { return null; }
                @Override public String getNamespaceURI(String prefix) {
                    if ("s".equals(prefix)) return "http://example.com";
                    if ("p".equals(prefix)) return "http://example.com";
                    return XMLConstants.NULL_NS_URI;
                }
            });
            String storeName = (String) xPath.evaluate("/s:store/s:name", docWithNS, XPathConstants.STRING);
            String itemTitle = (String) xPath.evaluate("/s:store/p:item/p:title", docWithNS, XPathConstants.STRING);
            assertEquals("Central Mega Store", storeName);
            assertEquals("Wireless Mouse", itemTitle);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAppendElementChild() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(html));
            org.w3c.dom.Document docToBeModified = builder.parse(inputSource);
            assertNotNull(docToBeModified);
            org.w3c.dom.Element body = (Element) docToBeModified.getElementsByTagName("body").item(0);
            org.w3c.dom.Element hr = docToBeModified.createElement("hr");
            org.w3c.dom.Node addedNode = body.appendChild(hr);
            assertTrue(addedNode.isSameNode(hr));
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("/html/body/hr");
            NodeList nodeList = (NodeList) expression.evaluate(docToBeModified, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            assertEquals("hr", nodeList.item(0).getNodeName());
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAppendTextChild() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(html));
            org.w3c.dom.Document docToBeModified = builder.parse(inputSource);
            assertNotNull(docToBeModified);
            org.w3c.dom.Element body = (Element) docToBeModified.getElementsByTagName("body").item(0);
            org.w3c.dom.Text text = docToBeModified.createTextNode("second");
            org.w3c.dom.Node added = body.appendChild(text);
            assertTrue(added.isSameNode(text));
            String content = body.getTextContent();
            assertEquals("firstsecond", content);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
