package zosui.parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zosui.helper.TextUtil;
import zosui.nodes.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuirksModeTest {
    private static Parser parser;

    @BeforeAll
    public static void setUp() {
        parser = Parser.htmlParser();
        parser.setTrackPosition(true);
        parser.setTrackErrors(100);
    }

    @Test
    public void testJsoupQuirksMode() {
        String html = "<p><span><table><tbody><tr><td><span>Hello table data</span></td></tr></tbody></table></span></p>";
        Document document = parser.parseInput(html, "");
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
        // quirks, allows table in p
        assertEquals(
                "<p><span><table><tbody><tr><td><span>Hello table data</span></td></tr></tbody></table></span></p>",
                TextUtil.normalizeSpaces(document.body().html())
        );
    }

    @Test
    public void testJsoupNoQuirksMode() {
        String html = "<!DOCTYPE html><p><span><table><tbody><tr><td><span>Hello table data</span></td></tr></tbody></table></span></p>";
        Document document = parser.parseInput(html, "");
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        // no quirks, p gets closed
        assertEquals(
                "<p><span></span></p><table><tbody><tr><td><span>Hello table data</span></td></tr></tbody></table><p></p>",
                TextUtil.normalizeSpaces(document.body().html())
        );
    }

    @Test
    public void testWithoutParsing() {
        Document document = new Document("");
        Document.QuirksMode mode = document.quirksMode();
        assertNull(mode);
    }

    @Test
    public void testDocumentWithDoctype() {
        String html = "<!DOCTYPE html><p>hello</p>";
        Document document = parser.parseInput(html, "");
        Document.QuirksMode mode = document.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, mode);
    }

    @Test
    public void testDocumentWithoutDoctype() {
        String html = "<html><p>hello</p>";
        Document document = parser.parseInput(html, "");
        Document.QuirksMode mode = document.quirksMode();
        assertEquals(Document.QuirksMode.quirks, mode);
    }
}
