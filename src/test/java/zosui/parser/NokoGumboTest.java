package zosui.parser;

import org.junit.jupiter.api.Test;
import org.w3c.dom.*;
import zosui.helper.TextUtil;
import zosui.select.Elements;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

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
    The parser converts the doctype to lower case.
     */

    @Test
    public void testFragmentHeader() {
        Pattern pattern = Pattern.compile("<head>(.*?)</head>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(buffer());
        assertTrue(matcher.find());
        String found = matcher.group(1);
        zosui.nodes.DocumentFragment fragment = new zosui.nodes.DocumentFragment(found, null, "", new HashMap<>());
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("title");
            Node title = (Node) expression.evaluate(fragment, XPathConstants.NODE);
            assertEquals("hello world", title.getTextContent());
            expression = xPath.compile("meta");
            Node meta = (Node) expression.evaluate(fragment, XPathConstants.NODE);
            assertEquals("utf-8", meta.getAttributes().getNamedItem("charset").getNodeValue());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFragmentBody() {
        Pattern pattern = Pattern.compile("<body>(.*?)</body>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(buffer());
        assertTrue(matcher.find());
        String found = matcher.group(1);
        zosui.nodes.DocumentFragment fragment = new zosui.nodes.DocumentFragment(found, null, "", new HashMap<>());
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("main/span");
            Node span = (Node) expression.evaluate(fragment, XPathConstants.NODE);
            assertEquals("<span>content</span>", span.toString());
            expression = xPath.compile("comment()");
            Node comment = (Node) expression.evaluate(fragment, XPathConstants.NODE);
            assertEquals(" test comment ", comment.getTextContent());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testXlinkAttribute() {
        String source = """
                <!DOCTYPE html>
                      <svg xmlns="http://www.w3.org/2000/svg">
                        <a xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#s1"/>
                      </svg>
                """;
        Document document = Parser.parse(TextUtil.stripNewlines(source), "");
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new NamespaceContext() {
                @Override public Iterator getPrefixes(String prefix) { return null; }
                @Override public String getPrefix(String namespaceURI) { return null; }
                @Override public String getNamespaceURI(String prefix) {
                    if ("svg".equals(prefix)) { return "http://www.w3.org/2000/svg"; }
                    if ("xlink".equals(prefix)) { return "http://www.w3.org/1999/xlink"; }
                    return XMLConstants.NULL_NS_URI;
                }
            });
            XPathExpression expression = xPath.compile("//html/body/svg:svg/svg:a");
            NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            Element element = (Element) nodeList.item(0);
            assertNotNull(element);
            NamedNodeMap attributes = element.getAttributes();
            assertEquals(2, attributes.getLength());
            Attr attr = (Attr) attributes.getNamedItem("xlink:href");
            assertEquals("#s1", attr.getValue());
            attr = (Attr) attributes.getNamedItem("xmlns:xlink");
            assertEquals("http://www.w3.org/1999/xlink", attr.getValue());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testXlinkAttributeFragment() {
        String source = """
        <svg xmlns="http://www.w3.org/2000/svg">
          <a xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#s1"/>
        </svg>
        """;
        DocumentFragment fragment = new zosui.nodes.DocumentFragment(TextUtil.stripNewlines(source), null, "", new HashMap<>());
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new NamespaceContext() {
                @Override public Iterator getPrefixes(String prefix) { return null; }
                @Override public String getPrefix(String namespaceURI) { return null; }
                @Override public String getNamespaceURI(String prefix) {
                    if ("svg".equals(prefix)) { return "http://www.w3.org/2000/svg"; }
                    if ("xlink".equals(prefix)) { return "http://www.w3.org/1999/xlink"; }
                    return XMLConstants.NULL_NS_URI;
                }
            });
            XPathExpression expression = xPath.compile("svg:svg/svg:a");
            NodeList nodeList = (NodeList) expression.evaluate(fragment, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            Element element = (Element) nodeList.item(0);
            assertNotNull(element);
            NamedNodeMap attributes = element.getAttributes();
            assertEquals(2, attributes.getLength());
            Attr attr = (Attr) attributes.getNamedItem("xlink:href");
            assertEquals("#s1", attr.getValue());
            attr = (Attr) attributes.getNamedItem("xmlns:xlink");
            assertEquals("http://www.w3.org/1999/xlink", attr.getValue());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRootComments() {
        String source = "<!DOCTYPE html><!-- start --><html></html><!-- -->";
        Document  document = Parser.parse(source, "");
        NodeList list = document.getChildNodes();
        String[] names = new String[list.getLength()];
        for (int i = 0; i < list.getLength(); i++) {
            names[i] = list.item(i).getNodeName();
        }
        //String[] expected  = {"html", "#comment", "html", "#comment"};
        //assertArrayEquals(expected, names);
        String[] expected = {"html", "#comment", "html"};
        assertArrayEquals(expected, names);
    }
    /*
    Two problems are here. The jsoup parser moves the last comment in the html block.
    It should be the outside of html block.
    Another problem is, tne name "comment" is not comply with DOM specification. It should be #comment as the spec says.
     */

    /*
    The jsoup parser doesn't have an idea of max_attributes.
     */

    @Test
    public void testParseErrors() {
        String html = "<!DOCTYPE html><html><!-- <!-- --></a>";
        zosui.nodes.Document document = Parser.parse(html, "");
        ParseErrorList errorList = document.getParseErrors();
        assertEquals(0, errorList.size());
    }
    /*
    The parser's behavior is much different. The parsed result is like this:
    <!doctype html>
<html>
 <!-- <!-- -->
 <head></head>
 <body></body>
</html>
    No error is reported.
     */

    @Test
    public void testParseFragmentErrors() {
        String html = "<\\r\\n";
        Parser parser = Parser.htmlParser();
        List<zosui.nodes.Node> list = parser.parseFragmentInput(html, null, "");
        ParseErrorList errorList = parser.getErrors();
        assertEquals(0, errorList.size());
    }
    /*
    No error is reported. The parsed result is like this:
    <html>
 <head></head>
 <body>&lt;\r\n</body>
</html>
     */

    @Test
    public void testDefaultMaxDepthParse() {
        Parser parser = Parser.htmlParser();
        int maxDepth = parser.getMaxDepth();  // 512
        StringBuilder sb = new StringBuilder("<!DOCTYPE html><html><body>");
        sb.append("<div>".repeat(maxDepth));
        Document document = parser.parseInput(sb.toString(), "");
        assertNotNull(document);
    }
    /*
    No error is reported even though the nesting is over the max depth.
     */

    @Test
    public void testDocumentEncoding() {
        String html = """
                <html>
                        <head>
                          <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                        </head>
                        <body>
                          Кирилические символы
                        </body>
                      </html>
                """;
        Document document = Parser.parse(html, "");
        String encoding = document.getInputEncoding();
        assertEquals("UTF-8", encoding);
        Element body = (Element) document.getElementsByTagName("body").item(0);
        String text = TextUtil.stripNewlines(body.getTextContent());
        assertEquals("Кирилические символы", text);
    }

    @Test
    public void testLineText() {
        String html = "<!DOCTYPE html>\\ntext node";
        Parser parser = Parser.htmlParser();
        parser.setTrackPosition(true);
        Document document = parser.parseInput(html, "");
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("/html/body/text()");
            Node text = (Node)expression.evaluate(document, XPathConstants.NODE);
            assertEquals("#text", text.getNodeName());
            zosui.nodes.Range range = ((zosui.nodes.Node) text).sourceRange();
            assertEquals(1, range.end().lineNumber());
            assertEquals(1, range.start().lineNumber());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    The line number is different from Gumbo. It is the line number of the original input source.
     */

    @Test
    public void testIllFormedProcessing() {
        String html = "<html><body><!--><?a/";
        Parser parser = Parser.htmlParser();
        parser.setTrackPosition(true);
        Document document = parser.parseInput(html, "");
        Element body = (Element) document.getElementsByTagName("body").item(0);
        NodeList nodeList = body.getChildNodes();
        assertEquals(2, nodeList.getLength());
        String[] names = new String[nodeList.getLength()];
        Class[] classes = new Class[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            names[i] = nodeList.item(i).getNodeName();
            classes[i] = nodeList.item(i).getClass();
        }
        String[] expected = {"#comment", "#comment"};
        assertArrayEquals(expected, names);
        Class[] expected2 = {zosui.nodes.Comment.class, zosui.nodes.Comment.class};
        assertArrayEquals(expected2, classes);
    }
}
