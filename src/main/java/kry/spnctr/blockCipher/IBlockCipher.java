package kry.spnctr.blockCipher;

public interface IBlockCipher {

    int encrypt(int clearTextBlock);

    int decrypt(int cipherTextBlock);
}
