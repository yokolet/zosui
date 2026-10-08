package zosui.nodes;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import zosui.parser.Parser;

import javax.xml.xpath.*;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    @Test
    public void testRangesAndPositionsHaveValueEquality() {
        String html = "xx<p id=1>";
        Parser parser = Parser.htmlParser();
        parser.setTrackPosition(true);
        Document document = parser.parseInput(html, "");
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("/html/body/p");
            Node p = (Node) expression.evaluate(document, XPathConstants.NODE);
            Range pRange = p.sourceRange();
            assertTrue(pRange.isTracked());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTrackPositions() {
        String html = "<p id=1\n class=foo>\n<span>Hello\n &reg;\n there &copy.</span> now.\n <!-- comment --> ";
        Parser parser = Parser.htmlParser().setTrackPosition(true);
        Document document = parser.parseInput(html, "");
        Element span = (Element) document.getElementsByTagName("span").item(0);
        TextNode textNode = (TextNode) span.getFirstChild();
        String wholeText = textNode.getWholeText();
        assertEquals("Hello\n ®\n there ©.", wholeText);
        Range textRange = textNode.sourceRange();
        String textOrig = "Hello\n &reg;\n there &copy.";
        assertEquals(textRange.end().pos() -  textRange.start().pos(), textOrig.length());
    }

    @Test
    public void testLineText() {
        String html = "<!DOCTYPE html>\ntext node";
        Parser parser = Parser.htmlParser().setTrackPosition(true);
        Document document = parser.parseInput(html, "");
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("/html/body/text()");
            zosui.nodes.Node text = (zosui.nodes.Node) expression.evaluate(document, XPathConstants.NODE);
            assertEquals("text node", text.getTextContent());
            Range textRange = text.sourceRange();
            assertTrue(textRange.isTracked());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
