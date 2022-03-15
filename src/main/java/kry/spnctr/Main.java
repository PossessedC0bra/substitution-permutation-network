package kry.spnctr;

import kry.spnctr.spn.SubstitutionPermutationNetwork;
import kry.spnctr.util.FileUtil;

public class Main {

    public static void main(String[] args) {
        String input = FileUtil.readFirstLineFromFile("/chiffre.txt");
        SubstitutionPermutationNetwork spn = SubstitutionPermutationNetwork.init();
    }
}
