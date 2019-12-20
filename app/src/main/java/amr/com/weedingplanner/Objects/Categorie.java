package amr.com.weedingplanner.Objects;

public class Categorie {
    String CategorieName;
    String CategorieDatabaseName;

    public Categorie(String categorieName, String categorieDatabaseName) {
        CategorieName = categorieName;
        CategorieDatabaseName = categorieDatabaseName;
    }

    public String getCategorieName() {
        return CategorieName;
    }

    public void setCategorieName(String categorieName) {
        CategorieName = categorieName;
    }

    public String getCategorieDatabaseName() {
        return CategorieDatabaseName;
    }

    public void setCategorieDatabaseName(String categorieDatabaseName) {
        CategorieDatabaseName = categorieDatabaseName;
    }
}
