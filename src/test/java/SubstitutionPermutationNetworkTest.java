import static org.junit.Assert.assertEquals;

import org.junit.Test;
import kry.spnctr.spn.SubstitutionPermutationNetwork;

public class SubstitutionPermutationNetworkTest {

    @Test
    public void testEncryption() {
        int input = 0b0001_0010_1000_1111;
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();
        assertEquals(0b1010_1110_1011_0100, spn.encrypt(input));
    }

    @Test
    public void testDecryption() {
        int input = 0b1010_1110_1011_0100;
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();
        assertEquals(0b0001_0010_1000_1111, spn.decrypt(input));
    }

    @Test
    public void testSubstitution() {
        int input = 0b1111_0101_1010_1100;
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();
        assertEquals(0b0111_1111_0110_0101, spn.substitute(input, spn.sBox));
    }

    @Test
    public void testInverseSubstitution() {
        int input = 0b0111_1111_0110_0101;
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();
        assertEquals(0b1111_0101_1010_1100, spn.substitute(input, spn.sBoxInverse));
    }

    @Test
    public void testPermutation() {
        int input = 0b1111_0101_1010_1100;
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();
        assertEquals(0b1011_1101_1010_1100, spn.permute(input));
    }

    @Test
    public void testRoundKey0() {
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();

        int key0 = 0b0001_0001_0010_1000;
        assertEquals(key0, spn.getRoundKey(0));
    }

    @Test
    public void testRoundKey1() {
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();

        int key1 = 0b0001_0010_1000_1000;
        assertEquals(key1, spn.getRoundKey(1));
    }

    @Test
    public void testRoundKey2() {
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();

        int key2 = 0b0010_1000_1000_1100;
        assertEquals(key2, spn.getRoundKey(2));
    }

    @Test
    public void testRoundKey3() {
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();

        int key3 = 0b1000_1000_1100_0000;
        assertEquals(key3, spn.getRoundKey(3));
    }

    @Test
    public void testRoundKey4() {
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();

        int key4 = 0b1000_1100_0000_0000;
        assertEquals(key4, spn.getRoundKey(4));
    }
}
