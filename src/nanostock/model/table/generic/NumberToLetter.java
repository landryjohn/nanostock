package nanostock.model.table.generic;

/**
 *
 * @author medkhd
 */

public class NumberToLetter {

    private String[] group_unit = {"", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf", "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix sept", "dix huit", "dix neuf"};
    private String[] group_diz = {"", "dix", "vingt", "trente", "quarante", "cinquante", "soixante", "", "quatre vingt"};
    private String[] group_cent = {"", "cent", "mille", "million", "milliard"};
    private String[] group_mi = {"", "mille", "million", "milliard"};
    private String montantLettre = "";
    private String montant = "0.00";
    private long partieEnt = 0;
    private int partieFraq = 0;
    private String unite;
    private String sousUnite;

    public String getMontant() {
        return montant;
    }

    public String getSousUnite() {
        return sousUnite;
    }

    public void setSousUnite(String sousUnite) {
        this.sousUnite = sousUnite;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public void setMontant(String montantChiffre) {
        montant = montantChiffre;
        String[] split = montantChiffre.split("\\.");
        this.partieEnt = Long.parseLong(split[0]);
        this.partieFraq = Integer.parseInt(split[1]);
    }

    public String getMontantLettre() {
        return montantLettre;
    }

    private String calculer(int val) {
        int r = val % 100;
        int d = val / 100;
        String s = "";
        if (r < 20) {
            s = group_unit[r];
        } else {
            int r1 = r % 10;
            int r2 = r / 10;
            if (r2 < 7 || r2 == 8) {
                s = group_diz[r2];
                if (r2 == 8 && r1 == 0) {
                    s += "s";
                }
                s += " " + group_unit[r1];
            } else {
                s = group_diz[r2 - 1] + " " + group_unit[r1 + 10];
            }
        }
        if (d != 0) {
            s = group_cent[1] + " " + s;
            if (d > 1) {
                s = group_unit[d] + " " + s;
            }
        }
        return s;
    }

    public void calculer_glob() {
        int i = 0;
        int r = 0;
        long nb = partieEnt;
        String s = "";
        montantLettre = "";
        do {
            r = (int) (nb % 1000);
            nb = nb / 1000;
            if (r != 0) {
                if (r == 1 && i == 1) {
                    s = group_mi[i];
                } else {
                    s = calculer(r) + " " + group_mi[i];
                }
            }
            montantLettre = s + " " + montantLettre;
            i++;
        } while (nb != 0);
        if (partieFraq != 0) {
            montantLettre = montantLettre + " " + unite + "(s) et " + calculer(partieFraq) + " " + sousUnite + "(s)";
        }
    }

    public NumberToLetter() {
        unite = "Euro";
        sousUnite = "centime";
    }

    public NumberToLetter(String unite, String sousUnite) {
        this.unite = unite;
        this.sousUnite = sousUnite;
    }

}
