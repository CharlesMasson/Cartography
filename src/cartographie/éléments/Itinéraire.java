package cartographie.éléments;

import cartographie.positionnementGéographique.Point;
import java.awt.Graphics;
import java.util.List;

/**
 * La classe {@code Itinéraire} représente un itinéraire, c'est-à-dire une liste d'arcs qui se suivent.
 *
 * @author Charles Masson
 */
public class Itinéraire implements Dessinable {

    /**
     * la liste des arcs composants l'itinéraire
     */
    private List<Arc> arcs;

    /**
     * Construit l'itinéraire à partir de la liste d'arcs le composant.
     *
     * @param arcs la liste des arcs composant l'itinéraire.
     */
    public Itinéraire(List<Arc> arcs) {
        this.arcs = arcs;
    }

    /**
     * Renvoie le noeud duquel part cet itinéraire.
     *
     * @return le noeud duquel part cet itinéraire.
     */
    public Noeud getDépart() {
        return arcs.get(0).getDépart();
    }

    /**
     * Renvoie le noeud auquel arrive cet itinéraire.
     *
     * @return le noeud auquel arrive cet itinéraire.
     */
    public Noeud getArrivée() {
        return arcs.get(arcs.size() - 1).getArrivée();
    }

    /**
     * Calcule la longueur totale de cet itinéraire.
     *
     * @return la longueur totale de cet itinéraire en mètres.
     */
    public double calculerLongueur() {
        double longueur = 0;
        for (Arc a : arcs)
            longueur += a.calculerLongueur();
        return longueur;
    }

    /**
     * Calcule le temps de parcous nominal de cet itinéraire (dans les condition normales de circulation).
     *
     * @return le temps de parcours nominal en secondes.
     */
    public double caculerTempsParcoursNominal() {
        double temps = 0;
        for (Arc a : arcs)
            temps += a.calculerTempsParcoursEffectif();
        return temps;
    }

    /**
     * Calcule le temps de parcours actuel de cet itinéraire (dans les conditions actuelles de circulation).
     *
     * @return le temps de parcours actuel en secondes.
     */
    public double calculerTempsParcoursActuel() {
        double temps = 0;
        for (Arc a : arcs)
            temps += a.calculerTempsParcoursEffectif();
        return temps;
    }

    @Override
    public String toString() {
        return "Itin\u00e9raire{" + "arcs=" + arcs + '}';
    }

    @Override
    public void dessiner(Graphics g, Point origine, double échelle) {
        //TODO Programmation en cours.
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
