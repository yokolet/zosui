package w3c;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ElementWithNamespaceTest {
    private static String html = "<!DOCTYPE html>\n" +
            "<html>\n" +
            " <head></head>\n" +
            " <body>\n" +
            "  <math xmlns=\"http://www.w3.org/1998/Math/MathML\">\n" +
            "   <annotation-xml encoding=\"MathML-Presentation\" />\n" +
            "  </math>\n" +
            " </body>\n" +
            "</html>";

    private static org.w3c.dom.Document document;
    private static org.w3c.dom.Element root, body, math;

    @BeforeAll
    public static void setUp() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(html));
            document = builder.parse(inputSource);
            System.out.println("document parsed successfully");
            root = document.getDocumentElement();
            body = (Element)document.getElementsByTagName("body").item(0);
            math = (Element)document.getElementsByTagName("math").item(0);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetNamespaceURI() {
        assertNull(root.getNamespaceURI());
        assertNull(body.getNamespaceURI());
        assertEquals("http://www.w3.org/1998/Math/MathML", math.getNamespaceURI());
        assertNull(math.getPrefix());
    }

    @Test
    public void testGetLocalName() {
        assertEquals("html", root.getLocalName());
        assertEquals("body", body.getLocalName());
        assertEquals("math", math.getLocalName());
    }

    @Test
    public void testGetChildNamespace() {
        NodeList list = math.getElementsByTagName("annotation-xml");
        assertEquals(1, list.getLength());
        Element element = (Element)list.item(0);
        assertEquals("http://www.w3.org/1998/Math/MathML", element.getNamespaceURI());
    }
}
