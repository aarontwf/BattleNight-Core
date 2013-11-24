package me.limebyte.battlenight.core.old;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class UtilDataInput implements DataInput {
    protected final DataInputStream inputStream;

    protected final InputStream b;
    protected final byte[] c;

    public UtilDataInput(InputStream inputStream) {
        b = inputStream;
        this.inputStream = new DataInputStream(inputStream);
        c = new byte[8];
    }

    public static String a(DataInput paramDataInput) throws IOException {
        return DataInputStream.readUTF(paramDataInput);
    }

    public final int a(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
        return b.read(paramArrayOfByte, paramInt1, paramInt2);
    }

    public final void closeStream() throws IOException {
        inputStream.close();
    }

    @Override
    public final boolean readBoolean() throws IOException {
        return inputStream.readBoolean();
    }

    @Override
    public final byte readByte() throws IOException {
        return inputStream.readByte();
    }

    @Override
    public final char readChar() throws IOException {
        inputStream.readFully(c, 0, 2);
        return (char) ((c[1] & 0xFF) << 8 | c[0] & 0xFF);
    }

    @Override
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public final void readFully(byte[] paramArrayOfByte) throws IOException {
        inputStream.readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    @Override
    public final void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
        inputStream.readFully(paramArrayOfByte, paramInt1, paramInt2);
    }

    @Override
    public final int readInt() throws IOException {
        inputStream.readFully(c, 0, 4);
        return c[3] << 24 | (c[2] & 0xFF) << 16 | (c[1] & 0xFF) << 8 | c[0] & 0xFF;
    }

    /** @deprecated */
    @Deprecated
    @Override
    public final String readLine() throws IOException {
        return inputStream.readLine();
    }

    @Override
    public final long readLong() throws IOException {
        inputStream.readFully(c, 0, 8);
        return c[7] << 56 | (c[6] & 0xFF) << 48 | (c[5] & 0xFF) << 40 | (c[4] & 0xFF) << 32 | (c[3] & 0xFF) << 24 | (c[2] & 0xFF) << 16 | (c[1] & 0xFF) << 8 | c[0] & 0xFF;
    }

    @Override
    public final short readShort() throws IOException {
        inputStream.readFully(c, 0, 2);
        return (short) ((c[1] & 0xFF) << 8 | c[0] & 0xFF);
    }

    public final String readString() throws IOException {
        int i = readInt();
        int j = 0;
        byte[] arrayOfByte = new byte[i];
        while (j < i) {
            j += a(arrayOfByte, j, i - j);
        }
        return new String(arrayOfByte);
    }

    @Override
    public final int readUnsignedByte() throws IOException {
        return inputStream.readUnsignedByte();
    }

    @Override
    public final int readUnsignedShort() throws IOException {
        inputStream.readFully(c, 0, 2);
        return (c[1] & 0xFF) << 8 | c[0] & 0xFF;
    }

    @Override
    public final String readUTF() throws IOException {
        return inputStream.readUTF();
    }

    @Override
    public final int skipBytes(int bytes) throws IOException {
        return inputStream.skipBytes(bytes);
    }
}
