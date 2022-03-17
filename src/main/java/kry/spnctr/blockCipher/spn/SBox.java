package kry.spnctr.blockCipher.spn;

public class SBox {
    private int[] m_sBox;
    private int[] m_inverseSBox;

    public SBox(int... substitutions) {
        m_sBox = substitutions;

        initInverseSBox();
    }

    private void initInverseSBox() {
        m_inverseSBox = new int[m_sBox.length];
        for (int i = 0; i < m_sBox.length; i++) {
            m_inverseSBox[m_sBox[i]] = i;
        }
    }

    public int getSubstitution(int block) {
        return m_sBox[block];
    }

    public int getInverseSubstitution(int block) {
        return m_inverseSBox[block];
    }
}
