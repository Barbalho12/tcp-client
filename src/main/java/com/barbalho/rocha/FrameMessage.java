package com.barbalho.rocha;

import java.nio.ByteBuffer;
import java.util.Arrays;

class FrameMessage {
    byte init;
    int bytes;
    byte frame;
    byte[] data;
    byte crc;
    byte end;

    public FrameMessage(ByteBuffer byteBuffer){
        init = byteBuffer.get();
        bytes = byteBuffer.get();
        frame = byteBuffer.get();

        data = new byte[bytes - 5];
        byteBuffer.get(data);
        crc = byteBuffer.get();
        end = byteBuffer.get();
    }

    public FrameMessage(byte [] bytesMessage){
        init = bytesMessage[0];
        bytes = bytesMessage[1];
        frame = bytesMessage[2];
        data = Arrays.copyOfRange(bytesMessage, 3, bytesMessage.length-2);
        crc = bytesMessage[bytesMessage.length-2];
        end = bytesMessage[bytesMessage.length-1];
    }

    @Override
    public String toString() {
        String show = String.format("0x%02X", init);
        show += String.format(" 0x%02X", bytes);
        show += String.format(" 0x%02X", frame);
        for(int i = 0; i < data.length; i++){
            show += String.format(" 0x%02X", data[i]);
        }
        show += String.format(" 0x%02X", crc);
        show += String.format(" 0x%02X", end);
        return show;
    }
}