package pl.hackyeah.colorando.repository.dto;

import android.support.annotation.NonNull;

import java.util.Objects;

import static java.lang.String.format;

public class Location {

    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        super();

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.latitude, latitude) == 0 &&
                Double.compare(location.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @NonNull
    @Override
    public String toString() {
        return format("Location{latitude=%s, longitude=%s}", latitude, longitude);
    }
}
