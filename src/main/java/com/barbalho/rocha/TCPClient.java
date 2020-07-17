package com.barbalho.rocha;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.apache.mina.api.IoFuture;
import org.apache.mina.api.IoSession;
import org.apache.mina.transport.nio.NioTcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPClient {
	
	static final private Logger LOG = LoggerFactory.getLogger(TCPClient.class);

	public static final String IP_SERVER = "localhost";
	public static final int PORT_SERVER = 9999;

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
		byte[] array = { 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64 };
		String s = "Hello World";
		System.out.println(hexToASCII(array));
		System.out.println(hexToASCII(ASCIIToHex(s)));
	}

	public static byte[] createMessage(byte[] textMessage, byte frame) {

		byte[] byteMessage = new byte[textMessage.length + 5];

		byteMessage[Protocol.INIT] = Protocol.INIT_VALUE;
		byteMessage[Protocol.BYTES] = (byte) byteMessage.length;
		byteMessage[Protocol.FRAME] = frame;

		int index = Protocol.START_DATA;

		for (int i = 0; i < textMessage.length; i++) {
			byteMessage[index++] = (byte) textMessage[i];
		}

		byte[] subMessage = Arrays.copyOfRange(byteMessage, 3, index);

		byteMessage[index++] = CRC8.calc(subMessage, subMessage.length);
		byteMessage[index++] = Protocol.END_VALUE;

		return byteMessage;
	}

	public static void tcpSend(byte[] message) {

		final NioTcpClient client = new NioTcpClient();
		client.setIoHandler(new ClientHandler());
	

		try {

			IoFuture<IoSession> future = client.connect(new InetSocketAddress(IP_SERVER, PORT_SERVER));
			client.setConnectTimeoutMillis(3000);
			try {
				IoSession session = future.get();
				LOG.info("session connected : {" + session + "}");
				ByteBuffer encode = ByteBuffer.wrap(message);
				session.write(encode);
				Thread.sleep(1000);
			} catch (ExecutionException e) {
				LOG.error("cannot connect : ", e);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void sendTextMessage(String message) {
		tcpSend(createMessage(message.getBytes(), Protocol.TEXT_FRAME));
	}

	public static void sendUserMessage(User user) {
		tcpSend(createMessage(user.getBytes(), Protocol.USER_FRAME));
	}

	public static void main(String[] args) {
		// sendTextMessage("cabeça de dragão");

		User user = new User(14, 59, 165, "Marcos");
		sendUserMessage(user);

	}
}
