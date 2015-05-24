package com.jbee.device.ardrone2.internal.navdata.options;

import com.jbee.utils.Numbers;
import java.nio.ByteBuffer;

/**
 *
 * @author weinpau
 */
public class GPS implements Option {

    double latitude;
    double longitude;
    double elevation;
    double hdop;
    int dataAvailable;
    int zeroValidated;
    int wptValidated;
    double lat0;
    double lon0;
    double latFuse;
    double lonFuse;
    int gpsState;
    float xTraj;
    float xRef;
    float yTraj;
    float yRef;
    float thetaP;
    float phiP;
    float thetaI;
    float phiI;
    float thetaD;
    float phiD;
    double vdop;
    double pdop;
    float speed;
    int lastFrameTimestamp;
    float degree;
    float degreeMag;
    float ehpe;
    float ehve;
    float c_n0;
    int nbSatellites;
    SatChannel[] channels = new SatChannel[12];
    int gpsPlugged;
    int ephemerisStatus;
    float vxTraj;
    float vyTraj;
    int firmwareStatus;

    @Override
    public OptionId getId() {
        return OptionId.GPS;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getElevation() {
        return elevation;
    }

    public double getHdop() {
        return hdop;
    }

    public int getDataAvailable() {
        return dataAvailable;
    }

    public int getZeroValidated() {
        return zeroValidated;
    }

    public int getWptValidated() {
        return wptValidated;
    }

    public double getLat0() {
        return lat0;
    }

    public double getLon0() {
        return lon0;
    }

    public double getLatFuse() {
        return latFuse;
    }

    public double getLonFuse() {
        return lonFuse;
    }

    public int getGpsState() {
        return gpsState;
    }

    public float getxTraj() {
        return xTraj;
    }

    public float getxRef() {
        return xRef;
    }

    public float getyTraj() {
        return yTraj;
    }

    public float getyRef() {
        return yRef;
    }

    public float getThetaP() {
        return thetaP;
    }

    public float getPhiP() {
        return phiP;
    }

    public float getThetaI() {
        return thetaI;
    }

    public float getPhiI() {
        return phiI;
    }

    public float getThetaD() {
        return thetaD;
    }

    public float getPhiD() {
        return phiD;
    }

    public double getVdop() {
        return vdop;
    }

    public double getPdop() {
        return pdop;
    }

    public float getSpeed() {
        return speed;
    }

    public int getLastFrameTimestamp() {
        return lastFrameTimestamp;
    }

    public float getDegree() {
        return degree;
    }

    public float getDegreeMag() {
        return degreeMag;
    }

    public float getEhpe() {
        return ehpe;
    }

    public float getEhve() {
        return ehve;
    }

    public float getC_n0() {
        return c_n0;
    }

    public int getNbSatellites() {
        return nbSatellites;
    }

    public SatChannel[] getChannels() {
        return channels;
    }

    public int getGpsPlugged() {
        return gpsPlugged;
    }

    public int getEphemerisStatus() {
        return ephemerisStatus;
    }

    public float getVxTraj() {
        return vxTraj;
    }

    public float getVyTraj() {
        return vyTraj;
    }

    public int getFirmwareStatus() {
        return firmwareStatus;
    }

    @Override
    public void parse(ByteBuffer buffer) {

        latitude = buffer.getDouble();
        longitude = buffer.getDouble();
        elevation = buffer.getDouble();
        hdop = buffer.getDouble();
        dataAvailable = buffer.getInt();
        zeroValidated = buffer.getInt();
        wptValidated = buffer.getInt();
        lat0 = buffer.getDouble();
        lon0 = buffer.getDouble();
        latFuse = buffer.getDouble();
        lonFuse = buffer.getDouble();
        gpsState = buffer.getInt();
        xTraj = buffer.getFloat();
        xRef = buffer.getFloat();
        yTraj = buffer.getFloat();
        yRef = buffer.getFloat();
        thetaP = buffer.getFloat();
        phiP = buffer.getFloat();
        thetaI = buffer.getFloat();
        phiI = buffer.getFloat();
        thetaD = buffer.getFloat();
        phiD = buffer.getFloat();
        vdop = buffer.getDouble();
        pdop = buffer.getDouble();
        speed = buffer.getFloat();
        lastFrameTimestamp = buffer.getInt();
        degree = buffer.getFloat();
        degreeMag = buffer.getFloat();
        ehpe = buffer.getFloat();
        ehve = buffer.getFloat();
        c_n0 = buffer.getFloat();
        nbSatellites = buffer.getInt();

        for (int i = 0; i < channels.length; i++) {
            channels[i] = new SatChannel(buffer.get(), buffer.get());

        }
        gpsPlugged = buffer.getInt();
        ephemerisStatus = buffer.getInt();
        vxTraj = buffer.getFloat();
        vyTraj = buffer.getFloat();
        firmwareStatus = buffer.getInt();

    }

    @Override
    public String toString() {
         StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("GPS{").append("\n");
        stringBuilder.append("\t").append("latitude=").append(Numbers.format(latitude, 6)).append("\n");
        stringBuilder.append("\t").append("longitude=").append(Numbers.format(longitude, 6)).append("\n");
        stringBuilder.append("\t").append("elevation=").append(Numbers.format(elevation, 3)).append("\n");
        stringBuilder.append("\t").append("speed=").append(Numbers.format(speed, 3)).append("\n");
        stringBuilder.append("\t").append("nbSatellites=").append(nbSatellites).append("\n");

        stringBuilder.append("}");

        return stringBuilder.toString();
    }
    
    

    public static class SatChannel {

        private final byte sat;
        private final byte channel;

        public SatChannel(byte sat, byte channel) {
            this.sat = sat;
            this.channel = channel;
        }

        public byte getChannel() {
            return channel;
        }

        public byte getSat() {
            return sat;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 37 * hash + this.sat;
            hash = 37 * hash + this.channel;
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
            final SatChannel other = (SatChannel) obj;
            if (this.sat != other.sat) {
                return false;
            }
            return this.channel == other.channel;
        }

    }

}
