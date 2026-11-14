package library.structural.adapter;

import library.model.LibraryItem;

import java.util.List;

/** Target interface of the ADAPTER pattern - what our system understands. */
public interface CatalogProvider {
    List<LibraryItem> fetchItems();
}
