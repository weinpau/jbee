package com.jbee.positioning;

/**
 * This class is a converter of GPS coordinates in UTM coordinates.
 *
 * @author weinpau
 */
class CoordinateConverter {

    private CoordinateConverter() {
    }

    public static LatLon utm2geo(Position position, Ellipsoid ellipsoid, int zone, Hemisphere hemisphere) {

        double a = ellipsoid.getSemiMajorAxis();
        double b = a * (1 - ellipsoid.getFlattening());
        double se2 = ((Math.pow(a, 2)) - (Math.pow(b, 2))) / ((Math.pow(b, 2)));
        double c = Math.pow(a, 2) / b;

        double x = position.getX();
        double y = position.getY();

        /* Delete the offset in the Xs*/
        x = x - 500000;
        /* Delete the offset of the coordenate Y for the south hemisfery */
        if (hemisphere == Hemisphere.SOUTH) {
            y = y - 10000000.0;
        }
        /* average longitude of the zone */
        double lonmedia = (zone * 6.0) - 183.0;
        /* Operations */
        double fip = (y / (6366197.724 * 0.9996));
        double v = (c * 0.9996) / Math.sqrt((1 + se2 * Math.pow(Math.cos(fip), 2)));
        double aa = (x / v);
        double A1 = Math.sin(2 * fip);
        double A2 = A1 * Math.pow(Math.cos(fip), 2);
        double J2 = fip + (A1 / 2);
        double J4 = (3 * J2 + A2) / 4;
        double J6 = (5 * J4 + A2 * Math.pow(Math.cos(fip), 2)) / 3.0;
        double alfa = (3.0 / 4) * se2;
        double beta = (5.0 / 3) * Math.pow(alfa, 2);
        double gamma = (35.0 / 27) * Math.pow(alfa, 3);
        double B = 0.9996 * c * (fip - (alfa * J2) + (beta * J4) - (gamma * J6));
        double bb = (y - B) / v;
        double S = (se2 * Math.pow(aa, 2) * Math.pow(Math.cos(fip), 2)) / 2;
        double CC = aa * (1 - (S / 3.0));
        double n = (bb * (1.0 - S)) + fip;
        double sinh = (Math.pow(Math.E, CC) - Math.pow(Math.E, -(CC))) / 2.0;
        double ilam = Math.atan(sinh / Math.cos(n));
        double tau = Math.atan(Math.cos(ilam) * Math.tan(n));
        double gilam = (ilam * 180) / Math.PI;
        double lon = gilam + lonmedia;
        double lat = fip + (1 + se2 * (Math.pow(Math.cos(fip), 2)) - (3.0 / 2) * se2 * Math.sin(fip) * Math.cos(fip) * (tau - fip)) * (tau - fip);
        double glat = (lat * 180) / Math.PI;

        return new LatLon(glat, lon);

    }

    public static Position geo2utm(LatLon geo, Ellipsoid ellipsoid) {
        /* Elipsoide */
        double a = ellipsoid.getSemiMajorAxis();
        double b = a * (1 - ellipsoid.getFlattening());
        double se2 = ((Math.pow(a, 2)) - (Math.pow(b, 2))) / ((Math.pow(b, 2)));
        double c = Math.pow(a, 2) / b;

        double lat = geo.getLatitude();
        double lon = geo.getLongitude();

        /* Correccion de lon y lat por error */
        if (lon > 180) {
            lon = 180;
        }
        if (lon < -180) {
            lon = -180;
        }
        if (lat > 90) {
            lat = 90;
        }
        if (lat < -90) {
            lat = -90;
        }
        /* Transform to radians */
        double rlon = (lon * Math.PI) / 180.0;
        double rlat = (lat * Math.PI) / 180.0;
        /* Calculate the zone */
        double hus = (lon / 6.0) + 31;
        double huso = Math.floor(hus);
        /* average longitude of the zone */
        double lonmedia = (huso * 6.0) - 183;
        /* Angular distance between the point and the central meridian of the zone */
        double rilon = rlon - ((lonmedia * Math.PI) / 180.0);
        /*  Operations  */
        double A = (Math.cos(rlat)) * (Math.sin(rilon));
        double CC = (0.5) * (Math.log((1 + A) / (1 - A)));
        double n = (Math.atan((Math.tan(rlat)) / (Math.cos(rilon)))) - rlat;
        double v = (c * 0.9996) / Math.sqrt((1 + se2 * Math.pow(Math.cos(rlat), 2)));
        double S = (se2 * Math.pow(CC, 2) * Math.pow(Math.cos(rlat), 2)) / 2.0;
        double A1 = Math.sin(2 * rlat);
        double A2 = A1 * Math.pow(Math.cos(rlat), 2);
        double J2 = rlat + (A1 / 2);
        double J4 = (3 * J2 + A2) / 4;
        double J6 = (5 * J4 + A2 * Math.pow(Math.cos(rlat), 2)) / 3;
        double alfa = (3.0 / 4) * se2;
        double beta = (5.0 / 3) * Math.pow(alfa, 2);
        double gamma = (35.0 / 27) * Math.pow(alfa, 3);
        double B = 0.9996 * c * (rlat - (alfa * J2) + (beta * J4) - (gamma * J6));
        double x = CC * v * (1 + (S / 3)) + 500000;
        double y = n * v * (1 + S) + B;
        /* For latitudes in the south hemisfery */
        if (lat < 0) {
            y = y + 10000000;
        }
        return new Position(x, y);
    }

}
