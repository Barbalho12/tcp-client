package com.barbalho.rocha;

import java.nio.ByteBuffer;
import java.util.Date;

import org.apache.mina.api.IdleStatus;
import org.apache.mina.api.IoHandler;
import org.apache.mina.api.IoService;
import org.apache.mina.api.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler implements IoHandler {

	static final private Logger LOG = LoggerFactory.getLogger(ClientHandler.class);

	@Override
	public void sessionOpened(IoSession session) {
		LOG.info("session opened {" + session + "}");
	}

	@Override
	public void sessionClosed(IoSession session) {
		LOG.info("client :" + session.getRemoteAddress().toString() + " close connection");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		// TODO Auto-generated method stub

	}

	public Date getDateTime(byte [] bytes){

		int year = (int) bytes[0];
		int month = (int) bytes[1];  
		int day = (int) bytes[2];
		int hour = (int) bytes[3]; 
		int minute = (int) bytes[4]; 
		int second = (int) bytes[5];

		Date date = new Date(year, month, day, hour, minute, second);
	
		return date;
	}

	public void showResponse(byte frame, byte[] data){

		switch(frame){
			case Protocol.ACK_FRAME:
				System.out.println("DATA: ACK");
				break;
			case Protocol.TIME_FRAME:
				System.out.println("DATA: " + getDateTime(data));
				break;
			default:
				System.err.println("FRAME INVÁLIDO");
		}	
	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		LOG.info("client get message => " + message.toString());
		LOG.info("client :" + message);

		if (message instanceof ByteBuffer) {
			try {

				ByteBuffer b = (ByteBuffer) message;

				byte init = b.get();
				int bytes = b.get();
				byte frame = b.get();

				byte[] messageBytes = new byte[bytes - 5];
				b.get(messageBytes);

				byte crc = b.get();
				byte end = b.get();

				System.out.println("INIT: " + String.format("0x%02X", init));
				System.out.println("BYTES: " + String.format("0x%02X", bytes) + " = " + ((int) bytes));
				System.out.println("FRAME: " + String.format("0x%02X", frame));
				
				
				// System.out.println("DATA: " + new String(messageBytes, StandardCharsets.ISO_8859_1));
				// byte [] response = saveData(frame, messageBytes);
				showResponse(frame, messageBytes);


				System.out.println("CRC: " + String.format("0x%02X", crc));
				System.out.println("END: " + String.format("0x%02X", end));
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) {
		LOG.info("client send message: " + message.toString());
		// System.out.println("client send message: " + message.toString());
	}

	@Override
	public void serviceActivated(IoService service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void serviceInactivated(IoService service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exceptionCaught(IoSession session, Exception cause) {
		// TODO Auto-generated method stub

	}

}