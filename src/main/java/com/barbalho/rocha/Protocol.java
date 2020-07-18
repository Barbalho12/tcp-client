package com.barbalho.rocha;

import java.util.Arrays;

public class Protocol {
    public static final int INIT = 0;
	public static final int BYTES = 1;
	public static final int FRAME = 2;
	public static final int START_DATA = 3;

	public static final byte INIT_VALUE = 0x0A;
	public static final byte END_VALUE = 0x0D;

	public static final byte TEXT_FRAME = (byte) 0xA1;
	public static final byte USER_FRAME = (byte) 0xA2;
	public static final byte TIME_FRAME = (byte) 0xA3;
    public static final byte ACK_FRAME = (byte) 0xA0;
    
	public static final byte [] ACK = {INIT_VALUE, 0x05, ACK_FRAME, 0x28, END_VALUE};
	
	public static byte[] createMessage(final byte[] textMessage, final byte frame) {

		final byte[] byteMessage = new byte[textMessage.length + 5];

		byteMessage[Protocol.INIT] = Protocol.INIT_VALUE;
		byteMessage[Protocol.BYTES] = (byte) byteMessage.length;
		byteMessage[Protocol.FRAME] = frame;

		int index = Protocol.START_DATA;

		for (int i = 0; i < textMessage.length; i++) {
			byteMessage[index++] = (byte) textMessage[i];
		}

		final byte[] subMessage = Arrays.copyOfRange(byteMessage, 1, index);

		byteMessage[index++] = CRC8.calc(subMessage, subMessage.length);
		byteMessage[index++] = Protocol.END_VALUE;

		return byteMessage;
	}
    
}