package de.guntram.gamecubetexturedisplay;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class GamecubeTextureReader {
    RandomAccessFile backend;
    byte[] buffer;

    public GamecubeTextureReader(String fileName) throws IOException {
        RandomAccessFile file=new RandomAccessFile(fileName, "r");
        int length=(int)file.length();
        buffer=new byte[length];
        if (file.read(buffer)!=length) {
            throw new IOException("Didn't read enough bytes");
        };
    }
    
    public byte[] getPalette() {
        byte[] result=Arrays.copyOfRange(buffer, 0x40, 0x240);
        return result;
    }
                
    
    public byte[] getBuffer() {
        return buffer;
    };

    void readImage(long filePos, byte[] target, int size) {
        System.arraycopy(buffer, (int)filePos, target, 0, size);
    }
}
