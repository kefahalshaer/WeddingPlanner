package amr.com.weedingplanner.Objects;

public class rent {

    private String ProductName;
    private String ProductCategory;
    private String Status;
    private String Date;
    private String UserId;
    private String UserName;


    public rent(String UserId, String status, String date, String ProductName,String UserName,String ProductCategory) {
        this.UserId = UserId;
        Status = status;
        Date = date;
        this.ProductName = ProductName;
        this.UserName = UserName;
        this.ProductCategory = ProductCategory;
    }

    public rent() {
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String name) {
        ProductName = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
