package kry.blockcipher;

public interface IBlockCipher {

    int encrypt(int clearTextBlock);

    int decrypt(int cipherTextBlock);
}
