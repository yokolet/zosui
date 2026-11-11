package zosui.helper;

import zosui.nodes.Node;

import java.util.Set;

public final class Html5Util {
    private Html5Util() {}

    private static Set<String> voidElements = Set.of(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "link", "meta", "param", "source", "track", "wbr");

    public static boolean isVoidElement(Node node) {
        return voidElements.contains(node.getNodeName().toLowerCase());
    }

    private static Set<String> followedBySpecificElements = Set.of(
            "ul", "ol", "menu", "dl", "table", "tr", "select", "optgroup");

    public static boolean isFollowedBySpecificElement(Node node) {
        return followedBySpecificElements.contains(node.getNodeName().toLowerCase());
    }

    private static Set<String> listChildren = Set.of("li", "script", "template");
    private static Set<String> descriptionListChildren = Set.of("dt", "dd", "div", "script", "template");
    private static Set<String> tableChildren = Set.of("caption", "colgroup", "thead", "tbody", "tfoot", "tr");
    private static Set<String> tableRowChildren = Set.of("th", "td");
    private static Set<String> selectChildren = Set.of("option", "optgroup", "script", "template");
    private static Set<String> optgroupChildren = Set.of("option");

    public static boolean isValidChild(Node parent, Node child) {
        String parentName = parent.getNodeName().toLowerCase();
        String childName = child.getNodeName().toLowerCase();
        return switch (parentName) {
            case "ul", "ol", "menu" -> listChildren.contains(childName);
            case "dl" -> descriptionListChildren.contains(childName);
            case "table" -> tableChildren.contains(childName);
            case "tr" -> tableRowChildren.contains(childName);
            case "select" -> selectChildren.contains(childName);
            case "optgroup" -> optgroupChildren.contains(childName);
            default -> true;
        };
    }

    public static boolean isValidToAppend(Node parent, Node child) {
        if (isVoidElement(parent)) { throw new ValidationException(parent.getNodeName() + " cannot have a child"); }
        if (isFollowedBySpecificElement(parent) && !isValidChild(parent, child)) {
            throw new ValidationException(parent.getNodeName() + " cannot have a child: " + child.getNodeName());
        }
        return true;
    }
}
