package amr.com.weedingplanner.Objects;

import android.os.Parcel;
import android.os.Parcelable;


public class product implements Parcelable {
    private String Name;
    private String Description;
    private String ImgUrl;
    private double Price;
    private String SellerId;
    private String ShopName;
    private int ProductRent;


    public product(String name, String description, String imgUrl, double price, String sellerId, String shopName, int ProductRent) {
        Name = name;
        Description = description;
        ImgUrl = imgUrl;
        Price = price;
        SellerId = sellerId;
        ShopName = shopName;
        this.ProductRent = ProductRent;

    }

    public product(String name, String description, String imgUrl, double price, String sellerId) {
        Name = name;
        Description = description;
        ImgUrl = imgUrl;
        Price = price;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public product(String description, String imgUrl, double price,int ProductRent) {
        Description = description;
        ImgUrl = imgUrl;
        Price = price;
        this.ProductRent = ProductRent;

    }


    public static final Creator<product> CREATOR = new Creator<product>() {
        @Override
        public product createFromParcel(Parcel in) {
            return new product(in);
        }

        @Override
        public product[] newArray(int size) {
            return new product[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getSellerId() {
        return SellerId;
    }

    public void setSellerId(String sellerId) {
        SellerId = sellerId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Name);
        parcel.writeString(Description);
        parcel.writeString(ImgUrl);
        parcel.writeDouble(Price);
        parcel.writeString(SellerId);
        parcel.writeString(ShopName);
        parcel.writeInt(ProductRent);
    }


    private product(Parcel parcel) {
        Name = parcel.readString();
        Description = parcel.readString();
        ImgUrl = parcel.readString();
        Price = parcel.readDouble();
        SellerId = parcel.readString();
        ShopName = parcel.readString();
        ProductRent = parcel.readInt();
    }


    public int getProductRent() {
        return ProductRent;
    }

    public void setProductRent(int productRent) {
        ProductRent = productRent;
    }


}
