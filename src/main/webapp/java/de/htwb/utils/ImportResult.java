package de.htwb.utils;

import de.htwb.model.imported.ImportedShape;

import java.util.ArrayList;

public class ImportResult {

    private String key;
    private ArrayList<ImportedShape> importedShapes;

    public ImportResult(String key, ArrayList<ImportedShape> importedShapes)
    {
        this.key = key;
        this.importedShapes = importedShapes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<ImportedShape> getImportedShapes() {
        return importedShapes;
    }

    public void setImportedShapes(ArrayList<ImportedShape> importedShapes) {
        this.importedShapes = importedShapes;
    }
}
