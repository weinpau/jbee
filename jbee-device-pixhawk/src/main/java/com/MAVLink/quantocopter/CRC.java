        package com.MAVLink.quantocopter;
        
        /**
        * X.25 CRC calculation for MAVlink messages. The checksum must be initialized,
        * updated with witch field of the message, and then finished with the message
        * id.
        *
        */
        public class CRC {
        private final int[] MAVLINK_MESSAGE_CRCS = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 51, 99, 61, 252, 123, 202, 225, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int CRC_INIT_VALUE = 0xffff;
        private int CRCvalue;
        
        /**
        * Accumulate the X.25 CRC by adding one char at a time.
        *
        * The checksum function adds the hash of one char at a time to the 16 bit
        * checksum (uint16_t).
        *
        * @param data
        *            new char to hash
        * @param crcAccum
        *            the already accumulated checksum
        **/
        public  void update_checksum(int data) {
		int tmp;
		data= data & 0xff;	//cast because we want an unsigned type
		tmp = data ^ (CRCvalue & 0xff);
		tmp ^= (tmp << 4) & 0xff;
		CRCvalue = ((CRCvalue >> 8) & 0xff) ^ (tmp << 8) ^ (tmp << 3)
        ^ ((tmp >> 4) & 0xf);
        }
        
        /**
        * Finish the CRC calculation of a message, by running the CRC with the
        * Magic Byte. This Magic byte has been defined in MAVlink v1.0.
        *
        * @param msgid
        *            The message id number
        */
        public  void finish_checksum(int msgid) {
		update_checksum(MAVLINK_MESSAGE_CRCS[msgid]);
        }
        
        /**
        * Initialize the buffer for the X.25 CRC
        *
        */
        public void start_checksum() {
		CRCvalue = CRC_INIT_VALUE;
        }
        
        public int getMSB() {
		return ((CRCvalue >> 8) & 0xff);
        }
        
        public int getLSB() {
		return (CRCvalue & 0xff);
        }
        
        public CRC() {
		start_checksum();
        }
        
        }
        