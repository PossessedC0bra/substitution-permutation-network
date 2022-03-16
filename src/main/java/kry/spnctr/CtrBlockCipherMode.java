package kry.spnctr;

import kry.spnctr.spn.SubstitutionPermutationNetwork;

public class CtrBlockCipherMode {

    private final int blockSizeInBit = 16;
    private final int blockCipherKey = 0b0011_1010_1001_0100_1101_0110_0011_1111;
    
    private int nonce; // k-1
    
    private SubstitutionPermutationNetwork blockCipher;

    public CtrBlockCipherMode() {
        blockCipher = SubstitutionPermutationNetwork.init();
    }

    public byte[] encrypt(String plainText) {
        char[] letters = plainText.toCharArray();
        if ((letters.length * 8) % blockSizeInBit != 0) {
            // pad
        }

        int counter = 0;
        for (int i = 0; i < letters.length; i = i + 2) {
            int encryptedNonce = blockCipher.encrypt(nonce ^ counter++); // TODO ykl: counter and nonce should only have 16 bit
            int block = ((letters[i] << 8) | letters[i + 1]);
            int encryptedBlock = encryptedNonce ^ block;
        }
    }

    public String decrypt(byte[] cipherText) {
        nonce = (cipherText[0] << 8) | cipherText[1];
        
        int counter = 0;
        for (int i = 2; i < cipherText.length; i = i + 2) {
            int encryptedNonce = blockCipher.encrypt(nonce ^ counter++); // TODO ykl: counter and nonce should only have 16 bit
            int block = (cipherText[i] << 8) | cipherText[i + 1];
            int decryptedText = encryptedNonce ^ block;
        }
    }
}
