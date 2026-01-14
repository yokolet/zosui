package zosui.parser;

import org.junit.jupiter.api.Test;
import zosui.nodes.Element;

import static org.junit.jupiter.api.Assertions.*;

public class BasicTest {
    private static String html = """
            <html>
              <head></head>
              <body>
                <div class='baz'><a href="foo" class="bar">first</a></div>
              </body>
            </html>
        """;

    @Test
    public void findTags() {
        zosui.nodes.Document doc = Parser.parse(html, "");
        zosui.nodes.Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());

        zosui.nodes.Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());

        body.children().forEach(child -> {
            if (child instanceof Element) {
                Element el = (Element) child;
                assertEquals("div", el.tagName());
            }
        });
    }

    @Test
    public void parseFragment() {
        String fragment = "<!DOCTYPE html><p>hi";
        zosui.nodes.Document doc = Parser.parse(fragment, "");
        zosui.nodes.Element body = doc.body();
        assertNotNull(body);
        Element pTag = body.firstElementChild();
        assertNotNull(pTag);
        zosui.nodes.Node node = pTag.firstChild();
        assertNotNull(node);
        assertEquals("hi", node.nodeValue());
    }
}
