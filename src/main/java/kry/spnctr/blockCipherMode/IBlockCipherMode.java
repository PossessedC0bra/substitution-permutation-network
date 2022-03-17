package kry.spnctr.blockCipherMode;

public interface IBlockCipherMode {

    byte[] encrypt(byte[] plainText);

    byte[] decrypt(byte[] cipherText);
}
