package cartographie.gestionDonnées;

import cartographie.positionnementGéographique.Point;
import cartographie.positionnementGéographique.Terre;
import java.util.Collection;
import java.util.Set;

/**
 * La clase {@code Ensemble} représente un ensemble d'éléments identifiables et localisables. La structure de l'ensemble tire donc profit de ces deux
 * caractéristiques. Les éléments de l'objet sont simultanément stockés dans un ensemble d'identifiables et un ensemble de localisables et la classe regroupe
 * les méthodes des deux classes {@link EnsembleDIdentifiables} et {@link EnsembleDeLocalisables}.
 *
 * @author Charles Masson
 * @param <T> le type des éléments de l'ensemble, qui doit implémenter les deux interfaces {@link Identifiable} et {@link Localisable}.
 */
public class Ensemble<T extends Identifiable & Localisable> {

    /**
     * ensemble d'identifiables dans lequel sont stockés les éléments de l'ensemble
     */
    private EnsembleDIdentifiables<T> ensId;
    /**
     * ensemble de localisables dans lequel sont stockés les éléments de l'ensemble
     */
    private EnsembleDeLocalisables<T> ensLoc;

    /**
     * Construit un ensemble vide, c'est-à-dire simultanément un ensemble d'identifiables et un ensemble de localisables, dont le maillage a la taille
     * spécifiée.
     *
     * @param nbDivLat taille du maillage.
     * @param nbDivLong taille du maillage.
     */
    public Ensemble(int nbDivLat, int nbDivLong) {
        ensId = new EnsembleDIdentifiables();
        ensLoc = new EnsembleDeLocalisables(nbDivLat, nbDivLong);
    }

    /**
     * Construit un ensemble vide. La taille du maillage de l'ensemble de localisables associé est spécifiée en argument du constructeur.
     *
     * @param taille la taille d'un côté de la maille (suivant les deux directions) au niveau de l'équateur.
     */
    public Ensemble(double taille) {
        this((int) (2 * Math.PI * Terre.RAYON / taille), (int) (Math.PI * Terre.RAYON / taille));
    }

    /**
     * Ajoute un élément à l'ensemble.
     *
     * @param élément l'élément à ajouter.
     */
    public void ajouterÉlément(T élément) {
        ensId.ajouterÉlément(élément);
        ensLoc.ajouterÉlément(élément);
    }

    /**
     * Renvoie un élément à partir de son identifiant.
     *
     * @param id l'identifiant de l'élément à renvoyer.
     * @return l'élément correspondant.
     */
    public T getÉlément(long id) {
        return ensId.getÉlément(id);
    }

    /**
     * Renvoie les éléments contenus dans la zone spécifiée. En réalité, l'ensemble renvoyé contient également d'autres éléments, situés aux alentours de la
     * zone.
     *
     * @param z une zone.
     * @return un ensemble contenant au moins tous les éléments situés dans la zone spécifiée.
     */
    public Set<T> getÉléments(Zone z) {
        return ensLoc.getÉlémentsDansZone(z);
    }

    /**
     * Renvoie les éléments contenus dans le disque spécifié.
     *
     * @param centre centre du disque.
     * @param rayonMétrique rayon du disque en mètres.
     * @return un ensemble contenant au moins tous les éléments situés dans le disque spécifié.
     */
    public Set<T> getÉléments(Point centre, double rayonMétrique) {
        return ensLoc.getÉlémentsDansZone(centre, rayonMétrique);
    }

    /**
     * @return une collection contenant tous les éléments de l'ensemble.
     */
    public Collection<T> getÉléments() {
        return ensId.getÉléments();
    }
}
