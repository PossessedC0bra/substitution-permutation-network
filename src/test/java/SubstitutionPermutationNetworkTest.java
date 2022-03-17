import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import kry.spnctr.blockCipher.spn.SubstitutionPermutationNetwork;

public class SubstitutionPermutationNetworkTest {

    private SubstitutionPermutationNetwork spn;
    
    @Before
    public void init() {
        spn = SubstitutionPermutationNetwork.init(0b0001_0001_0010_1000_1000_1100_0000_0000);
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
        
        assertEquals(0b0111_1111_0110_0101, spn.substitute(input, spn.m_sBox));
    }

    @Test
    public void testInverseSubstitution() {
        int input = 0b0111_1111_0110_0101;
        
        assertEquals(0b1111_0101_1010_1100, spn.substitute(input, spn.m_inverseSBox));
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
