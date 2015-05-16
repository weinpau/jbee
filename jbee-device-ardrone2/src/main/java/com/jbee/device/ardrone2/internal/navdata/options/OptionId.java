package com.jbee.device.ardrone2.internal.navdata.options;

/**
 *
 * @author weinpau
 */
public enum OptionId {

    CHECKSUM(-1),
    DEMO(0, Demo.class),
    TIME(1),
    RAW_MEASURES(2),
    PHYS_MEASURES(3),
    GYROS_OFFSETS(4),
    EULER_ANGLES(5),
    REFERENCES(6),
    TRIMS(7),
    RC_REFERENCES(8),
    PWM(9),
    ALTITUDE(10),
    VISION_RAW(11),
    VISION_OF(12),
    VISION(13),
    VISION_PERF(14),
    TRACKERS_SEND(15),
    VISION_DETECT(16),
    WATCHDOG(17),
    ADC_DATA_FRAME(18),
    VIDEO_STREAM(19),
    GAMES(20),
    PRESSURE_RAW(21),
    MAGNETO(22),
    WIND_SPEED(23),
    KALMAN_PRESSURE(24),
    HDVIDEO_STREAM(25),
    WIFI(26),
    GPS(27);

    private final int id;
    private final Class<? extends Option> optionClass;

    private OptionId(int id) {
        this(id, null);
    }

    private OptionId(int id, Class<? extends Option> optionClass) {
        this.id = id;
        this.optionClass = optionClass;
    }

    public int getId() {
        return id;
    }

    public int mask() {
        return 1 << id;
    }

    public Option createOption() {
        if (optionClass != null) {
            try {
                return optionClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
            }
        }
        return null;
    }

    public static OptionId getById(int id) {
        for (OptionId option : values()) {
            if (option.id == id) {
                return option;
            }
        }
        return null;
    }

    public static int mask(OptionId... options) {
        int mask = 0;
        for (OptionId option : options) {
            mask = mask | option.mask();
        }
        return mask;
    }

}
