package me.limebyte.battlenight.core.util.sound;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class UtilDataInput implements DataInput {
    protected final DataInputStream inputStream;
    protected final InputStream b;
    protected final byte[] c;

    public static String a(DataInput paramDataInput) throws IOException {
        return DataInputStream.readUTF(paramDataInput);
    }

    public UtilDataInput(InputStream inputStream) {
        this.b = inputStream;
        this.inputStream = new DataInputStream(inputStream);
        this.c = new byte[8];
    }

    public final void closeStream() throws IOException {
        this.inputStream.close();
    }

    public final int a(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
        return this.b.read(paramArrayOfByte, paramInt1, paramInt2);
    }

    public final boolean readBoolean() throws IOException {
        return this.inputStream.readBoolean();
    }

    public final byte readByte() throws IOException {
        return this.inputStream.readByte();
    }

    public final char readChar() throws IOException {
        this.inputStream.readFully(this.c, 0, 2);
        return (char) ((this.c[1] & 0xFF) << 8 | this.c[0] & 0xFF);
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public final void readFully(byte[] paramArrayOfByte) throws IOException {
        this.inputStream.readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    public final void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
        this.inputStream.readFully(paramArrayOfByte, paramInt1, paramInt2);
    }

    public final int readInt() throws IOException {
        this.inputStream.readFully(this.c, 0, 4);
        return this.c[3] << 24 | (this.c[2] & 0xFF) << 16 | (this.c[1] & 0xFF) << 8 | this.c[0] & 0xFF;
    }

    /** @deprecated */
    public final String readLine() throws IOException {
        return this.inputStream.readLine();
    }

    public final long readLong() throws IOException {
        this.inputStream.readFully(this.c, 0, 8);
        return this.c[7] << 56 | (this.c[6] & 0xFF) << 48 | (this.c[5] & 0xFF) << 40 | (this.c[4] & 0xFF) << 32 | (this.c[3] & 0xFF) << 24 | (this.c[2] & 0xFF) << 16 | (this.c[1] & 0xFF) << 8
                | this.c[0] & 0xFF;
    }

    public final short readShort() throws IOException {
        this.inputStream.readFully(this.c, 0, 2);
        return (short) ((this.c[1] & 0xFF) << 8 | this.c[0] & 0xFF);
    }

    public final String readUTF() throws IOException {
        return this.inputStream.readUTF();
    }

    public final int readUnsignedByte() throws IOException {
        return this.inputStream.readUnsignedByte();
    }

    public final int readUnsignedShort() throws IOException {
        this.inputStream.readFully(this.c, 0, 2);
        return (this.c[1] & 0xFF) << 8 | this.c[0] & 0xFF;
    }

    public final int skipBytes(int bytes) throws IOException {
        return inputStream.skipBytes(bytes);
    }

    public final String readString() throws IOException {
        int i = readInt();
        int j = 0;
        byte[] arrayOfByte = new byte[i];
        while (j < i)
            j += a(arrayOfByte, j, i - j);
        return new String(arrayOfByte);
    }
}
