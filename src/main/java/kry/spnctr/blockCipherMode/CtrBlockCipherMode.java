package kry.spnctr.blockCipherMode;

import kry.spnctr.blockCipher.spn.SubstitutionPermutationNetwork;

public class CtrBlockCipherMode {

    private final static int SIZE_OF_BYTE = 8;
    private final static int BYTE_MASK = (1 << SIZE_OF_BYTE) -1;

    private static final int BLOCK_SIZE = 16;
    private static final int BLOCK_MASK = (1 << BLOCK_SIZE) - 1;

    private static final int BLOCK_CIPHER_KEY = 0b0011_1010_1001_0100_1101_0110_0011_1111;

    private final SubstitutionPermutationNetwork blockCipher;

    public CtrBlockCipherMode() {
        blockCipher = SubstitutionPermutationNetwork.init(BLOCK_CIPHER_KEY);
    }

    public byte[] encrypt(byte[] plainText) {
        byte[] paddedPlainText = padPlainText(plainText);

        int nonce = 1234;
        // cipher includes nonce at the beginning -> add two bytes for nonce
        byte[] cipher = new byte[paddedPlainText.length + 2];
        // store nonce (y-1)
        cipher[0] = (byte) ((nonce >>> SIZE_OF_BYTE) & BYTE_MASK);
        cipher[1] = (byte) (nonce & BYTE_MASK);

        int counter = 0;
        for (int i = 0; i < paddedPlainText.length; i = i + 2) {
            int increasedNonce = (nonce + counter++) & BLOCK_MASK;
            int encryptedNonce = blockCipher.encrypt(increasedNonce);

            int block = (paddedPlainText[i] << SIZE_OF_BYTE) | Byte.toUnsignedInt(paddedPlainText[i + 1]);
            int encryptedBlock = encryptedNonce ^ block;

            cipher[i + 2] = (byte) ((encryptedBlock >>> SIZE_OF_BYTE) & BYTE_MASK);
            cipher[i + 2 + 1] = (byte) (encryptedBlock & BYTE_MASK);
        }

        return cipher;
    }

    private byte[] padPlainText(byte[] bytes) {
        boolean divisibleByBlockSize = (bytes.length * SIZE_OF_BYTE) % BLOCK_SIZE == 0;
        byte[] paddingBytes = getPaddingBytes(divisibleByBlockSize);
        return padByteArray(bytes, paddingBytes);
    }

    private byte[] getPaddingBytes(boolean divisibleByBlockSize) {
        return divisibleByBlockSize ? new byte[] {Byte.MIN_VALUE, 0} : new byte[] {Byte.MIN_VALUE};
    }

    private byte[] padByteArray(byte[] original, byte[] padding) {
        byte[] paddedBytes = new byte[original.length + padding.length];
        System.arraycopy(original, 0, paddedBytes, 0, original.length);
        System.arraycopy(padding, 0, paddedBytes, original.length, padding.length);
        return paddedBytes;
    }

    /* ****************************************************************************************** */

    public byte[] decrypt(byte[] cipher) {
        int nonce = (cipher[0] << SIZE_OF_BYTE) | Byte.toUnsignedInt(cipher[1]);

        // plain text does not contain the nonce -> 2 bytes shorter
        byte[] paddedPlainTextBytes = new byte[cipher.length - 2];

        int counter = 0;
        for (int i = 2; i < cipher.length; i = i + 2) {
            int increasedNonce = (nonce + counter++) & ((1 << BLOCK_SIZE) - 1);
            int encryptedNonce = blockCipher.encrypt(increasedNonce);

            int block = (cipher[i] << SIZE_OF_BYTE | Byte.toUnsignedInt(cipher[i + 1]));
            int decryptedBlock = encryptedNonce ^ block;

            paddedPlainTextBytes[i - 2] = (byte) ((decryptedBlock >>> 8) & BYTE_MASK);
            paddedPlainTextBytes[i - 1] = (byte) (decryptedBlock & BYTE_MASK);
        }

        return removePadding(paddedPlainTextBytes);
    }

    private byte[] removePadding(byte[] paddedBytes) {
        int unpaddedLength = paddedBytes.length;
        while (paddedBytes[unpaddedLength - 1] == Byte.MIN_VALUE || paddedBytes[unpaddedLength - 1] == 0) {
            unpaddedLength--;
        }

        byte[] bytesWithoutPadding = new byte[unpaddedLength];
        System.arraycopy(paddedBytes, 0, bytesWithoutPadding, 0, unpaddedLength);
        return bytesWithoutPadding;
    }
}
