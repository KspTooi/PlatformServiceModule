package com.ksptooi.uac.commons.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProgressInputStream extends InputStream{


    private InputStream is = null;

    private final long total;

    private long current;

    public ProgressInputStream(long total,InputStream is){
        this.is = is;
        this.total = total;
    }

    @Override
    public int read() throws IOException {
        int read = is.read();
        this.update(read);
        return read;
    }

    private void update(long length){
        current = current + length;
        updateProgress("处理中",current,total-1);
    }

    private void updateProgress(String title, long progress, long total) {

        int width = 50;

        long percent = progress * 100 / total;

        long completeLength = progress * width / total;

        StringBuilder sb = new StringBuilder();

        sb.append(title).append(" ");

        sb.append('[');

        for (int i = 0; i < completeLength; i++) {
            sb.append('=');
        }

        sb.append('>');

        for (long i = completeLength; i < width; i++) {
            sb.append(' ');
        }

        sb.append(']');

        sb.append(String.format(" %d%%", percent));

        sb.append(" ").append(progress).append(" of ").append(total);

        System.out.print("\r" + sb.toString());
    }


    @Override
    public int read(byte[] b) throws IOException {
        int read = is.read(b);
        this.update(read);
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = is.read(b, off, len);
        this.update(read);
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
