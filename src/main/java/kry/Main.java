package kry;

import kry.blockCipher.spn.SBox;
import kry.blockCipher.spn.SubstitutionPermutationNetwork;
import kry.blockCipherMode.CtrBlockCipherMode;
import kry.util.BitUtil;
import kry.util.FileUtil;

import java.nio.charset.StandardCharsets;

public class Main {

    private static final int r = 4;
    private static final int n = 4;
    private static final int m = 4;
    private static final SBox sBox = new SBox(0b1110, 0b0100, 0b1101, 0b0001, 0b0010, 0b1111, 0b1011, 0b1000, 0b0011, 0b1010, 0b0110, 0b1100, 0b0101, 0b1001, 0b0000, 0b0111);
    private static final int[] pBox = new int[] {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};
    private static final int key = 0b0011_1010_1001_0100_1101_0110_0011_1111;

    public static void main(String[] args) {
        String bitString = FileUtil.readFirstLineFromFile("/chiffre.txt");
        byte[] bytes = BitUtil.readBitString(bitString);
        BitUtil.printByteArrayBitRepresentation(bytes);

        SubstitutionPermutationNetwork spn = new SubstitutionPermutationNetwork(r, n, m, sBox, pBox, key);
        CtrBlockCipherMode blockCipherMode = new CtrBlockCipherMode(spn);

        byte[] plainTextBytes = blockCipherMode.decrypt(bytes);
        System.out.println(new String(plainTextBytes, StandardCharsets.UTF_8));
    }
}
