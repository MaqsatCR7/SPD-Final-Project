package library.structural.adapter;

import java.util.List;

/**
 * Adaptee: an old external catalog we cannot change.
 * It exports plain text records in the format  TYPE|ID|TITLE|CREATOR|YEAR
 * where TYPE is B (book), M (magazine) or D (dvd).
 */
public class LegacyCatalogSystem {
    public List<String> exportRecords() {
        return List.of(
                "B|LG-101|Clean Code|Robert C. Martin|2008",
                "B|LG-102|Head First Design Patterns|Eric Freeman|2004",
                "M|LG-201|National Geographic|NatGeo Society|2024",
                "D|LG-301|The Social Network|David Fincher|2010",
                "X|LG-999|Broken record|Unknown|abc");
    }
}
