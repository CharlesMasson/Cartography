package cartographie.éléments;

import cartographie.Carte;
import cartographie.positionnementGéographique.Coordonnées;
import cartographie.positionnementGéographique.Point;
import cartographie.positionnementGéographique.PositionSurArc;
import cartographie.gestionDonnées.Identifiable;
import cartographie.gestionDonnées.Localisable;
import java.util.HashSet;
import java.util.Set;

/**
 * La classe {@code Arc} représente un arc routier.
 *
 * @author Charles Masson
 */
public abstract class Arc implements Identifiable, Localisable, Dessinable {

    /**
     * l'identifiant de l'arc
     */
    private long id;
    /**
     * l'identifiant de la route associée à l'arc
     */
    private long idRoute;
    /**
     * l'ensemble des identifiants des points d'intérêt associés à l'arc
     */
    private Set<Long> idPointsDIntérêt = new HashSet<>();
    /**
     * le noeud duquel part l'arc
     */
    private Noeud départ;
    /**
     * le noeud auquel arrive l'arc
     */
    private Noeud arrivée;
    /**
     * le nombre de voies de l'arc
     */
    private byte nbVoies;
    /**
     * la vitesse limite légale à laquelle peuvent circuler les véhicules sur cet arc
     */
    private short vitesseLimite;
    /**
     * le coefficient de la vitesse effectif de circulation des véhicules sur l'arc : il représente en fait la vitesse moyenne à laquelle circule habituellement
     * les véhicules sur l'axe (par exemple, {@code 0.9} signifie que les véhicules roulent habituellement à 90 % de la vitesse limite légale sur l'arc).
     */
    private double coefVitesseNominale;
    /**
     * le coefficient de la vitesse nominale actuelle de circulation des véhicules sur l'arc, prenant en compte les conditions actuelles de circulation
     */
    private double coefVitesseActuelle;

    /**
     * Construit un arc routier.
     *
     * @param id l'identifiant de l'arc.
     * @param départ le noeud duquel part l'arc.
     * @param arrivée le noeud auquel arrive l'arc.
     * @param nbVoies le nombre de voies de l'arc.
     * @param vitesseLimite la vitesse limite légale de circulation sur l'arc.
     * @param coefVitesseNominale la coefficient de vitesse nominale de circulation sur l'arc.
     */
    public Arc(long id, Noeud départ, Noeud arrivée, byte nbVoies, short vitesseLimite, double coefVitesseNominale) {
        this.id = id;
        this.départ = départ;
        this.arrivée = arrivée;
        this.nbVoies = nbVoies;
        this.vitesseLimite = vitesseLimite;
        this.coefVitesseNominale = coefVitesseNominale;
        this.coefVitesseActuelle = coefVitesseNominale;
        départ.ajouterIdArcSortant(id);
        arrivée.ajouterIdArcEntrant(id);
    }

    /**
     * Détermine le point sur cet arc le plus proche du point spécifié.
     *
     * @param p un point (une position géographique).
     * @return le point sur l'arc le plus proche du point donné en paramètre.
     */
    public abstract PositionSurArc calculerPointLePlusProche(Point p);

    /**
     * @param positionRelative un double entre 0 et 1 (0 correspond au départ, 1 correspond à l'arrivée)
     * @return les coordonnées d'un point se trouvant sur un arc, à la position relative donnée.
     */
    public abstract Coordonnées getCoordonnées(double positionRelative);

    /**
     * Renvoie le noeud duquel part cet arc.
     *
     * @return le noeud duquel part cet arc.
     */
    public Noeud getDépart() {
        return départ;
    }

    /**
     * Renvoie le noeud auquel arrive cet arc.
     *
     * @return le noeud auquel arrive cet arc.
     */
    public Noeud getArrivée() {
        return arrivée;
    }

    /**
     * Renvoie la route à laquelle appartient cet arc.
     *
     * @param c la carte à laquelle appartient cet arc.
     * @return la route à laquelle appartient cet arc.
     */
    public Route getRoute(Carte c) {
        return c.getRoute(idRoute);
    }

    /**
     * Affecte à cet arc l'identifiant de la route à laquelle appartient cet arc.
     *
     * @param id l'identifiant de la route.
     */
    public void setIdRoute(long id) {
        idRoute = id;
    }

    /**
     * Renvoie les points d'intérêt associés à cet arc.
     *
     * @param c la carte à laquelle appartient cet arc.
     * @return l'ensemble des points d'intérêt associés à cet arc.
     */
    public Set<PointDIntérêt> getPointsDIntérêt(Carte c) {
        HashSet<PointDIntérêt> pointDIntérêt = new HashSet<>();
        for (long idPoI : idPointsDIntérêt)
            pointDIntérêt.add(c.getPoI(idPoI));
        return pointDIntérêt;
    }

    /**
     * Ajoute un point d'intérêt à cet arc.
     *
     * @param id l'identifiant du point d'intérêt à ajouter.
     */
    public void ajouterPointDIntérêt(long id) {
        idPointsDIntérêt.add(id);
    }

    /**
     * Renvoie le nombre de voies de cet arc.
     *
     * @return le nombre de voies de cet arc.
     */
    public byte getNbVoies() {
        return nbVoies;
    }

    /**
     * Calcule la longueur de cet arc.
     *
     * @return la longueur de cet arc.
     */
    public double calculerLongueur() {
        return arrivée.calculerDistanceMétrique(départ);
    }

    /**
     * Calcule le temps de parcours nominal de cet arc (dans les conditions normales de circulation).
     *
     * @return le temps de parcours nominal de cet arc en secondes.
     */
    public double calculerTempsParcoursEffectif() {
        return calculerLongueur() * 3.6 / (vitesseLimite * coefVitesseNominale);
    }

    /**
     * Calcule le temps de parcours actuel de cet arc (dans les conditions actuelles de circulation).
     *
     * @return le temps de parcours actuel de cet arc en secondes.
     */
    public double calculerTempsParcoursActuel() {
        return calculerLongueur() * 3.6 / (vitesseLimite * coefVitesseActuelle);
    }

    /**
     * Redéfinit le coefficient de la vitesse actuelle.
     *
     * @param coef le coefficient de la vitesse actuelle.
     */
    public void setCoefVitesseActuelle(double coef) {
        coefVitesseActuelle = coef;
    }

    /**
     * Redéfinit le coefficient de la vitesse actuelle à partir de la vitesse actuelle de circulation sur cet arc.
     *
     * @param vitesse la vitesse actuelle de circulation sur cet arc.
     */
    public void setVitesseActuelle(double vitesse) {
        coefVitesseActuelle = (short) (vitesse / vitesseLimite);
    }

    /**
     * Redéfinit à 0 le coefficient de la vitesse actuelle de circulation sur cet arc (correspond à un arc bouché).
     */
    public void setArcBouché() {
        coefVitesseActuelle = 0;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
