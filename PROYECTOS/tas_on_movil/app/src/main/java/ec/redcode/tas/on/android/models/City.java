package ec.redcode.tas.on.android.models;

import java.net.URL;

/**
 * Created by User on 17/11/2017.
 * Modelo de la ciudad
 */

public class City {

    private String name;
    private String code;
    private boolean isInteresting;
    private String provincia;
    private String region;
    private URL flagUrl;
    private URL provinciaImage;
    private boolean isLoading;

    public City() {
    }

    public City(String name, String code, boolean isInteresting, String provincia, String region, URL flagUrl, URL provinciaImage) {
        this.name = name;
        this.code = code;
        this.isInteresting = isInteresting;
        this.provincia = provincia;
        this.region = region;
        this.flagUrl = flagUrl;
        this.provinciaImage = provinciaImage;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public URL getImageUrl() {
        return flagUrl;
    }

    public void setImageUrl(URL imageUrl) {
        this.flagUrl = imageUrl;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public URL getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(URL flagUrl) {
        this.flagUrl = flagUrl;
    }

    public URL getProvinciaImage() {
        return provinciaImage;
    }

    public void setProvinciaImage(URL provinciaImage) {
        this.provinciaImage = provinciaImage;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isInteresting() {
        return isInteresting;
    }

    public void setInteresting(boolean interesting) {
        isInteresting = interesting;
    }
}
