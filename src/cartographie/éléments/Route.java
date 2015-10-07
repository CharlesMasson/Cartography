package cartographie.éléments;

import cartographie.Carte;
import cartographie.positionnementGéographique.Point;
import cartographie.gestionDonnées.Zone;
import cartographie.gestionDonnées.Identifiable;
import cartographie.gestionDonnées.Localisable;
import cartographie.positionnementGéographique.CoordonnéesMétriques;
import java.awt.Graphics;
import org.w3c.dom.Element;

/**
 * La classe {@code Route} représente une route, composée d'un arc routier ou de deux arcs routiers complémentaires (le départ de l'un est l'arrivée de
 * l'autre).
 *
 * @author Charles Masson
 */
public class Route implements Dessinable, Identifiable, Localisable {

    /**
     * l'identifiant de la route
     */
    private long id;
    /**
     * le premier arc de la route (éventuellement le seul, si la route est à sens unique)
     */
    private Arc arcAller;
    /**
     * le deuxième arc de la route (éventuellement égal à {@code null}, si la route est à sens unique)
     */
    private Arc arcRetour;
    /**
     * le nom de la route (rue, boulevard...)
     */
    private String nom;

    /**
     * Construit une route.
     *
     * @param id l'identifiant de la route.
     * @param nom le nom de la route.
     * @param arcAller le premier arc.
     * @param arcRetour le deuxième arc (éventuellement {@code null}).
     */
    public Route(long id, String nom, Arc arcAller, Arc arcRetour) {
        this.id = id;
        this.arcAller = arcAller;
        this.arcRetour = arcRetour;
        this.nom = nom;
        arcAller.setIdRoute(id);
        if (arcRetour != null)
            arcRetour.setIdRoute(id);
    }

    /**
     * Construit une route à sens unique.
     *
     * @param id l'identifiant de la route.
     * @param nom le nom de la route.
     * @param arcAller le seul arc de la route.
     */
    public Route(long id, String nom, Arc arcAller) {
        this(id, nom, arcAller, null);
    }

    /**
     * Construit une route à partir d'un élément DOM (utile pour le décodage XML).
     *
     * @param e un élément DOM représentant une route.
     * @param c la carte à laquelle appartient la route.
     */
    public Route(Element e, Carte c) {
        this(Long.valueOf(e.getAttribute("id")),
                e.getElementsByTagName("nom").item(0).getTextContent(),
                c.getArc(Long.valueOf(e.getElementsByTagName("idarcaller").item(0).getTextContent())),
                c.getArc(Long.valueOf(e.getElementsByTagName("idarcretour").item(0).getTextContent())));
    }

    /**
     * Permet de savoir si la route est à sens unique.
     *
     * @return {@code true} si la route est à sens unique, {@code false} sinon.
     */
    public boolean estÀSensUnique() {
        return (arcRetour == null);
    }

    /**
     * Renvoie le nom de la route.
     *
     * @return le nom de la route.
     */
    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return "Route{" + "id=" + id + ", arcAller=" + arcAller + ", arcRetour=" + arcRetour + ", nom=" + nom + '}';
    }

    @Override
    public void dessiner(Graphics g, Point origine, double échelle) {
        arcAller.dessiner(g, origine, échelle);
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public Zone calculerZone() {
        return arcAller.calculerZone();
    }
}
