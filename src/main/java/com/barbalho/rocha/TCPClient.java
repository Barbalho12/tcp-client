package com.barbalho.rocha;

import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.apache.mina.api.IoFuture;
import org.apache.mina.api.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.nio.NioTcpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPClient {

	static final private Logger LOG = LoggerFactory.getLogger(TCPClient.class);

	public static String IP_SERVER = "localhost";
	public static int PORT_SERVER = 9999;

	public void testConversion() {
		final byte[] array = { 0x09, 0x01, 0x31, 0x32, 0x33, 0x34 };
		System.out.println(String.format("0x%02X", CRC8.calc(array, 6)));
	}

	public static String hexToASCII(final byte[] array) {
		return new String(array);
	}

	public static byte[] ASCIIToHex(final String ascii) {
		return ascii.getBytes();
	}

	public static void teste() {
		final byte[] array = { 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64 };
		final String s = "Hello World";
		System.out.println(hexToASCII(array));
		System.out.println(hexToASCII(ASCIIToHex(s)));
	}

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

	public static NioTcpClient tcpSend(final byte[] message) {

		final NioTcpClient client = new NioTcpClient();
		client.setIoHandler(new ClientHandler());
		try {
			final IoFuture<IoSession> future = client.connect(new InetSocketAddress(IP_SERVER, PORT_SERVER));
			try {
				final IoSession session = future.get();
				final ByteBuffer encode = ByteBuffer.wrap(message);
				session.write(encode);
			} catch (final ExecutionException e) {
				LOG.error("cannot connect : ", e);
			}
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		return client;
	}

	public static NioTcpClient sendTextMessage(final String message) {
		return tcpSend(createMessage(message.getBytes(), Protocol.TEXT_FRAME));
	}

	public static NioTcpClient sendUserMessage(final User user) {
		return tcpSend(createMessage(user.getBytes(), Protocol.USER_FRAME));
	}

	public static NioTcpClient requestDatetime(final String fuse) {
		return tcpSend(createMessage(fuse.getBytes(), Protocol.TIME_FRAME));
	}

	public static void main(final String[] args) {

		try {

			if (args.length > 0) {
				IP_SERVER = args[0];
				if (args.length > 1) {
					PORT_SERVER = Integer.valueOf(args[1]);
					if (args.length > 2) {
						String frame = args[2];
						if (frame.equals("-cliente")) {
							if (args.length > 3) {
								String filePath = args[3];
								final User user = getFromJson(filePath);
								sendUserMessage(user);
								Thread.sleep(2000);
							} else {
								requestDatetime("America/Sao_Paulo");
								Thread.sleep(2000);
							}
						} else if (frame.equals("-texto")) {
							if (args.length > 3) {
								String texto = args[3];
								sendTextMessage(texto);
								Thread.sleep(2000);
							} else {
								requestDatetime("America/Sao_Paulo");
								Thread.sleep(2000);
							}
						} else if (frame.equals("-datetime")) {
							if (args.length > 3) {
								String zone = args[3];
								requestDatetime(zone);
								Thread.sleep(2000);
							} else {
								requestDatetime("America/Sao_Paulo");
								Thread.sleep(2000);
							}
						} else {
							throw new Exception("Caminho inválido");
						}
					}else{

					}
				}
			}

		} catch (Exception exception) {
			LOG.error("Erro na execução do cliente", exception);
		}

	}

	public static User getFromJson(String file) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(file)) {
			JSONObject obj = (JSONObject) jsonParser.parse(reader);
			
			int idade = Integer.valueOf(obj.get("idade").toString());
			int peso = Integer.valueOf(obj.get("peso").toString());
			int altura = Integer.valueOf( obj.get("altura").toString());
			String nome = obj.get("nome").toString();

			User user = new User(idade, peso, altura, nome);
			 
            return user;
 
        }
	}
}
