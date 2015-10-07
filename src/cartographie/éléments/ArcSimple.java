package cartographie.éléments;

import cartographie.Carte;
import cartographie.positionnementGéographique.Coordonnées;
import cartographie.positionnementGéographique.CoordonnéesMétriques;
import cartographie.positionnementGéographique.Latitude;
import cartographie.positionnementGéographique.Longitude;
import cartographie.positionnementGéographique.Point;
import cartographie.positionnementGéographique.PositionSurArc;
import cartographie.gestionDonnées.Zone;
import java.awt.Graphics;
import org.w3c.dom.Element;

/**
 * La classe {@code ArcSimple} représente un arc routier quasi-rectiligne.
 *
 * @author Charles Masson
 */
public class ArcSimple extends Arc {

    /**
     * Construit un arc simple.
     *
     * @param id l'identifiant de l'arc.
     * @param départ le noeud duquel part l'arc.
     * @param arrivée le noeud auquel arrive l'arc.
     * @param nbVoies le nombre de voies de l'arc.
     * @param vitesseLimite la vitesse limite légale de circulation sur l'arc.
     * @param coefVitesseNominale la coefficient de vitesse nominale de circulation sur l'arc.
     */
    public ArcSimple(long id, Noeud départ, Noeud arrivée, byte nbVoies, short vitesseLimite, double coefVitesseNominale) {
        super(id, départ, arrivée, nbVoies, vitesseLimite, coefVitesseNominale);
    }

    /**
     * Construit un arc simple à partir d'un élément DOM (utile pour le décodage XML).
     *
     * @param e un élément DOM représentant un arc simple.
     * @param c la carte à laquelle appartient l'arc simple.
     */
    public ArcSimple(Element e, Carte c) {
        this(Long.valueOf(e.getAttribute("id")),
                c.getNoeud(Long.valueOf(e.getElementsByTagName("départ").item(0).getTextContent())),
                c.getNoeud(Long.valueOf(e.getElementsByTagName("arrivée").item(0).getTextContent())),
                Byte.valueOf(e.getElementsByTagName("nbvoies").item(0).getTextContent()),
                Short.valueOf(e.getElementsByTagName("vitesselimite").item(0).getTextContent()),
                Double.valueOf(e.getElementsByTagName("coefvitessenominale").item(0).getTextContent()));

    }

    @Override
    public Zone calculerZone() {
        return new Zone(getDépart(), getArrivée());
    }

    @Override
    public PositionSurArc calculerPointLePlusProche(Point point) {

        // Calcul du projeté du point sur la droite définie par l'arc (qui est en fait un segment)
        CoordonnéesMétriques coordArrivée = getArrivée().calculerCoordonnéesMétriques(getDépart());
        CoordonnéesMétriques coordPoint = point.calculerCoordonnéesMétriques(getDépart());
        double lambda = (coordArrivée.getX() * coordPoint.getX() + coordArrivée.getY() * coordPoint.getY()) / (coordArrivée.getX() * coordArrivée.getX()
                + coordArrivée.getY() * coordArrivée.getY());

        // Renvoie le point sur l'arc le plus proche du projeté, et donc le plus proche du point passé en argument de la méthode
        if (lambda < 0)
            return new PositionSurArc(this, 0);
        if (lambda > 1)
            return new PositionSurArc(this, 1);
        else
            return new PositionSurArc(this, lambda);
    }

    @Override
    public Coordonnées getCoordonnées(double positionRelative) {
        return new Coordonnées(new Latitude(getDépart().getLatitude().getValeurRadians()
                + positionRelative * getArrivée().getLatitude().getAngleRelatifRadians(getDépart().getLatitude())),
                new Longitude(getDépart().getLongitude().getValeurRadians()
                + positionRelative * getArrivée().getLongitude().getAngleRelatifRadians(getDépart().getLongitude())));
    }

    @Override
    public void dessiner(Graphics g, Point origine, double échelle) {
        CoordonnéesMétriques coordDépart = getDépart().calculerCoordonnéesMétriques(origine);
        CoordonnéesMétriques coordArrivée = getArrivée().calculerCoordonnéesMétriques(origine);
        g.drawLine((int) (échelle * coordDépart.getX()), (int) (-échelle * coordDépart.getY()), (int) (échelle * coordArrivée.getX()), (int) (-échelle
                * coordArrivée.getY()));
    }
}
