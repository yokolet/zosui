package zosui.nodes;

import org.w3c.dom.NamedNodeMap;
import zosui.parser.Parser;
import zosui.parser.Tag;

import static zosui.parser.Parser.NamespaceHtml;

public class DocumentFragment extends Element implements org.w3c.dom.DocumentFragment {
    private Parser parser; // the parser used to parse this document
    private final String location;

    /**
     Create a new, empty Document, in the specified namespace.
     @param namespace the namespace of this Document's root node.
     @param baseUri base URI of document
     @see zosui.parser.Parser#parse
     */
    public DocumentFragment(String namespace, String baseUri) {
        this(namespace, baseUri, Parser.htmlParser()); // default HTML parser, but overridable
    }

    private DocumentFragment(String namespace, String baseUri, Parser parser) {
        super(new Tag("body", namespace), baseUri);
        this.location = baseUri;
        this.parser = parser;
    }

    /**
     Create a new, empty Document, in the HTML namespace.
     @param baseUri base URI of document
     @see zosui.parser.Parser#parse
     @see #DocumentFragment(String namespace, String baseUri)
     */
    public DocumentFragment(String baseUri) {
        this(NamespaceHtml, baseUri);
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
}
