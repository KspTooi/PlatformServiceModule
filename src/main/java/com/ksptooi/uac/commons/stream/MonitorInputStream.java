package com.ksptooi.uac.commons.stream;

import java.io.IOException;
import java.io.InputStream;

public class MonitorInputStream extends InputStream {

    private InputStream is = null;
    private long total;

    public MonitorInputStream(InputStream is) {
        this.is = is;
    }

    public long getTransferLength() {
        return total;
    }

    @Override
    public int read() throws IOException {
        int read = is.read();
        this.total = total + read;
        return read;
    }


    @Override
    public int read(byte[] b) throws IOException {
        int read = is.read(b);
        this.total = total + read;
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = is.read(b, off, len);
        this.total = total + read;
        return read;
    }


    @Override
    public long skip(long n) throws IOException {
        return is.skip(n);
    }

    @Override
    public int available() throws IOException {
        return is.available();
    }

    @Override
    public void close() throws IOException {
        System.out.print("\r\n");
        is.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        is.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        is.reset();
    }

    @Override
    public boolean markSupported() {
        return is.markSupported();
    }

}
