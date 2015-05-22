            /** Register Result Class for feadback after registration of Mavlink Module.
            */
            package com.MAVLink.enums;
            
            public class MAV_QC_REGISTER_RESULT {
            	public static final int MAV_QC_REGISTER_RESULT_REGISTER_OK = 0; /* Registration successful | */
            	public static final int MAV_QC_REGISTER_RESULT_UNREGISTER_OK = 1; /* Registration successful | */
            	public static final int MAV_QC_REGISTER_RESULT_FAILD_EXISTING = 2; /* Registration failed because the Module System or Componentn ID already registerd | */
            	public static final int MAV_QC_REGISTER_RESULT_FAILD_MODULE_NOT_EXISTS = 3; /* Registration Failed | */
            	public static final int MAV_QC_REGISTER_RESULT_FAILD_OTHER = 4; /* Registration Failed | */
            	public static final int MAV_QC_REGISTER_RESULT_ENUM_END = 5; /*  | */
            
            }
            