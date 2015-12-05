package Task2;

import java.util.ArrayList;

public class Business {
    private String name;
    private String business_id;
    private String latitude;
    private String longitude;
    private String city;
    private ArrayList<String> categories;

    Business() {
        categories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessId() {
        return business_id;
    }

    public void setBusinessId(String businessId) {
        this.business_id = businessId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<String> getCategory() {
        return categories;
    }

    public void setCategory(ArrayList<String> categories) {
        this.categories.addAll(categories);
    }


}
