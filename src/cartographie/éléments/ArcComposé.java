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
 * La classe {@code ArcComposé} représente un arc routier composé, c'est-à-dire composé de plusieurs tronçons quasi-rectilignes séparés de shape-points.
 *
 * @author Charles Masson
 */
public class ArcComposé extends Arc {

    /**
     * les coordonnées des shape-points
     */
    private Coordonnées[] shapePoints;
    /**
     * les longueurs cumulatives en mètres des portions de cet arc, c'est-à-dire les longueurs entre le départ de cet arc et chacun des différents shape-points
     */
    private double[] longueurCumulative;

    /**
     * Construit un arc composé sans shape-points (constructeur privé).
     *
     * @param id l'identifiant de l'arc.
     * @param départ le noeud duquel part l'arc.
     * @param arrivée le noeud auquel arrive l'arc.
     * @param nbVoies le nombre de voies de l'arc.
     * @param vitesseLimite la vitesse limite légale de circulation sur l'arc.
     * @param coefVitesseNominale la coefficient de vitesse nominale de circulation sur l'arc.
     */
    private ArcComposé(long id, Noeud départ, Noeud arrivée, byte nbVoies, short vitesseLimite, double coefVitesseNominale) {
        super(id, départ, arrivée, nbVoies, vitesseLimite, coefVitesseNominale);
    }

    /**
     * Construit un arc composé.
     *
     * @param id l'identifiant de l'arc.
     * @param départ le noeud duquel part l'arc.
     * @param arrivée le noeud auquel arrive l'arc.
     * @param nbVoies le nombre de voies de l'arc.
     * @param vitesseLimite la vitesse limite légale de circulation sur l'arc.
     * @param coefVitesseNominale la coefficient de vitesse nominale de circulation sur l'arc.
     */
    public ArcComposé(long id, Noeud départ, Noeud arrivée, byte nbVoies, short vitesseLimite, double coefVitesseNominale, Coordonnées... shapePoints) {
        this(id, départ, arrivée, nbVoies, vitesseLimite, coefVitesseNominale);
        this.shapePoints = shapePoints;
        calculerLongueursCumulatives();
    }

    /**
     * Construit un arc composé à partir d'un élément DOM (utile pour le décodage XML).
     *
     * @param e un élément DOM représentant un arc composé.
     * @param c la carte à laquelle appartient l'arc composé.
     */
    public ArcComposé(Element e, Carte c) {
        this(Long.valueOf(e.getAttribute("id")),
                c.getNoeud(Long.valueOf(e.getElementsByTagName("départ").item(0).getTextContent())),
                c.getNoeud(Long.valueOf(e.getElementsByTagName("arrivée").item(0).getTextContent())),
                Byte.valueOf(e.getElementsByTagName("nbvoies").item(0).getTextContent()),
                Short.valueOf(e.getElementsByTagName("vitesselimite").item(0).getTextContent()),
                Double.valueOf(e.getElementsByTagName("coefvitessenominale").item(0).getTextContent()));

        int nbShapePoints = Integer.valueOf(e.getElementsByTagName("nbshapepoints").item(0).getTextContent());
        shapePoints = new Coordonnées[nbShapePoints];

        for (int i = 0; i < nbShapePoints; i++)
            shapePoints[i] = new Coordonnées(new Latitude((Element) e.getElementsByTagName("latitude").item(i)),
                    new Longitude((Element) e.getElementsByTagName("longitude").item(i)));

        calculerLongueursCumulatives();
    }

    /**
     * Renvoie le nombre de points (sommets) de cet arc composé (nombre de shape-points augmenté de 2).
     *
     * @return le nombre de points de cet arc composé.
     */
    private int nbPoints() {
        return shapePoints.length + 2;
    }

