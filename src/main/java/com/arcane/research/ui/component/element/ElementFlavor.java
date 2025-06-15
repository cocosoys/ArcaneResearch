package com.arcane.research.ui.component.element;

import com.arcane.research.enums.Element;

import java.awt.datatransfer.DataFlavor;

public class ElementFlavor extends DataFlavor {
    public static final DataFlavor ELEMENT_FLAVOR = new DataFlavor(
            Element.class, "Element Flavor");
}