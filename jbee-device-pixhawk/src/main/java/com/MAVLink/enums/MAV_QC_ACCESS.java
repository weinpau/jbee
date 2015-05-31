            /** ACCESS Class for registration of Mavlink Module.
            */
            package com.MAVLink.enums;
            
            public class MAV_QC_ACCESS {
            	public static final int MAV_QC_ACCESS_NONE = 0; /* NON Access, all mavlink messages invalid | */
            	public static final int MAV_QC_ACCESS_BASIC = 1; /* Allow sending basic messages like heartbeat | */
            	public static final int MAV_QC_ACCESS_START_LAND = 2; /* Allow Start/Land operations | */
            	public static final int MAV_QC_ACCESS_SET_MODE = 4; /* Allow changeing Flight-Mode | */
            	public static final int MAV_QC_ACCESS_LOITER = 8; /* Allow Operations in LOITER Mode | */
            	public static final int MAV_QC_ACCESS_AUTO = 16; /* Allow Operations in AUTO Mode | */
            	public static final int MAV_QC_ACCESS_STABELIZE = 32; /* Allow Operations in STABELIZE Mode | */
            	public static final int MAV_QC_ACCESS_RAW_CONTROL = 64; /* Allow set of THODDLE/ROLL/PITCH/YAW Controller Values | */
            	public static final int MAV_QC_ACCESS_MISSION = 128; /* Allow set of Mission messages | */
            	public static final int MAV_QC_ACCESS_GUIDED = 256; /* Allow Operations in GUIDED Mode | */
            	public static final int MAV_QC_ACCESS_DEBUG = 512; /* Allow sending debug messages | */
            	public static final int MAV_QC_ACCESS_COMMAND = 1024; /* Allow sending COmmand_INT and Command_Long Messages | */
            	public static final int MAV_QC_ACCESS_ALL = 65535; /* ALL Access, all mavlink messages valid | */
            	public static final int MAV_QC_ACCESS_ENUM_END = 65536; /*  | */
            
            }
            