    /**
     * Renvoie le point d'indice spécifié de cet arc.
     *
     * @param indice l'indice du point à renvoyer (0 correspond au départ).
     * @return le point correspondant à l'indice.
     */
    private Point getPoint(int indice) {
        if (indice == 0)
            return getDépart();
        if (indice == nbPoints() - 1)
            return getArrivée();
        else
            return shapePoints[indice - 1];
    }

    /**
     * Calcule les longueurs cumulatives de cet arc stockées dans le tableau {@code longueursCumulatives}.
     */
    private void calculerLongueursCumulatives() {
        longueurCumulative = new double[nbPoints()];
        longueurCumulative[0] = 0;
        for (int i = 1; i < nbPoints(); i++)
            longueurCumulative[i] = longueurCumulative[i - 1] + getPoint(i - 1).calculerDistanceMétrique(getPoint(i));
    }

    /**
     * Détermine le point sur la portion de cet arc d'indice spécifié le plus proche du point spécifié.
     *
     * @param p un point.
     * @param indice l'indice de la portion considérée.
     * @return le point sur la portion spécifiée le plus proche du point donné en paramètre.
     */
    private PositionSurArc calculerPointLePlusProcheSurPortion(Point p, int indice) {

        // Calcule le projeté du point sur la droite définie par la portion de l'arc
        CoordonnéesMétriques coordArrivée = getPoint(indice + 1).calculerCoordonnéesMétriques(getPoint(indice));
        CoordonnéesMétriques coordPoint = p.calculerCoordonnéesMétriques(getPoint(indice));
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
    public PositionSurArc calculerPointLePlusProche(Point p) {

        PositionSurArc pointLePlusProche = calculerPointLePlusProcheSurPortion(p, 0);
        double distance = p.calculerDistanceMétrique(pointLePlusProche);

        for (int i = 1; i < nbPoints() - 1; i++) {
            PositionSurArc nouveauPointLePlusProche = calculerPointLePlusProcheSurPortion(p, i);
            double nouvelleDistance = p.calculerDistanceMétrique(nouveauPointLePlusProche);
            if (nouvelleDistance < distance) {
                pointLePlusProche = nouveauPointLePlusProche;
                distance = nouvelleDistance;
            }
        }

        return pointLePlusProche;
    }

    @Override
    public Zone calculerZone() {
        Point[] points = new Point[shapePoints.length + 2];
        points[0] = getDépart();
        points[1] = getArrivée();
        System.arraycopy(shapePoints, 0, points, 2, shapePoints.length);
        return new Zone(points);
    }

    @Override
    public Coordonnées getCoordonnées(double positionRelative) {
        int indice = 0;
        while (longueurCumulative[indice + 1] < positionRelative * longueurCumulative[nbPoints() - 1])
            indice++;
        double r = (positionRelative * longueurCumulative[nbPoints() - 1] - longueurCumulative[indice]) / (longueurCumulative[indice + 1]
                / longueurCumulative[indice]);
        return new Coordonnées(new Latitude(getPoint(indice).getLatitude().getValeurRadians()
                + r * getPoint(indice + 1).getLatitude().getAngleRelatifRadians(getPoint(indice).getLatitude())),
                new Longitude(getPoint(indice).getLongitude().getValeurRadians()
                + r * getPoint(indice + 1).getLongitude().getAngleRelatifRadians(getPoint(indice).getLongitude())));
    }

    @Override
    public void dessiner(Graphics g, Point origine, double échelle) {
        CoordonnéesMétriques coordDépart = getPoint(0).calculerCoordonnéesMétriques(origine);
        for (int i = 0; i < nbPoints(); i++) {
            CoordonnéesMétriques coordArrivée = getPoint(i).calculerCoordonnéesMétriques(origine);
            g.drawLine((int) (échelle * coordDépart.getX()), (int) (-échelle * coordDépart.getY()), (int) (échelle * coordArrivée.getX()), (int) (-échelle
                    * coordArrivée.getY()));
            coordDépart = coordArrivée;
        }
    }
}
