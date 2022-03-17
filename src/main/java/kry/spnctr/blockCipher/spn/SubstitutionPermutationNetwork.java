package kry.spnctr.blockCipher.spn;

/**
 * A Substitution Permutation Network with a fixed block size of 16 and a maximum key length of 32
 *
 * <b>Note:</b> due to performance reasons all calculations are done with integers
 */
public class SubstitutionPermutationNetwork {

    private final int m_roundCount = 4; // r
    private final int m_substitutionBlockLength = 4; // n
    private final int m_substitutionBlockCount = 4; // m

    public final int[] m_sBox = new int[] {0b1110, 0b0100, 0b1101, 0b0001, 0b0010, 0b1111, 0b1011, 0b1000, 0b0011, 0b1010, 0b0110, 0b1100, 0b0101, 0b1001, 0b0000, 0b0111};
    public final int[] m_inverseSBox = new int[m_sBox.length];
    private final int[] m_pBox = new int[] {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

    private final int totalKeyLength = 32; // s
    private int m_key;
    private final int[] m_encryptionRoundKeys = new int[m_roundCount + 1];
    private final int[] m_decryptionRoundKeys = new int[m_roundCount + 1];

    // private IRoundKeyFactory m_keyFactory;

    public static SubstitutionPermutationNetwork init(int key) {
        return new SubstitutionPermutationNetwork(key);
    }

    /**
     *
     * @param r number of rounds
     * @param n length of substitution block
     * @param m number of blocks to be substituted
     * @param sBox
     * @param pBox
     * @param key
     * @return
     */
    public static SubstitutionPermutationNetwork init(int r, int n, int m, int[] sBox, int[] pBox, int key) {
        return new SubstitutionPermutationNetwork(key);
    }

    public SubstitutionPermutationNetwork(int key) {
        m_key = key;

        initInverseSBox();
        initRoundKeys();
    }

    private void initInverseSBox() {
        for (int i = 0; i < m_sBox.length; i++) {
            m_inverseSBox[m_sBox[i]] = i;
        }
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

    public int encrypt(int clearText) {
        return encryptInternal(clearText, m_encryptionRoundKeys, m_sBox);
    }

    private int encryptInternal(int clearText, int[] keys, int[] sBox) {
        int cipher = 0;
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

    public int substitute(int number, int[] sBox) {
        int result = 0;
        for (int i = 0; i < m_substitutionBlockCount; i++) {
            int extract = (number >>> m_substitutionBlockLength * i) & ((1 << m_substitutionBlockLength) - 1);
            int substitution = sBox[extract];
            result |= substitution << m_substitutionBlockLength * i;
        }
        return result;
    }

    public int permute(int number) {
        int result = 0;
        for (int i = 0; i < m_substitutionBlockLength * m_substitutionBlockCount; i++) {
            result |= ((number >>> i) & 1) <<  m_pBox[i];
        }
        return result;
    }

    /* ****************************************************************************************** */

    public int decrypt(int cipher) {
        return encryptInternal(cipher, m_decryptionRoundKeys, m_inverseSBox);
    }
}
