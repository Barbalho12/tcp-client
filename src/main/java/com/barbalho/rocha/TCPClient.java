package com.barbalho.rocha;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPClient {
	static final private Logger LOG = LoggerFactory.getLogger(TCPClient.class);

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

	public void testConversion() {
		byte[] array = { 0x09, 0x01, 0x31, 0x32, 0x33, 0x34 };
		System.out.println(String.format("0x%02X", CRC8.calc(array, 6)));
	}
	
	public static String hexToASCII(byte[] array) {
		return new String(array);
	}
	
	public static byte[] ASCIIToHex(String ascii) {
		return ascii.getBytes();
	}

	public static void teste() {
		byte[] array = { 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64};
		String s = "Hello World";
		System.out.println(hexToASCII(array));
		System.out.println(hexToASCII(ASCIIToHex(s)));
	}

	public byte[] createMessage(String textMessage, byte frame) {

		byte[] byteMessage = new byte[textMessage.length() + 5];

		byteMessage[INIT] = INIT_VALUE;
		byteMessage[BYTES] = (byte) byteMessage.length;
		byteMessage[FRAME] = frame;

		int index = START_DATA;

		for (int i = 0; i < textMessage.length(); i++) {
			byteMessage[index++] = (byte) textMessage.charAt(i);
		}

		byte[] subMessage = Arrays.copyOfRange(byteMessage, 3, index);

		byteMessage[index++] = CRC8.calc(subMessage, byteMessage.length);
		byteMessage[index++] = END_VALUE;

		return byteMessage;
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		
		teste();

//		LOG.info("starting echo client");
//		final NioTcpClient client = new NioTcpClient();
//		client.setIoHandler(new ClientHandler());
//
//		try {
//
//			IoFuture<IoSession> future = client.connect(new InetSocketAddress("localhost", 9999));
//
//			try {
//				IoSession session = future.get();
//				LOG.info("session connected : {" + session + "}");
//
//				HashMap<String, String> m = new HashMap<String, String>();
//				m.put("1", "1");
//
//				// encode
//				JavaNativeMessageEncoder<HashMap> in = new JavaNativeMessageEncoder<HashMap>();
//				ByteBuffer encode = in.encode(m);
//
//				session.write(encode);
//
//			} catch (ExecutionException e) {
//				LOG.error("cannot connect : ", e);
//			}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
}
