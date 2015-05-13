package com.jbee.positioning;

import com.jbee.units.Distance;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author weinpau
 */
public class LatLon {

    private final double latitude;
    private final double longitude;

    public LatLon(double latitude, double longitude) {
        if (!Double.isFinite(latitude)) {
            throw new IllegalArgumentException("The latitude must be a finite number.");
        }
        if (!Double.isFinite(longitude)) {
            throw new IllegalArgumentException("The longitude must be a finite number.");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Distance distance(LatLon coordinate) {
        return distance(coordinate, Ellipsoid.WGS84);
    }

    public Distance distance(LatLon coordinate, Ellipsoid ellipsoid) {
        double ellipsoidalDistance = GeodeticCalculator.calculateGeodeticCurve(ellipsoid, this, coordinate).getEllipsoidalDistance();
        return Distance.ofMeters(ellipsoidalDistance);
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(6);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(nf.format(Math.abs(latitude)));
        stringBuilder.append((latitude >= 0) ? "N" : "S");
        stringBuilder.append(" ");
        stringBuilder.append(nf.format(Math.abs(longitude)));
        stringBuilder.append((longitude >= 0) ? "E" : "W");

        return stringBuilder.toString();

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LatLon other = (LatLon) obj;
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
            return false;
        }
        return Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(other.longitude);
    }

}
