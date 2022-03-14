package spn;

public class SubstitutionPermutationNetwork {

    private final int roundCount = 4; // r
    private final int substitutionLength = 4; // n
    private final int permutationLength = 4; // m

    private final int[] sBox = new int[] {0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7};
    private final int[] pBox = new int[] {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

    private final int keyLength = 32; // s
    // private final int key = 0b0011_1010_1001_0100_1101_0110_0011_1111;
    // TODO ykl: key below is only used for testing
    private final int key = 0b0001_0001_0010_1000_1000_1100_0000_0000; // TODO ykl: make this configurable

    public static SubstitutionPermutationNetwork init() {
        return new SubstitutionPermutationNetwork();
    }

    public SubstitutionPermutationNetwork() {
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
        for (int i = substitutionLength - 1; i >= 0; i--) {
            int extract = (number >>> substitutionLength * i) & 0b1111;
            int substitution = sBox[extract];
            result |= substitution << substitutionLength * i;
        }
        return result;
    }

    public int permute(int number) {
        int result = 0;
        for (int i = substitutionLength * permutationLength - 1; i >= 0; i--) {
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
