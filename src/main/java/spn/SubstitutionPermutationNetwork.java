package spn;

public class SubstitutionPermutationNetwork {

    private final int roundCount = 4; // r
    private final int substitutionBlockLength = 4; // n
    private final int substitutionBlockCount = 4; // m

    private final int[] sBox = new int[] {0b1110, 0b0100, 0b1101, 0b0001, 0b0010, 0b1111, 0b1011, 0b1000, 0b0011, 0b0001, 0b0110, 0b1100, 0b0101, 0b1001, 0b0000, 0b0111};
    private final int[] sBoxInverse = new int[sBox.length];
    private final int[] pBox = new int[] {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

    private final int totalKeyLength = 32; // s

    // private final int key = 0b0011_1010_1001_0100_1101_0110_0011_1111;
    // TODO ykl: key below is only used for testing
    private final int key = 0b0001_0001_0010_1000_1000_1100_0000_0000; // TODO ykl: make this configurable

    public static SubstitutionPermutationNetwork init() {
        return new SubstitutionPermutationNetwork();
    }

    public SubstitutionPermutationNetwork() {
        initSBoxInverse();
    }

    private void initSBoxInverse() {
        for (int i = 0; i < sBox.length; i++) {
            sBoxInverse[sBox[i]] = i;
        }
    }

    public int encrypt(int clearText) {
        // initial round
        clearText = clearText ^ getRoundKey(0);
        // regular rounds
        for (int i = 1; i < roundCount; i++) {
            clearText = substitute(clearText);
            clearText = permute(clearText);
            clearText = clearText ^ getRoundKey(i);
        }
        // final round
        clearText = substitute(clearText);
        clearText = clearText ^ getRoundKey(roundCount);
        return clearText;
    }

    public int substitute(int number) {
        int result = 0;
        for (int i = 0; i < substitutionBlockCount; i++) {
            int extract = (number >>> substitutionBlockLength * i) & ((1 << substitutionBlockLength) - 1);
            int substitution = sBox[extract];
            result |= substitution << substitutionBlockLength * i;
        }
        return result;
    }

    public int permute(int number) {
        int result = 0;
        for (int i = 0; i < substitutionBlockLength * substitutionBlockCount; i++) {
            result |= ((number >>> i) & 1) <<  pBox[i];
        }
        return result;
    }

    public int getRoundKey(int round) {
        int mask = (1 << 16) - 1;
        int start = 4 * (4 - round);
        return (mask & (key >> start));
    }
}
