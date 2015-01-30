package com;

/**
 * Created by justhacker on 14-8-5.
 */

import java.net.InetAddress;

/**
 * Generate UUID
 *
 * @author: justhacker
 */
public class UUIDHexGenerator {

    private String sep = "";
    private static int IP;
    private static short counter = (short) 0;
    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

    private static UUIDHexGenerator uuidgen = new UUIDHexGenerator();

    static {
        int ipadd;
        try {
            ipadd = toInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception e) {
            ipadd = 0;
        }
        IP = ipadd;
    }

    public static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

    public static UUIDHexGenerator getInstance() {
        return uuidgen;
    }

    protected String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    protected int getJVM() {
        return JVM;
    }

    protected synchronized short getCount() {
        if (counter < 0) {
            counter = 0;
        }
        return counter++;
    }

    protected int getIP() {
        return IP;
    }

    protected short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    protected int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    /**
     * Generate UUID
     *
     * @return UUID
     */
    public String generate() {
        return new StringBuffer(32).append(format(getIP())).append(sep)
                .append(format(getJVM())).append(sep)
                .append(format(getHiTime())).append(sep)
                .append(format(getLoTime())).append(sep)
                .append(format(getCount())).toString();
    }

    public static void main(String args[]) {
        UUIDHexGenerator id = new UUIDHexGenerator();
        for (int i = 0; i < 10; i++) {
            System.out.println(id.generate());
        }
    }
}
