package zosui.parser;

import org.junit.jupiter.api.Test;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import zosui.nodes.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class BasicTest {
    private static String html = """
            <html>
              <head></head>
              <body>
                <div class='baz'><a href="foo" class="bar">first</a></div>
              </body>
            </html>
        """;

    @Test
    public void findTags() {
        zosui.nodes.Document doc = Parser.parse(html, "");
        zosui.nodes.Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());

        zosui.nodes.Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());

        body.children().forEach(child -> {
            if (child instanceof Element) {
                Element el = (Element) child;
                assertEquals("div", el.tagName());
            }
        });
    }

    @Test
    public void parseFragment() {
        String fragment = "<!DOCTYPE html><p>hi";
        zosui.nodes.Document doc = Parser.parse(fragment, "");
        zosui.nodes.Element body = doc.body();
        assertNotNull(body);
        Element pTag = body.firstElementChild();
        assertNotNull(pTag);
        zosui.nodes.Node node = pTag.firstChild();
        assertNotNull(node);
        assertEquals("hi", node.nodeValue());
    }

    @Test
    public void simpleXpath() {
        org.w3c.dom.Document doc = Parser.parse(html, "");
        assertEquals(9, doc.getNodeType());
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            XPathExpression expression = xPath.compile("//div[@class='baz']");
            NodeList nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            assertEquals("div", nodeList.item(0).getNodeName());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void vanillaDOM() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(html));
            org.w3c.dom.Document doc = builder.parse(inputSource);
            assertEquals(9, doc.getNodeType());
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("//div[@class='baz']");
            NodeList nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            assertEquals("div", nodeList.item(0).getNodeName());
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
}
