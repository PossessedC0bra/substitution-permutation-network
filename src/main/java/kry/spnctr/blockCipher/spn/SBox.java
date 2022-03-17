package kry.spnctr.blockCipher.spn;

public class SBox {

    private final int[] m_sBox;
    private final int[] m_inverseSBox;

    public SBox(int... substitutions) {
        m_sBox = substitutions;
        m_inverseSBox = new int[m_sBox.length];

        initInverseSBox();
    }

    private void initInverseSBox() {
        for (int i = 0; i < m_sBox.length; i++) {
            m_inverseSBox[m_sBox[i]] = i;
        }
    }

    public int[] get() {
        return m_sBox;
    }

    public int[] getInverse() {
        return m_inverseSBox;
    }
}
