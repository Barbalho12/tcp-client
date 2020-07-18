package com.barbalho.rocha;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import org.apache.mina.api.IoFuture;
import org.apache.mina.api.IoSession;
import org.apache.mina.transport.nio.NioTcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPClient {

	static final private Logger LOG = LoggerFactory.getLogger(TCPClient.class);

	public static String IP_SERVER = "localhost";
	public static int PORT_SERVER = 9999;

	public static NioTcpClient tcpSend(final byte[] message) {

		final NioTcpClient client = new NioTcpClient();
		client.setIoHandler(new ClientHandler());
		client.setConnectTimeoutMillis(3000);
		try {
			final IoFuture<IoSession> future = client.connect(new InetSocketAddress(IP_SERVER, PORT_SERVER));
			try {
				final IoSession session = future.get();
				final ByteBuffer encode = ByteBuffer.wrap(message);
				
				FrameMessage frameMessage = new FrameMessage(message);
				LogFile.log("client send: [ " + frameMessage.toString() + " ]");
				
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
		return tcpSend(Protocol.createMessage(message.getBytes(), Protocol.TEXT_FRAME));
	}

	public static NioTcpClient sendUserMessage(final User user) {
		return tcpSend(Protocol.createMessage(user.getBytes(), Protocol.USER_FRAME));
	}

	public static NioTcpClient requestDatetime(final String fuse) {
		return tcpSend(Protocol.createMessage(fuse.getBytes(), Protocol.TIME_FRAME));
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
								final User user = Utils.readUserFromJson(filePath);
								sendUserMessage(user);
							}

						} else if (frame.equals("-texto")) {

							if (args.length > 3) {
								String texto = args[3];
								sendTextMessage(texto);
							} 

						} else if (frame.equals("-datetime")) {

							if (args.length > 3) {
								String zone = args[3];
								requestDatetime(zone);
								Utils.LOCAL_DATE_ZONE = zone;
							} else {
								requestDatetime(Utils.LOCAL_DATE_ZONE);
							}

						} else {
							throw new Exception("Caminho inválido");
						}
					}
				}
			}
		} catch (Exception exception) {
			LOG.error("Erro na execução do cliente", exception);
		}
	}
}
