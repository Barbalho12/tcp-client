package com.barbalho.rocha;

import java.nio.ByteBuffer;
import java.util.Date;

import org.apache.mina.api.IdleStatus;
import org.apache.mina.api.IoHandler;
import org.apache.mina.api.IoService;
import org.apache.mina.api.IoSession;

public class ClientHandler implements IoHandler {

	@Override
	public void sessionOpened(IoSession session) {

	}

	@Override
	public void sessionClosed(IoSession session) {

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {

	}

	public Date getDateTime(byte[] bytes) {
		int year = (int) bytes[0];
		int month = (int) bytes[1];
		int day = (int) bytes[2];
		int hour = (int) bytes[3];
		int minute = (int) bytes[4];
		int second = (int) bytes[5];
		Date date = new Date(year, month, day, hour, minute, second);
		return date;
	}

	public void showResponse(FrameMessage frameMessage) {
		switch (frameMessage.frame) {
			case Protocol.ACK_FRAME:
				LogFile.log("client receive ACK FRAME: [ " + frameMessage.toString() + " ]");
				System.out.println("DATA: ACK");
				break;
			case Protocol.TIME_FRAME:
				LogFile.log("client receive TIME FRAME: [ " + frameMessage.toString() + " ] = "+getDateTime(frameMessage.data));
				System.out.println("DATA: " + getDateTime(frameMessage.data));
				break;
			default:
				System.err.println("client receive INVALID FRAME: "+frameMessage.toString());
				LogFile.log("client receive FRAME INVÁLIDO: [ " + frameMessage.toString() + " ]");
		}
	}

	private ByteBuffer readBuffer(Object message){
		return (ByteBuffer) message;
	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		if (message instanceof ByteBuffer) {
			try {
				ByteBuffer byteBuffer = readBuffer(message);
				FrameMessage frameMessage = new FrameMessage(byteBuffer);
				showResponse(frameMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) {

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