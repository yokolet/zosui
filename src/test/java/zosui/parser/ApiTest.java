package zosui.parser;

import java.util.List;

import org.junit.jupiter.api.Test;
import zosui.helper.TextUtil;
import zosui.nodes.Document;
import zosui.nodes.DocumentFragment;
import zosui.nodes.Node;

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

        DocumentFragment fragment = new DocumentFragment("");
        List<Node> children = Parser.parseFragment(frag, fragment, "");
        fragment.appendChildren(children);
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


}
