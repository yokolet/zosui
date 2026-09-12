package w3c;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentFragmentTest {
    private static DocumentBuilder builder;
    private static org.w3c.dom.Document document;

    @BeforeAll
    public static void setUp() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            System.out.println("document was created successfully");
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateDocumentFragment() {
        DocumentFragment docFragment = document.createDocumentFragment();
        assertNotNull(docFragment);
        assertEquals("#document-fragment", docFragment.getNodeName());
        assertNull(docFragment.getNodeValue());
        assertEquals(Node.DOCUMENT_FRAGMENT_NODE, docFragment.getNodeType());
        assertEquals(document, docFragment.getOwnerDocument());
        assertNull(docFragment.getParentNode());
    }

    @Test
    public void testCreateDocumentFragmentWithContent() {
        String fragment = "<div><p>おはようございます</p></div>";
        try {
            Document tempDoc = builder.parse(new InputSource(new StringReader(fragment)));
            Node tempRoot = tempDoc.getDocumentElement();
            assertNotNull(tempRoot);
            assertEquals("div", tempRoot.getNodeName());
            DocumentFragment documentFragment = document.createDocumentFragment();
            assertEquals(document, documentFragment.getOwnerDocument());
            assertNotEquals(document, tempRoot.getOwnerDocument());
            Node imported = document.importNode(tempRoot, true);
            assertEquals(document, imported.getOwnerDocument());
            documentFragment.appendChild(imported);
            assertEquals(1, documentFragment.getChildNodes().getLength());
            assertEquals("おはようございます", documentFragment.getFirstChild().getTextContent());
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
