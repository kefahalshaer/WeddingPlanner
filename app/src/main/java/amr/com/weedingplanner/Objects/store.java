package amr.com.weedingplanner.Objects;

public class store {
    private String StoreName;
    private String SellerId;
    private String Category;

    public store(String storeName, String sellerId, String category) {
        StoreName = storeName;
        SellerId = sellerId;
        Category = category;
    }

    public store(String storeName, String category) {
        StoreName = storeName;
        Category = category;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getSellerId() {
        return SellerId;
    }

    public void setSellerId(String sellerId) {
        SellerId = sellerId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
