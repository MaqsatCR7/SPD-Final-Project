package library.structural.adapter;

import library.creational.factory.ItemFactory;
import library.model.ItemType;
import library.model.LibraryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ADAPTER pattern: converts the text records of {@link LegacyCatalogSystem}
 * into {@link LibraryItem} objects, so the rest of the system can use the legacy
 * catalog through the {@link CatalogProvider} interface (composition, not inheritance).
 */
public class LegacyCatalogAdapter implements CatalogProvider {
    private static final Map<String, ItemType> TYPE_CODES = Map.of(
            "B", ItemType.BOOK,
            "M", ItemType.MAGAZINE,
            "D", ItemType.DVD);

    private final LegacyCatalogSystem legacySystem;

    public LegacyCatalogAdapter(LegacyCatalogSystem legacySystem) {
        this.legacySystem = legacySystem;
    }

    @Override
    public List<LibraryItem> fetchItems() {
        List<LibraryItem> result = new ArrayList<>();
        for (String record : legacySystem.exportRecords()) {
            try {
                result.add(parse(record));
            } catch (RuntimeException e) {
                System.out.println("   [ADAPTER] skipped invalid record: " + record);
            }
        }
        return result;
    }

    private LibraryItem parse(String record) {
        String[] p = record.split("\\|");
        if (p.length != 5) {
            throw new IllegalArgumentException("wrong field count");
        }
        ItemType type = TYPE_CODES.get(p[0]);
        if (type == null) {
            throw new IllegalArgumentException("unknown type " + p[0]);
        }
        return ItemFactory.create(type, p[1], p[2], p[3], Integer.parseInt(p[4]));
    }
}
