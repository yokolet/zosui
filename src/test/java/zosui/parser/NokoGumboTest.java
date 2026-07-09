package zosui.parser;

import org.junit.jupiter.api.Test;
import org.w3c.dom.*;
import zosui.helper.TextUtil;
import zosui.select.Elements;

import javax.xml.xpath.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NokoGumboTest {
    String buffer() {
        return """
                <html>
                        <head>
                          <meta charset="utf-8"/>
                          <title>hello world</title>
                          <script> if (a < b) alert(1) </script>
                        </head>
                        <body>
                          <h1>hello world</h1>
                          <main>
                            <span>content</span>
                          </main>
                          <!-- test comment -->
                          <form>
                            <textarea>foo<x>bar</textarea>
                          </form>
                        </body>
                      </html>
                """;
    }

    @Test
    public void testElementText() {
        Document document = Parser.parse(buffer(), "");
        NodeList list = document.getElementsByTagName("span");
        assertEquals(1, list.getLength());
        Element span = (Element) list.item(0);
        assertEquals("content", span.getTextContent());
    }

    @Test
    public void testElementCDATATextarea() {
        Document document = Parser.parse(buffer(), "");
        NodeList list = document.getElementsByTagName("textarea");
        assertEquals(1, list.getLength());
        Element textarea = (Element) list.item(0);
        assertEquals("foo<x>bar", textarea.getTextContent());
    }

    @Test
    public void testElementCDATAScript() {
        Document document = Parser.parse(buffer(), "");
        NodeList list = document.getElementsByTagName("script");
        assertEquals(1, list.getLength());
        Element script = (Element) list.item(0);
        assertEquals("<script> if (a < b) alert(1) </script>", script.toString());
    }

    @Test
    public void testAttrValue() {
        Document document = Parser.parse(buffer(), "");
        NodeList list = document.getElementsByTagName("meta");
        assertEquals(1, list.getLength());
        Element meta = (Element) list.item(0);
        NamedNodeMap attributes = meta.getAttributes();
        assertEquals("utf-8", attributes.getNamedItem("charset").getNodeValue());
    }

    @Test
    public void testComment() {
        Document document = Parser.parse(buffer(), "");
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("//comment()");
            Node comment = (Node) expression.evaluate(document, XPathConstants.NODE);
            assertEquals(" test comment ", comment.getTextContent());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUnknownElement() {
        Document document = Parser.parse(buffer(), "");
        NodeList list = document.getElementsByTagName("main");
        assertEquals(1, list.getLength());
        Element main = (Element) list.item(0);
        assertEquals("main", main.getNodeName());
    }

    @Test
    public void testIO() {
        Reader html = new StringReader(buffer());
        Parser parser = Parser.htmlParser();
        Document document = parser.parseInput(html, "");
        NodeList list = document.getElementsByTagName("form");
        assertEquals(1, list.getLength());
        Element form = (Element) list.item(0);
        NodeList formChildren = getElementChildren(form.getChildNodes());
        assertEquals("textarea", formChildren.item(0).getNodeName());
    }

    NodeList getElementChildren(NodeList list) {
        Elements elements = new Elements();
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                elements.add((zosui.nodes.Element)n);
            }
        }
        return elements;
    }

    @Test
    public void testNull() {
        // The parser doesn't accept null.
        Document document = Parser.parse("", "");
        NodeList list = document.getElementsByTagName("body");
        assertEquals(1, list.getLength());

        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(100);
        List<zosui.nodes.Node> nodes = parser.parseFragmentInput("", null,"");
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testHtml5DocType() {
        Document document = Parser.parse("<!DOCTYPE html><html></html>", "");
        String html = TextUtil.stripNewlines(document.toString());
        assertTrue(html.matches("(?i)<!DOCTYPE html>.*"));
    }
    /*
    The parser converts to lower case doctype.
     */
}
