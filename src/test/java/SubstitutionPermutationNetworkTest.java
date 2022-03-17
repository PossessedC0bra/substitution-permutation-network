import kry.spnctr.blockCipher.spn.SBox;
import kry.spnctr.blockCipher.spn.SubstitutionPermutationNetwork;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubstitutionPermutationNetworkTest {

    private static final int r = 4;
    private static final int n = 4;
    private static final int m = 4;
    private static final SBox sBox = new SBox(0b1110, 0b0100, 0b1101, 0b0001, 0b0010, 0b1111, 0b1011, 0b1000, 0b0011, 0b1010, 0b0110, 0b1100, 0b0101, 0b1001, 0b0000, 0b0111);
    private static final int[] pBox = new int[]{0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};
    private static final int key = 0b0001_0001_0010_1000_1000_1100_0000_0000;

    private SubstitutionPermutationNetwork spn;

    @Before
    public void init() {
        spn = SubstitutionPermutationNetwork.init(r, n, m, sBox, pBox, key);
    }

    @Test
    public void testEncryption() {
        int input = 0b0001_0010_1000_1111;

        assertEquals(0b1010_1110_1011_0100, spn.encrypt(input));
    }

    @Test
    public void testDecryption() {
        int input = 0b1010_1110_1011_0100;
        
        assertEquals(0b0001_0010_1000_1111, spn.decrypt(input));
    }

    @Test
    public void testSubstitution() {
        int input = 0b1111_0101_1010_1100;

        assertEquals(0b0111_1111_0110_0101, spn.substitute(input, sBox.get()));
    }

    @Test
    public void testInverseSubstitution() {
        int input = 0b0111_1111_0110_0101;

        assertEquals(0b1111_0101_1010_1100, spn.substitute(input, sBox.getInverse()));
    }

    @Test
    public void testPermutation() {
        int input = 0b1111_0101_1010_1100;
        
        assertEquals(0b1011_1101_1010_1100, spn.permute(input));
    }

    @Test
    public void testRoundKey0() {
        int key0 = 0b0001_0001_0010_1000;
        assertEquals(key0, spn.getRoundKey(0));
    }

    @Test
    public void testRoundKey1() {
        int key1 = 0b0001_0010_1000_1000;
        assertEquals(key1, spn.getRoundKey(1));
    }

    @Test
    public void testRoundKey2() {
        int key2 = 0b0010_1000_1000_1100;
        assertEquals(key2, spn.getRoundKey(2));
    }

    @Test
    public void testRoundKey3() {
        int key3 = 0b1000_1000_1100_0000;
        assertEquals(key3, spn.getRoundKey(3));
    }

    @Test
    public void testRoundKey4() {
        int key4 = 0b1000_1100_0000_0000;
        assertEquals(key4, spn.getRoundKey(4));
    }
}
