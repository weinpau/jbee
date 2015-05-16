package com.jbee.device.ardrone2.internal.navdata;

import com.jbee.device.ardrone2.internal.navdata.options.Option;
import com.jbee.device.ardrone2.internal.navdata.options.OptionId;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author weinpau
 */
public class NavDataParser {

    public NavData parse(ByteBuffer buffer) throws NavDataParseException {
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int expectedChecksum = checksum(buffer);
        int header = buffer.getInt();
        if (header != 0x55667788 && header != 0x55667789) {
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

            int nextPosition = buffer.position() + length - 4;
            if (OptionId.CHECKSUM == id) {

                int checksum = buffer.getInt();
                if (expectedChecksum != checksum) {
                    throw new NavDataParseException("Invalid checksum, expected: " + expectedChecksum + ", got: " + checksum);
                }
                checked = true;
                break;
            }
            Option option = createOption(id);
            if (option != null) {
                option.parse(buffer);
                data.options.put(option.getClass(), option);
            }
            buffer.position(nextPosition);

        }

        if (!checked) {
            throw new NavDataParseException("Checksum is missing, expected: " + expectedChecksum);
        }

        return data;
    }

    Option createOption(OptionId id) {
        if (id == null) {
            return null;
        }
        return id.createOption();
    }

    int checksum(ByteBuffer buffer) {
        int checksum = 0;
        while (buffer.remaining() > 8) {
            byte b = buffer.get();
            checksum += b & 0xFF;
        }
        buffer.position(0);
        return checksum;
    }

}
