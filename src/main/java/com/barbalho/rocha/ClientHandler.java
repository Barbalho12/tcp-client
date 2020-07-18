package com.barbalho.rocha;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

import org.apache.mina.api.IdleStatus;
import org.apache.mina.api.IoHandler;
import org.apache.mina.api.IoService;
import org.apache.mina.api.IoSession;

public class ClientHandler implements IoHandler {

	@Override
	public void sessionOpened(final IoSession session) {

	}

	@Override
	public void sessionClosed(final IoSession session) {

	}

	@Override
	public void sessionIdle(final IoSession session, final IdleStatus status) {

	}

	public Date getDateTime(final byte[] bytes) {
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, (int) bytes[0]);
        cal.set(Calendar.MONTH, (int) bytes[1]);
        cal.set(Calendar.DAY_OF_MONTH, (int) bytes[2]);
        cal.set(Calendar.HOUR_OF_DAY, (int) bytes[3]);
        cal.set(Calendar.MINUTE, (int) bytes[4]);
        cal.set(Calendar.SECOND, (int) bytes[5]);
        return cal.getTime();
	}

	public void showResponse(final FrameMessage frameMessage) {
		switch (frameMessage.frame) {
			case Protocol.ACK_FRAME:
				LogFile.log("client receive ACK FRAME: [ " + frameMessage.toString() + " ]");
				System.out.println("DATA: ACK");
				break;
			case Protocol.TIME_FRAME:
				LogFile.log("client receive TIME FRAME: [ " + frameMessage.toString() + " ] = "
						+ getDateTime(frameMessage.data));
				System.out.println("DATA: " + getDateTime(frameMessage.data));
				break;
			default:
				System.err.println("client receive INVALID FRAME: " + frameMessage.toString());
				LogFile.log("client receive FRAME INVÁLIDO: [ " + frameMessage.toString() + " ]");
		}
	}

	private ByteBuffer readBuffer(final Object message) {
		return (ByteBuffer) message;
	}

	@Override
	public void messageReceived(final IoSession session, final Object message) {
		if (message instanceof ByteBuffer) {
			try {
				final ByteBuffer byteBuffer = readBuffer(message);
				final FrameMessage frameMessage = new FrameMessage(byteBuffer);
				showResponse(frameMessage);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void messageSent(final IoSession session, final Object message) {

	}

	@Override
	public void serviceActivated(final IoService service) {

	}

	@Override
	public void serviceInactivated(final IoService service) {

	}

	@Override
	public void exceptionCaught(final IoSession session, final Exception cause) {

	}

}