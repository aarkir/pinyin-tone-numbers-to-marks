/**
 * Short program I created to find which finals contain other finals within them. I ordered the finals such that the for loop encountered
 * finals with other finals inside first, before the shorter particles
 */
public class FindParts {
    public static void main(String[] args) {
        String[] finals = {
                "uang",
                "iang",
                "iong",
                "uai",
                "uan",
                "iao",
                "üan", "ian", "iai",
                "eng", "ang", "ing",
                "üe", "ue", "ui", "uo", "ua", "iu", "ie", "io","ia", "ou", "ao", "ei", "ai",
                "ün", "ong", "un", "in", "er", "en", "an",
                "ü",  "u", "ê", "e", "o", "a", "i", };
        for (int i = 0; i < finals.length; i++) {
            System.out.printf("%-10s: ", finals[i]);
            for (int j = 0; j < finals.length; j++) {
                if (i != j && finals[i].contains(finals[j])) {
                    System.out.print(finals[j]+" ");
                }
            }
            System.out.println();
        }
    }
}
