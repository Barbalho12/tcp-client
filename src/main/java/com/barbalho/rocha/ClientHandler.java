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
		// LOG.info("session opened {" + session + "}");
	}

	@Override
	public void sessionClosed(IoSession session) {
		// LOG.info("client :" + session.getRemoteAddress().toString() + " close connection");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {

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

				log(init, bytes, frame, messageBytes, crc, end);

				showResponse(frame, messageBytes);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void log(byte init, int bytes, byte frame, byte[] messageBytes, byte crc, byte end ){
		String show = String.format("0x%02X", init);
		show += String.format(" 0x%02X", bytes);
		show += String.format(" 0x%02X", frame);
		for(int i = 0; i < messageBytes.length; i++){
			show += String.format(" 0x%02X", messageBytes[i]);
		}
		show += String.format(" 0x%02X", crc);
		show += String.format(" 0x%02X", end);
		System.out.println("client receive: [ " + show + " ]");
	}

	@Override
	public void messageSent(IoSession session, Object message) {
		System.out.println("client send message: " + message.toString());
	}

	@Override
	public void serviceActivated(IoService service) {

	}

	@Override
	public void serviceInactivated(IoService service) {

	}

	@Override
	public void exceptionCaught(IoSession session, Exception cause) {

	}

}