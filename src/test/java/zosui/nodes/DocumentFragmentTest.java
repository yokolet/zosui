package zosui.nodes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import zosui.parser.Parser;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentFragmentTest {
    private static Document document;
    private static Parser parser;

    @BeforeAll
    public static void setUp() {
        document = new Document("");
        parser = Parser.htmlParser();
        parser.setTrackPosition(true);
        parser.setTrackErrors(100);
    }

    @Test
    public void testCreateDocumentFragment() {
        DocumentFragment documentFragment = document.createDocumentFragment();
        assertNotNull(documentFragment);
        assertEquals("#document-fragment", documentFragment.getNodeName());
        assertNull(documentFragment.getNodeValue());
        assertEquals(Node.DOCUMENT_FRAGMENT_NODE, documentFragment.getNodeType());
        assertEquals(document, documentFragment.getOwnerDocument());
        assertNull(documentFragment.getParentNode());
    }

    @Test
    public void testCreateDocumentFragmentWithContent() {
        String fragmentString = "<div><p>おはようございます</p></div>";
        List<zosui.nodes.Node> nodes = parser.parseFragmentInput(new StringReader(fragmentString), document, "");
        assertEquals(1, nodes.size());
        zosui.nodes.Node tempRoot = nodes.get(0);
        assertEquals("div", tempRoot.getNodeName());
        zosui.nodes.DocumentFragment documentFragment = (zosui.nodes.DocumentFragment) document.createDocumentFragment();
        assertEquals(document, documentFragment.getOwnerDocument());
        Node adopted = document.adoptNode(tempRoot); // context node might provide another document instance
        assertEquals(document, adopted.getOwnerDocument());
        documentFragment.appendChild(adopted);
        assertEquals(1, documentFragment.getChildNodes().getLength());
        assertEquals("おはようございます", documentFragment.getFirstChild().getTextContent());
    }
}
