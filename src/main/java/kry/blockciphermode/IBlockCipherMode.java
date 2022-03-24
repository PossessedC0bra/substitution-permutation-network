package kry.blockciphermode;

public interface IBlockCipherMode {

    byte[] encrypt(byte[] plainText);

    byte[] decrypt(byte[] cipherText);
}
