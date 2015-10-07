package cartographie.positionnementGéographique;

import cartographie.gestionDonnées.Ensemble;
import cartographie.gestionDonnées.Identifiable;
import cartographie.gestionDonnées.Zone;
import cartographie.gestionDonnées.Localisable;

/**
 * La classe {@code Point} représente une position géographique sur la Terre.
 *
 * @author Charles Masson
 */
public abstract class Point implements Localisable {

    /**
     * Renvoie la latitude de ce point.
     *
     * @return la latitude de ce point.
     */
    public abstract Latitude getLatitude();

    /**
     * Renvoie la longitude de ce point.
     *
     * @return la longitude de ce point.
     */
    public abstract Longitude getLongitude();

    /**
     * Convertit la position géographique (latitude et longitude) en coordonnées métriques par rapport à une origine.
     *
     * @param origine l'origine par rapport à laquelle sont calculées les coordonnées métriques de ce point.
     * @return les coordonnées en mètres.
     */
    public CoordonnéesMétriques calculerCoordonnéesMétriques(Point origine) {
        return new CoordonnéesMétriques(Terre.rayon(origine) * getLongitude().getAngleRelatifRadians(origine.getLongitude()),
                Terre.RAYON * getLatitude().getAngleRelatifRadians(origine.getLatitude()));
    }

    /**
     * Calcule la distance entre deux points.
     *
     * @param p un point de la Terre.
     * @return la distance en mètres entre ce point et le point spécifié
     */
    public double calculerDistanceMétrique(Point p) {
        return calculerCoordonnéesMétriques(p).calculerDistanceÀLOrigine();
    }

    /**
     * Renvoie le point le plus proche de ce point parmi un ensemble de points.
     * <p>L'algorithme utilisé commence par chercher dans un disque centré en ce point et de rayon spécifié dans le code, puis augmente le rayon du disque
     * jusqu'à trouver des éléments dans le disque.
     *
     * @param <T> le type des éléments de l'ensemble, qui doit hériter de {@link Point} (pour pouvoir calculer la distance à cet élément) et implémenter
     * l'interface {@link Identifiable}.
     * @param ens l'ensemble des points à explorer.
     * @return le point le plus proche de ce point.
     */
    public <T extends Point & Identifiable> T déterminerPlusProchePoint(Ensemble<T> ens) {
        // Rayon initial du disque et incrément du rayon à chaque itération.
        final double INCRÉMENT = 1000;

        double distanceMinimale = 1. / 0.;
        T pointRéalisant = null;

        for (double rayon = INCRÉMENT; distanceMinimale > rayon; rayon += INCRÉMENT)
            for (T p : ens.getÉléments(this, rayon))
                if (calculerDistanceMétrique(p) < distanceMinimale) {
                    pointRéalisant = p;
                    distanceMinimale = calculerDistanceMétrique(p);
                }

        return pointRéalisant;
    }

    @Override
    public String toString() {
        return getLatitude() + " - " + getLongitude();
    }

    @Override
    public Zone calculerZone() {
        return new Zone(getLatitude(), getLatitude(), getLongitude(), getLongitude());
    }

    public static void main(String[] args) {
        Point p = new Coordonnées(48, 52.408, 0, 2, 17.754, 0);
        Point q = new Coordonnées(48, 52.144, 0, 2, 18.585, 0);
        double d = p.calculerDistanceMétrique(q);
        System.out.println(p);
    }
}
