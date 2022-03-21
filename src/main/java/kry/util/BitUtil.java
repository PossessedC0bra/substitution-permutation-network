package kry.util;

public final class BitUtil {

    private final static char ONE = '1';

    private final static int SIZE_OF_BYTE = 8;
    private final static int BYTE_MASK = (1 << SIZE_OF_BYTE) - 1;

    private BitUtil() {
    }

    /**
     * Reads a string consisting of 1s and 0s and parses it into a byte array.
     *
     * <b>Note:</b> Characters other than 1s and 0s are treated like a 0.
     *
     * @param bitString the string representing a binary number
     * @return byte[]
     */
    public static byte[] readBitString(String bitString) {
        if (bitString == null || bitString.isEmpty()) {
            return null;
        }

        byte[] bytes = new byte[bitString.length() / 8];

        int index = 0;
        while (index < bitString.length()) {
            if (bitString.charAt(index) == ONE) {
                bytes[index / 8] |= 1 << 7 - (index % 8);
            }

            index++;
        }

        return bytes;
    }

    public static void printByteArrayBitRepresentation(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte loadedByte : bytes) {
            // extending byte by 2^8 so leading zeros are formatted correctly
            int convertedInteger = (loadedByte & BYTE_MASK) + (1 << 8);
            sb.append(Integer.toBinaryString(convertedInteger).substring(1));
        }
        System.out.println(sb);
    }
}
