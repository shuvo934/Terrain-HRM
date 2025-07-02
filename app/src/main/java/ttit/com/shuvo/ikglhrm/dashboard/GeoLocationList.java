package ttit.com.shuvo.ikglhrm.dashboard;

public class GeoLocationList {
    private String geo_id;
    private String geo_lat;
    private String geo_lng;
    private String geo_radius;
    private String coa_id;

    public GeoLocationList(String geo_id, String geo_lat, String geo_lng, String geo_radius, String coa_id) {
        this.geo_id = geo_id;
        this.geo_lat = geo_lat;
        this.geo_lng = geo_lng;
        this.geo_radius = geo_radius;
        this.coa_id = coa_id;
    }

    public String getCoa_id() {
        return coa_id;
    }

    public void setCoa_id(String coa_id) {
        this.coa_id = coa_id;
    }

    public String getGeo_id() {
        return geo_id;
    }

    public void setGeo_id(String geo_id) {
        this.geo_id = geo_id;
    }

    public String getGeo_lat() {
        return geo_lat;
    }

    public void setGeo_lat(String geo_lat) {
        this.geo_lat = geo_lat;
    }

    public String getGeo_lng() {
        return geo_lng;
    }

    public void setGeo_lng(String geo_lng) {
        this.geo_lng = geo_lng;
    }

    public String getGeo_radius() {
        return geo_radius;
    }

    public void setGeo_radius(String geo_radius) {
        this.geo_radius = geo_radius;
    }
}
