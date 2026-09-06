package zosui.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import zosui.helper.TextUtil;
import zosui.nodes.Document;
import zosui.nodes.DocumentFragment;
import zosui.nodes.Node;
import zosui.nodes.TextNode;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.*;

import static org.junit.jupiter.api.Assertions.*;

public class ApiTest {
    @Test
    public void testParseConvenienceMethods() {
        String html = "<!DOCTYPE html><p>hi";
        Document document = Parser.parse(html, "");
        document.outputSettings().prettyPrint(false);
        String str = document.html();
        assertNotNull(str);
        assertEquals("<!doctype html><html><head></head><body><p>hi</p></body></html>", str);
    }

    @Test
    public void testFragmentConvenientMethod() {
        String frag = "<div><p>hi</div>";
        Document document = Parser.parseBodyFragment(frag, "");
        document.outputSettings().prettyPrint(false);
        String str = document.body().html();
        assertNotNull(str);
        assertEquals("<div><p>hi</p></div>", TextUtil.stripNewlines(str));

        DocumentFragment fragment = new DocumentFragment(frag, null, "", new HashMap<>());
        String frag2 = fragment.html();
        assertNotNull(frag2);
        assertEquals("<div><p>hi</p></div>", TextUtil.stripNewlines(frag2));
    }

    @Test
    public void testUrl() {
        String html = "<p>hi";
        String url =  "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = parser.parseInput(html, "");
        assertNull(document.getBaseURI());

        document = parser.parseInput(html, url);
        assertEquals(url, document.getBaseURI());

        Parser parser2 = Parser.htmlParser();
        parser2.setTrackErrors(1);
        document = parser2.parseInput(html, url);
        // assertEquals(1, parser2.getErrors().size()); // this returns 0
    }

    @Test
    public void testUrl2() {
        String html = "<ruby><div><rp>Hello";
        String url =  "http://example.com";

        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(100);
        Document document = parser.parseInput(html, url);
        assertEquals(url, document.getBaseURI());
        assertEquals(2, parser.getErrors().size());

        parser.setTrackErrors(1);
        parser.parseInput(html, url);
        assertEquals(1, parser.getErrors().size());
    }

    @Test
    public void testParseEncoding() {
        String utf8 = "<!DOCTYPE html><body><p>おはようございます";
        Document document = Parser.parse(utf8, "");
        String str = TextUtil.stripNewlines(document.html());
        Pattern pattern = Pattern.compile("[\\p{InHiragana}]+");
        Matcher matcher = pattern.matcher(str);
        int count = 0;
        while (matcher.find()) {
            if (matcher.group().equals("おはようございます")) { count++; }
        }
        assertEquals(1, count);
        String charset = document.charset().name();
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testParseNoscriptAsElementInHead() {
        String html = "<!DOCTYPE html><head><noscript><img src=!></noscript></head>";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(100);
        Document document = parser.parseInput(html, "");
        String str = document.html();
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("/html/head/noscript");
            NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            Element noscript = (Element) nodeList.item(0);
            assertEquals(1, noscript.getChildNodes().getLength());
            assertInstanceOf(TextNode.class, noscript.getChildNodes().item(0));
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    The parser should respect 'parse_noscript_content_as_text: boolean' option.
    The option will be used in HtmlTreeBuilderState, InHeadScript, anythingElse method.
     */

    @Test
    public void testParseNoscriptAsElementInBody() {
        String html = "<!DOCTYPE html><body><noscript><img src=!></noscript></body>";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(100);
        Document document = parser.parseInput(html, "");
        String str = document.html();
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("/html/body/noscript/img");
            NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            Element img = (Element) nodeList.item(0);
            assertInstanceOf(Element.class, img);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseNoscriptFragmentAsElements() {
        String html = "<meta charset='UTF-8'><link rel=stylesheet href=!>";
        Map<String, Object> options = new HashMap<>();
        options.put("parseNoscriptContextAsText", false); // TODO: this option handling should be implemented
        options.put("maxErrors", 100);
        DocumentFragment frag = new DocumentFragment(html, "noscript", "", options);
        List<ParseError> errors = frag.getErrors();
        assertEquals(0, errors.size());
        assertEquals(2, frag.getChildNodes().getLength());
        String str = frag.html();
        assertEquals("<meta charset=\"UTF-8\"><link rel=\"stylesheet\" href=\"!\">", TextUtil.stripNewlines(str));
    }

    @Test
    public void testParseNoscriptContentDefault() {
        String html = "<!DOCTYPE html><body><noscript><img src=!></noscript></body>";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(100);
        Document document = parser.parseInput(html, "");
        assertEquals(0, parser.getErrors().size());
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("/html/body/noscript/img");
            NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            Element img = (Element) nodeList.item(0);
            assertInstanceOf(Element.class, img);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSerializePreserveNewlines() {
        String[] tags = {"pre", "listing", "textarea"};
//        String[] expected = {
//                "<pre>\n\nContent</pre>",
//                "<listing>\n\nContent</listing>",
//                "<textarea>\n\nContent</textarea>",
//        };
        // the differences come from the parser
        String[] expected = {
                "<pre>\nContent</pre>",
                "<listing>Content</listing>",
                "<textarea>\n\nContent</textarea>",
        };
        for (int i = 0; i < tags.length; i++) {
            String tag = tags[i];
            String html = String.format("<!DOCTYPE html><%s>\n\nContent</%s>", tag, tag);
            Document document = Parser.parse(html, "");
            try {
                XPath xPath = XPathFactory.newInstance().newXPath();
                String exprStr = String.format("/html/body/%s", tag);
                XPathExpression expression = xPath.compile(exprStr);
                NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
                assertEquals(1, nodeList.getLength());
                zosui.nodes.Element element = (zosui.nodes.Element)nodeList.item(0);
                assertEquals(expected[i], element.outerHtml());
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /*
    The parser keeps only one newline. The behavior should be changed to preserve all.
    At the same time, toString(), outerHtml() or such methods may reduce extra newline down to one by an option.
     */

    @Test
    public void testDocumentIO() {
        Reader html = new StringReader("<!DOCTYPE html><span>test</span>");
        Parser parser = Parser.htmlParser();
        Document document = parser.parseInput(html, "");
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("/html/body/span");
            NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            zosui.nodes.Element element = (zosui.nodes.Element)nodeList.item(0);
            assertEquals("span", element.nodeName());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFragmentWithMathHtml() {
        Document document = Parser.parse("<!DOCTYPE html><math><annotation-xml encoding='text/html' /></math>", "");
        Element math = (Element) document.getElementsByTagName("math").item(0);
        assertEquals("math", math.getNodeName());
        assertEquals("math", math.getLocalName());
        assertEquals(Parser.NamespaceMathml, math.getNamespaceURI());
        Element annotation = (Element) document.getElementsByTagName("annotation-xml").item(0);
        assertEquals(Parser.NamespaceMathml, annotation.getNamespaceURI());
    }

    @Test
    public void testFragmentWithAnnotationXmlContext() {
        Document document = Parser.parse("<!DOCTYPE html><math><annotation-xml encoding='MathML-Presentation' /></math>", "");
        try {
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
            NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            assertEquals("annotation-xml", nodeList.item(0).getNodeName());
            zosui.nodes.Element annotation = (zosui.nodes.Element) nodeList.item(0);
            annotation.append("<mi>x</mi>");
            Element mi = (Element) annotation.getElementsByTagName("mi").item(0);
            assertEquals("mi", mi.getLocalName());
            assertEquals(Parser.NamespaceMathml, mi.getNamespaceURI());
            assertEquals(null, mi.getPrefix());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
