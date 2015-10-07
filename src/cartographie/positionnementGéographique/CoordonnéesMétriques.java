package cartographie.positionnementGéographique;

/**
 * La classe {@code CoordonnéesMétriques} représente un couple de coordonnées dans un plan exprimées en mètres.
 *
 * @author Charles Masson
 */
public class CoordonnéesMétriques {

    /**
     * coordonnée
     */
    private double x, y;

    /**
     * Construit l'objet à partir des deux coordonnées.
     *
     * @param x l'abscisse du point.
     * @param y l'ordonnée du point.
     */
    public CoordonnéesMétriques(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Renvoie l'abscisse.
     *
     * @return l'abscisse.
     */
    public double getX() {
        return x;
    }

    /**
     * Renvoie l'ordonnée.
     *
     * @return l'ordonnée.
     */
    public double getY() {
        return y;
    }

    /**
     * Renvoie la distance entre ce point et l'origine du plan.
     *
     * @return la distance entre ce point et l'origine du plan, exprimée en mètres.
     */
    public double calculerDistanceÀLOrigine() {
        return Math.sqrt(x * x + y * y);
    }
}
