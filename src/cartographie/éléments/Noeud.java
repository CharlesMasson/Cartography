package cartographie.éléments;

import cartographie.Carte;
import cartographie.positionnementGéographique.Coordonnées;
import cartographie.positionnementGéographique.CoordonnéesMétriques;
import cartographie.positionnementGéographique.Latitude;
import cartographie.positionnementGéographique.Longitude;
import cartographie.positionnementGéographique.Point;
import cartographie.gestionDonnées.Identifiable;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Element;

/**
 * La classe {@code Noeud} représente un noeud du réseau routier.
 *
 * @author Charles Masson
 */
public class Noeud extends Coordonnées implements Identifiable, Dessinable {

    /**
     * identifiant de ce noeud
     */
    private long id;
    /**
     * ensemble des arcs arrivant à ce noeud
     */
    private Set<Long> idArcsEntrants = new HashSet<>();
    /**
     * ensemble des arcs partant de ce noeud
     */
    private Set<Long> idArcsSortants = new HashSet<>();

    /**
     * Construit un noeud à partir de sa latitude et de sa longitude.
     *
     * @param id l'identifiant du noeud.
     * @param latitude la latitude du noeud.
     * @param longitude la longitude du noeud.
     */
    public Noeud(long id, Latitude latitude, Longitude longitude) {
        super(latitude, longitude);
        this.id = id;
    }

    /**
     * Construit un noeud à partir d'un élément DOM (utile pour le décodage XML).
     *
     * @param e un élément DOM représentant un noeud.
     */
    public Noeud(Element e) {
        this(Long.valueOf(e.getAttribute("id")),
                new Latitude((Element) e.getElementsByTagName("latitude").item(0)),
                new Longitude((Element) e.getElementsByTagName("longitude").item(0)));
    }

    /**
     * Ajoute un arc entrant au noeud.
     *
     * @param id l'identifiant de l'arc entrant.
     */
    public void ajouterIdArcEntrant(long id) {
        idArcsEntrants.add(id);
    }

    /**
     * Ajoute un arc sortant au noeud.
     *
     * @param id l'identifiant de l'arc sortant.
     */
    public void ajouterIdArcSortant(long id) {
        idArcsSortants.add(id);
    }

    /**
     * Renvoie les arcs arrivant sur ce noeud.
     *
     * @param c la carte à laquelle appartient ce noeud.
     * @return l'ensemble des arcs arrivant sur ce noeud.
     */
    public Set<Arc> getArcsEntrants(Carte c) {
        HashSet<Arc> arcsEntrants = new HashSet<>();
        for (long idArc : idArcsEntrants)
            arcsEntrants.add(c.getArc(idArc));
        return arcsEntrants;
    }

    /**
     * Renvoie les arcs partant de ce noeud.
     *
     * @param c la carte à laquelle appartient ce noeud.
     * @return l'ensemble des arcs partant de ce noeud.
     */
    public Set<Arc> getArcsSortants(Carte c) {
        HashSet<Arc> arcsSortants = new HashSet<>();
        for (long idArc : idArcsSortants)
            arcsSortants.add(c.getArc(idArc));
        return arcsSortants;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void dessiner(Graphics g, Point origine, double échelle) {
        CoordonnéesMétriques c = calculerCoordonnéesMétriques(origine);
        g.drawRect((int) (échelle * c.getX()), (int) (-échelle * c.getY()), 1, 1);
    }
}
