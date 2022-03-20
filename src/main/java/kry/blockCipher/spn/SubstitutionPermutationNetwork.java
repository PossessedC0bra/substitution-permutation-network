package kry.blockCipher.spn;

import kry.blockCipher.IBlockCipher;

/**
 * A Substitution Permutation Network with a fixed block size of 16 and a maximum key length of 32
 *
 * <b>Note:</b> due to performance reasons all calculations are done with integers
 */
public class SubstitutionPermutationNetwork implements IBlockCipher {

    private static final int SIZE_OF_BLOCK = 16;
    private static final int BLOCK_MASK = (1 << SIZE_OF_BLOCK) - 1;

    private final int roundCount; // r
    private final int substitutionBlockLength; // n
    private final int substitutionBlockCount; // m

    private final SBox sBox;
    private final int[] pBox;

    // int has a length of 32 bits -> s = 32
    private final int key;
    private final int[] encryptionRoundKeys;
    private final int[] decryptionRoundKeys;

    /**
     * @param r    number of rounds
     * @param n    length of substitution block
     * @param m    number of blocks to be substituted
     * @param sBox definition of substitutions
     * @param pBox definition of bit permutations
     * @param key  key from which individual round keys will be generated
     */
    public SubstitutionPermutationNetwork(int r, int n, int m, SBox sBox, int[] pBox, int key) {
        roundCount = r;
        substitutionBlockLength = n;
        substitutionBlockCount = m;

        this.sBox = sBox;
        this.pBox = pBox;

        this.key = key;
        encryptionRoundKeys = new int[roundCount + 1];
        decryptionRoundKeys = new int[roundCount + 1];
        initRoundKeys();
    }

    private void initRoundKeys() {
        initEncryptionRoundKeys();
        initDecryptionRoundKeys();
    }

    private void initEncryptionRoundKeys() {
        for (int i = 0; i <= roundCount; i++) {
            encryptionRoundKeys[i] = getRoundKey(i);
        }
    }

    public int getRoundKey(int round) {
        int start = 4 * (roundCount - round);
        return (key >> start) & BLOCK_MASK;
    }

    private void initDecryptionRoundKeys() {
        decryptionRoundKeys[0] = encryptionRoundKeys[encryptionRoundKeys.length - 1];
        for (int i = 1; i < roundCount; i++) {
            decryptionRoundKeys[roundCount - i] = permute(encryptionRoundKeys[i]);
        }
        decryptionRoundKeys[encryptionRoundKeys.length - 1] = encryptionRoundKeys[0];
    }

    /* ****************************************************************************************** */

    @Override
    public int encrypt(int clearText) {
        return encryptInternal(clearText, encryptionRoundKeys, sBox.get());
    }

    /* ****************************************************************************************** */

    @Override
    public int decrypt(int cipher) {
        return encryptInternal(cipher, decryptionRoundKeys, sBox.getInverse());
    }

    /* ****************************************************************************************** */

    private int encryptInternal(int clearText, int[] keys, int[] sBox) {
        int cipher;
        // initial round
        cipher = clearText ^ keys[0];
        // r -1 regular rounds
        for (int i = 1; i < roundCount; i++) {
            cipher = substitute(cipher, sBox);
            cipher = permute(cipher);
            cipher ^= keys[i];
        }
        // final shortened round
        cipher = substitute(cipher, sBox);
        cipher ^= keys[roundCount];
        return cipher;
    }

    // TODO ykl: this is only public for tests -> make private
    public int substitute(int number, int[] sBox) {
        int result = 0;
        for (int i = 0; i < substitutionBlockCount; i++) {
            int extract = (number >>> substitutionBlockLength * i) & ((1 << substitutionBlockLength) - 1);
            int substitution = sBox[extract];
            result |= substitution << substitutionBlockLength * i;
        }
        return result;
    }

    // TODO ykl: this is only public for tests -> make private
    public int permute(int number) {
        int result = 0;
        for (int i = 0; i < substitutionBlockLength * substitutionBlockCount; i++) {
            result |= ((number >>> i) & 1) << pBox[i];
        }
        return result;
    }
}
