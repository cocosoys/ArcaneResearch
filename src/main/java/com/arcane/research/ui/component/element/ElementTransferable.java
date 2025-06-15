package com.arcane.research.ui.component.element;

import com.arcane.research.enums.Element;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ElementTransferable implements Transferable {
    public static final DataFlavor ELEMENT_FLAVOR = new DataFlavor(Element.class, "Element Flavor");
    private final Element element;

    public ElementTransferable(Element element) {
        this.element = element;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{ELEMENT_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(ELEMENT_FLAVOR);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return element;
        }
        throw new UnsupportedFlavorException(flavor);
    }
}