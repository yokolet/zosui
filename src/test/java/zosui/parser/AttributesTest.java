package zosui.parser;

import org.junit.jupiter.api.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import zosui.nodes.Attribute;
import zosui.nodes.DocumentFragment;
import zosui.select.Elements;

import javax.xml.xpath.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttributesTest {
    @Test
    public void testSerializeAttribute() {
        String html = "<div id='foo' class=\"bar baz\"></div>";
        DocumentFragment fragment = new DocumentFragment(html, null, "", new HashMap<>());
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("//div");
            NodeList nodeList = (NodeList) expression.evaluate(fragment, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            zosui.nodes.Element element = (zosui.nodes.Element)nodeList.item(0);
            assertEquals("div", element.nodeName());
            NamedNodeMap map = element.getAttributes();
            assertEquals(2, map.getLength());
            Attribute idAttr = (Attribute)map.getNamedItem("id");
            Attribute classAttr = (Attribute)map.getNamedItem("class");
            assertEquals("id=\"foo\"", idAttr.html());
            assertEquals("class=\"bar baz\"", classAttr.html());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDuplicateAttributes() {
        StringBuilder sb = new StringBuilder("<span ");
        for (int i = 0; i < 10; i++) {
            sb.append("a").append(i).append("=\"1\" ");
        }
        sb.append(" a9=\"2\" />");
        DocumentFragment fragment = new DocumentFragment(sb.toString(), null, "", new HashMap<>());
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("//span");
            NodeList nodeList = (NodeList) expression.evaluate(fragment, XPathConstants.NODESET);
            assertEquals(1, nodeList.getLength());
            zosui.nodes.Element span = (zosui.nodes.Element)nodeList.item(0);
            NamedNodeMap attributes = span.getAttributes();
            assertEquals(10, attributes.getLength());
            assertEquals("1", attributes.getNamedItem("a9").getNodeValue());
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    Needs to handle max_attributes option?
     */
}
