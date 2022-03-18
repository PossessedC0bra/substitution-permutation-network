package kry.spnctr.blockCipher.spn;

import kry.spnctr.blockCipher.IBlockCipher;

/**
 * A Substitution Permutation Network with a fixed block size of 16 and a maximum key length of 32
 *
 * <b>Note:</b> due to performance reasons all calculations are done with integers
 */
public class SubstitutionPermutationNetwork implements IBlockCipher {

    private final int m_roundCount; // r
    private final int m_substitutionBlockLength; // n
    private final int m_substitutionBlockCount; // m

    private final SBox m_sBox;
    private final int[] m_pBox;

    // int has a length of 32 bits -> s = 32
    private final int m_key;
    // TODO ykl: introduce key factory of some sorts
    // private IRoundKeyFactory m_keyFactory;
    private final int[] m_encryptionRoundKeys;
    private final int[] m_decryptionRoundKeys;

    /**
     * @param r    number of rounds
     * @param n    length of substitution block
     * @param m    number of blocks to be substituted
     * @param sBox definition of substitutions
     * @param pBox definition of bit permutations
     * @param key  key from which individual round keys will be generated
     * @return a new instance of an SPN
     */
    public static SubstitutionPermutationNetwork init(int r, int n, int m, SBox sBox, int[] pBox, int key) {
        return new SubstitutionPermutationNetwork(r, n, m, sBox, pBox, key);
    }

    public SubstitutionPermutationNetwork(int r, int n, int m, SBox sBox, int[] pBox, int key) {
        m_roundCount = r;
        m_substitutionBlockLength = n;
        m_substitutionBlockCount = m;

        m_sBox = sBox;
        m_pBox = pBox;
        m_key = key;

        m_encryptionRoundKeys = new int[m_roundCount + 1];
        m_decryptionRoundKeys = new int[m_roundCount + 1];
        initRoundKeys();
    }

    private void initRoundKeys() {
        initEncryptionRoundKeys();
        initDecryptionRoundKeys();
    }

    private void initEncryptionRoundKeys() {
        for (int i = 0; i <= m_roundCount; i++) {
            m_encryptionRoundKeys[i] = getRoundKey(i);
        }
    }

    public int getRoundKey(int round) {
        int mask = (1 << 16) - 1;
        int start = 4 * (4 - round);
        return (mask & (m_key >> start));
    }

    private void initDecryptionRoundKeys() {
        m_decryptionRoundKeys[0] = m_encryptionRoundKeys[m_encryptionRoundKeys.length - 1];
        for (int i = 1; i < m_roundCount; i++) {
            m_decryptionRoundKeys[m_roundCount - i] = permute(m_encryptionRoundKeys[i]);
        }
        m_decryptionRoundKeys[m_encryptionRoundKeys.length - 1] = m_encryptionRoundKeys[0];
    }

    /* ****************************************************************************************** */

    @Override
    public int encrypt(int clearText) {
        return encryptInternal(clearText, m_encryptionRoundKeys, m_sBox.get());
    }

    /* ****************************************************************************************** */

    @Override
    public int decrypt(int cipher) {
        return encryptInternal(cipher, m_decryptionRoundKeys, m_sBox.getInverse());
    }

    /* ****************************************************************************************** */

    private int encryptInternal(int clearText, int[] keys, int[] sBox) {
        int cipher;
        // initial round
        cipher = clearText ^ keys[0];
        // r -1 regular rounds
        for (int i = 1; i < m_roundCount; i++) {
            cipher = substitute(cipher, sBox);
            cipher = permute(cipher);
            cipher = cipher ^ keys[i];
        }
        // final shortened round
        cipher = substitute(cipher, sBox);
        cipher = cipher ^ keys[m_roundCount];
        return cipher;
    }

    // TODO ykl: this is only public for tests -> make private
    public int substitute(int number, int[] sBox) {
        int result = 0;
        for (int i = 0; i < m_substitutionBlockCount; i++) {
            int extract = (number >>> m_substitutionBlockLength * i) & ((1 << m_substitutionBlockLength) - 1);
            int substitution = sBox[extract];
            result |= substitution << m_substitutionBlockLength * i;
        }
        return result;
    }

    // TODO ykl: this is only public for tests -> make private
    public int permute(int number) {
        int result = 0;
        for (int i = 0; i < m_substitutionBlockLength * m_substitutionBlockCount; i++) {
            result |= ((number >>> i) & 1) << m_pBox[i];
        }
        return result;
    }
}
