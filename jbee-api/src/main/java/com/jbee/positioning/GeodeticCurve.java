package com.jbee.positioning;

/**
 * This is the outcome of a geodetic calculation. It represents the path and
 * ellipsoidal distance between two GlobalCoordinates for a specified reference
 * ellipsoid.
 *
 * @author Mike Gavaghan
 * @author weinpau
 */
class GeodeticCurve {

    /**
     * Ellipsoidal distance (in meters).
     */
    private final double mEllipsoidalDistance;

    /**
     * Azimuth (degrees from north).
     */
    private final double mAzimuth;

    /**
     * Reverse azimuth (degrees from north).
     */
    private final double mReverseAzimuth;

    /**
     * Create a new GeodeticCurve.
     *
     * @param ellipsoidalDistance ellipsoidal distance in meters
     * @param azimuth azimuth in degrees
     * @param reverseAzimuth reverse azimuth in degrees
     */
    public GeodeticCurve(double ellipsoidalDistance, double azimuth, double reverseAzimuth) {
        mEllipsoidalDistance = ellipsoidalDistance;
        mAzimuth = azimuth;
        mReverseAzimuth = reverseAzimuth;
    }

    /**
     * Get the ellipsoidal distance.
     *
     * @return ellipsoidal distance in meters
     */
    public double getEllipsoidalDistance() {
        return mEllipsoidalDistance;
    }

    /**
     * Get the azimuth.
     *
     * @return azimuth in degrees
     */
    public double getAzimuth() {
        return mAzimuth;
    }

    /**
     * Get the reverse azimuth.
     *
     * @return reverse azimuth in degrees
     */
    public double getReverseAzimuth() {
        return mReverseAzimuth;
    }

}
