package com.zyc.zdh.util;

public class Base64Util {
    private static final byte[] encodingTable = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] decodingTable = new byte[128];

    public Base64Util() {
    }

    public static byte[] encode(byte[] data, int offset) {
        int realCount = data.length - offset;
        int modulus = realCount % 3;
        byte[] bytes;
        if (modulus == 0) {
            bytes = new byte[4 * realCount / 3];
        } else {
            bytes = new byte[4 * (realCount / 3 + 1)];
        }

        int dataLength = data.length - modulus;
        int b1 = offset;

        int b2;
        for(b2 = 0; b1 < dataLength; b2 += 4) {
            int a1 = data[b1] & 255;
            int a2 = data[b1 + 1] & 255;
            int a3 = data[b1 + 2] & 255;
            bytes[b2] = encodingTable[a1 >>> 2 & 63];
            bytes[b2 + 1] = encodingTable[(a1 << 4 | a2 >>> 4) & 63];
            bytes[b2 + 2] = encodingTable[(a2 << 2 | a3 >>> 6) & 63];
            bytes[b2 + 3] = encodingTable[a3 & 63];
            b1 += 3;
        }

        int d1;
        switch(modulus) {
        case 0:
        default:
            break;
        case 1:
            d1 = data[data.length - 1] & 255;
            b1 = d1 >>> 2 & 63;
            b2 = d1 << 4 & 63;
            bytes[bytes.length - 4] = encodingTable[b1];
            bytes[bytes.length - 3] = encodingTable[b2];
            bytes[bytes.length - 2] = 61;
            bytes[bytes.length - 1] = 61;
            break;
        case 2:
            d1 = data[data.length - 2] & 255;
            int d2 = data[data.length - 1] & 255;
            b1 = d1 >>> 2 & 63;
            b2 = (d1 << 4 | d2 >>> 4) & 63;
            int b3 = d2 << 2 & 63;
            bytes[bytes.length - 4] = encodingTable[b1];
            bytes[bytes.length - 3] = encodingTable[b2];
            bytes[bytes.length - 2] = encodingTable[b3];
            bytes[bytes.length - 1] = 61;
        }

        return bytes;
    }

    public static byte[] decode(byte[] data) {
        data = discardNonBase64Bytes(data);
        byte[] bytes;
        if (data[data.length - 2] == 61) {
            bytes = new byte[(data.length / 4 - 1) * 3 + 1];
        } else if (data[data.length - 1] == 61) {
            bytes = new byte[(data.length / 4 - 1) * 3 + 2];
        } else {
            bytes = new byte[data.length / 4 * 3];
        }

        int i = 0;

        byte b1;
        byte b2;
        byte b3;
        byte b4;
        for(int j = 0; i < data.length - 4; j += 3) {
            b1 = decodingTable[data[i]];
            b2 = decodingTable[data[i + 1]];
            b3 = decodingTable[data[i + 2]];
            b4 = decodingTable[data[i + 3]];
            bytes[j] = (byte)(b1 << 2 | b2 >> 4);
            bytes[j + 1] = (byte)(b2 << 4 | b3 >> 2);
            bytes[j + 2] = (byte)(b3 << 6 | b4);
            i += 4;
        }

        if (data[data.length - 2] == 61) {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];
            bytes[bytes.length - 1] = (byte)(b1 << 2 | b2 >> 4);
        } else if (data[data.length - 1] == 61) {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];
            b3 = decodingTable[data[data.length - 2]];
            bytes[bytes.length - 2] = (byte)(b1 << 2 | b2 >> 4);
            bytes[bytes.length - 1] = (byte)(b2 << 4 | b3 >> 2);
        } else {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];
            b3 = decodingTable[data[data.length - 2]];
            b4 = decodingTable[data[data.length - 1]];
            bytes[bytes.length - 3] = (byte)(b1 << 2 | b2 >> 4);
            bytes[bytes.length - 2] = (byte)(b2 << 4 | b3 >> 2);
            bytes[bytes.length - 1] = (byte)(b3 << 6 | b4);
        }

        return bytes;
    }

    public static byte[] decode(String data) {
        data = discardNonBase64Chars(data);
        byte[] bytes;
        if (data.charAt(data.length() - 2) == '=') {
            bytes = new byte[(data.length() / 4 - 1) * 3 + 1];
        } else if (data.charAt(data.length() - 1) == '=') {
            bytes = new byte[(data.length() / 4 - 1) * 3 + 2];
        } else {
            bytes = new byte[data.length() / 4 * 3];
        }

        int i = 0;

        byte b1;
        byte b2;
        byte b3;
        byte b4;
        for(int j = 0; i < data.length() - 4; j += 3) {
            b1 = decodingTable[data.charAt(i)];
            b2 = decodingTable[data.charAt(i + 1)];
            b3 = decodingTable[data.charAt(i + 2)];
            b4 = decodingTable[data.charAt(i + 3)];
            bytes[j] = (byte)(b1 << 2 | b2 >> 4);
            bytes[j + 1] = (byte)(b2 << 4 | b3 >> 2);
            bytes[j + 2] = (byte)(b3 << 6 | b4);
            i += 4;
        }

        if (data.charAt(data.length() - 2) == '=') {
            b1 = decodingTable[data.charAt(data.length() - 4)];
            b2 = decodingTable[data.charAt(data.length() - 3)];
            bytes[bytes.length - 1] = (byte)(b1 << 2 | b2 >> 4);
        } else if (data.charAt(data.length() - 1) == '=') {
            b1 = decodingTable[data.charAt(data.length() - 4)];
            b2 = decodingTable[data.charAt(data.length() - 3)];
            b3 = decodingTable[data.charAt(data.length() - 2)];
            bytes[bytes.length - 2] = (byte)(b1 << 2 | b2 >> 4);
            bytes[bytes.length - 1] = (byte)(b2 << 4 | b3 >> 2);
        } else {
            b1 = decodingTable[data.charAt(data.length() - 4)];
            b2 = decodingTable[data.charAt(data.length() - 3)];
            b3 = decodingTable[data.charAt(data.length() - 2)];
            b4 = decodingTable[data.charAt(data.length() - 1)];
            bytes[bytes.length - 3] = (byte)(b1 << 2 | b2 >> 4);
            bytes[bytes.length - 2] = (byte)(b2 << 4 | b3 >> 2);
            bytes[bytes.length - 1] = (byte)(b3 << 6 | b4);
        }

        return bytes;
    }

    private static byte[] discardNonBase64Bytes(byte[] data) {
        byte[] temp = new byte[data.length];
        int bytesCopied = 0;

        for(int i = 0; i < data.length; ++i) {
            if (isValidBase64Byte(data[i])) {
                temp[bytesCopied++] = data[i];
            }
        }

        byte[] newData = new byte[bytesCopied];
        System.arraycopy(temp, 0, newData, 0, bytesCopied);
        return newData;
    }

    private static String discardNonBase64Chars(String data) {
        StringBuffer sb = new StringBuffer();
        int length = data.length();

        for(int i = 0; i < length; ++i) {
            if (isValidBase64Byte((byte)data.charAt(i))) {
                sb.append(data.charAt(i));
            }
        }

        return sb.toString();
    }

    private static boolean isValidBase64Byte(byte b) {
        if (b == 61) {
            return true;
        } else if (b >= 0 && b < 128) {
            return decodingTable[b] != -1;
        } else {
            return false;
        }
    }

    public static String encode(String data, String charset) throws Exception {
        if (data != null && data.length() != 0) {
            int offset = 0;
            byte[] result = encode(data.getBytes(charset), offset);
            StringBuffer sb = new StringBuffer(result.length);

            for(int i = 0; i < result.length; ++i) {
                sb.append((char)result[i]);
            }

            return sb.toString();
        } else {
            return data;
        }
    }

    public static String decode(String data, String charset) throws Exception {
        return data != null && data.length() != 0 ? new String(decode(data), charset) : data;
    }

    public static void main(String[] args) throws Exception {
        String data = "我们";
        String data1 = encode(data, "Unicode");
        String data2 = decode(data1, "Unicode");
        System.out.println(data);
        System.out.println(data1);
        System.out.println(data2);
    }

    static {
        int i;
        for(i = 0; i < 128; ++i) {
            decodingTable[i] = -1;
        }

        for(i = 65; i <= 90; ++i) {
            decodingTable[i] = (byte)(i - 65);
        }

        for(i = 97; i <= 122; ++i) {
            decodingTable[i] = (byte)(i - 97 + 26);
        }

        for(i = 48; i <= 57; ++i) {
            decodingTable[i] = (byte)(i - 48 + 52);
        }

        decodingTable[43] = 62;
        decodingTable[47] = 63;
    }
}
