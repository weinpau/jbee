package com.jbee.device.ardrone2.navdata;

import com.jbee.device.ardrone2.navdata.options.Option;
import com.jbee.device.ardrone2.navdata.options.OptionId;
import java.nio.ByteBuffer;

/**
 *
 * @author weinpau
 */
public class NavDataParser {

    public NavData parse(ByteBuffer buffer) throws NavDataParseException {
        int expectedChecksum = checksum(buffer);

        int header = buffer.getInt();
        if (header != 0x55667788) {
            throw new NavDataParseException("Invalid header: " + Integer.toHexString(header));
        }

        NavData data = new NavData();
        data.state = new State(buffer.getInt());
        data.sequenceNumber = buffer.getInt();
        data.visionFlag = buffer.getInt();

        boolean checked = false;

        while (buffer.remaining() > 0) {

            OptionId id = OptionId.getById(buffer.getShort());
            short length = buffer.getShort();

            if (OptionId.CHECKSUM == id) {

                int checksum = buffer.getInt();
                if (expectedChecksum != checksum) {
                    throw new NavDataParseException("Invalid checksum, expected: " + expectedChecksum + ", got: " + checksum);
                }
                checked = true;
                break;
            }

            Option option = id.createOption();
            if (option == null) {
                buffer.position(buffer.position() + length - 4);
                continue;
            }
            option.parse(buffer);
            data.options.put(option.getClass(), option);
        }

        if (!checked) {
            throw new NavDataParseException("Checksum is missing, expected: " + expectedChecksum);
        }

        return data;
    }

    int checksum(ByteBuffer buffer) {
        int checksum = 0;
        for (byte b : buffer.array()) {
            checksum += b;
        }
        return checksum;
    }

}
