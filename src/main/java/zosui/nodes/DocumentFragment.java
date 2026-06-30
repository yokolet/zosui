package zosui.nodes;

import org.w3c.dom.NamedNodeMap;
import zosui.parser.*;

import java.util.List;
import java.util.Map;

public class DocumentFragment extends Element implements org.w3c.dom.DocumentFragment {
    private final ParseErrorList errorList;

    /**
     Create a new, empty DocumentFragment, from a given html string.
     @param html the fragment of HTML to parse
     @param context (optional) the element that this HTML fragment is being parsed for.
     @param baseUri base URI of document
     @param options a map of options
     @see zosui.parser.Parser#parse
     */
    public DocumentFragment(String html, String context, String baseUri, Map<String, Object> options) {
        this(html, context, baseUri, Parser.htmlParser(), options); // default HTML parser, but overridable
    }

    /**
     Create a new, empty DocumentFragment, from a given html string.
     @param html the fragment of HTML to parse
     @param context (optional) the element that this HTML fragment is being parsed for.
     @param baseUri base URI of document
     @param parser the parser to parse the input
     @param options a map of options
     @see zosui.parser.Parser#parse
     */
    private DocumentFragment(String html, String context, String baseUri, Parser parser, Map<String, Object> options) {
        context = context != null ? context : (options.containsKey("context") ? (String) options.get("context") : "body");
        super(new Tag(context), baseUri);
        int max_errors = options.containsKey("max_errors") ? (int) options.get("max_errors") : 0;
        parser.setTrackErrors(max_errors);
        parser.settings(createSettings(options));
        List<Node> children = parser.parseFragmentInput(html, this, "");
        this.appendChildren(children);
        errorList = parser.getErrors();
    }
    private ParseSettings createSettings(Map<String, Object> options) {
        ParseSettings settings = ParseSettings.htmlDefault;
        if (options.containsKey("parse_noscript_content_as_text")) {
            settings.setNoscriptContentAsText((boolean)options.get("parse_noscript_content_as_text"));
        }
        return settings;
    }

    @Override public String getNodeName() { return "#document-fragment"; }
    @Override public String getNodeValue() { return null; }
    @Override public short getNodeType() { return Node.DOCUMENT_FRAGMENT_NODE; }
    @Override public Node getParentNode() { return null; }
    @Override public NamedNodeMap getAttributes() { return null; }
    @Override public org.w3c.dom.Document getOwnerDocument() { return null; }
    @Override public String getNamespaceURI() { return null; }
    @Override public String getPrefix() { return null; }
    @Override public String getLocalName() { return null; }
    @Override public boolean hasAttributes() { return false; }
    @Override public String getBaseURI() { return baseUri().trim().equals("") ? null : baseUri().trim(); }
    @Override public String lookupPrefix(String namespaceURI) { return null; }
    @Override public boolean isDefaultNamespace(String namespaceURI) { return false; }

    public List<ParseError> getErrors() { return errorList; }
}
