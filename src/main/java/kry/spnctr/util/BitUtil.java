package kry.spnctr.util;

public final class BitUtil {

    private final static char ZERO = '0';
    private final static char ONE = '1';

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
    public static byte[] readFromString(String bitString) {
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
            int convertedInteger = (loadedByte & 0b1111_1111) + (1 << 8);
            sb.append(Integer.toBinaryString(convertedInteger).substring(1));
        }
        System.out.println(sb);
    }

    public static int allOnes(int oneCount) {
        return (1 << oneCount) - 1;
    }
}
