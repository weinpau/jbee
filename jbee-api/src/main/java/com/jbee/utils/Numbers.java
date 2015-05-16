package com.jbee.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author weinpau
 */
public final class Numbers {

    public static String format(Number number, int scale) {

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(scale);
        nf.setMinimumFractionDigits(scale);
        nf.setGroupingUsed(false);
        return nf.format(number);
    }

}
