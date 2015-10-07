package cartographie.positionnementGéographique;

import cartographie.éléments.Arc;

/**
 * La classe {@code PositionSurArc} représente un point géographique appartenant à un arc. Il est donc déterminé par cet arc et sa position relative sur cet
 * arc.
 *
 * @author Charles Masson
 */
public class PositionSurArc extends Point {

    /**
     * l'arc sur lequel se situe le point
     */
    private Arc arc;
    /**
     * la position relative du point sur l'arc (0 correspond au départ de l'arc, 1 correspond à l'arrivée)
     */
    private double positionRelative;
    /**
     * booléen valant {@code true} si les attributs {@code latitude} et {@code longitude} sont à jour, {@code false} sinon
     */
    private boolean coordonnéesCalculées;
    /**
     * latitude du point, calculée seulement lorsque cela est nécessaire
     */
    private Latitude latitude;
    /**
     * longitude du point, calculée seulement lorsque cela est nécessaire
     */
    private Longitude longitude;

    /**
     * Construit un point à partir d'un arc et de la position relative du point sur l'arc.
     *
     * @param arc l'arc sur lequel se trouve le point.
     * @param positionRelative la position relative du point sur l'arc.
     */
    public PositionSurArc(Arc arc, double positionRelative) {
        this.arc = arc;
        this.positionRelative = positionRelative;
        coordonnéesCalculées = false;
    }

    /**
     * Calcule la latitude et la longitude de ce point et les stocke dans les attributs respectifs.
     */
    private void calculerCoordonnées() {
        Coordonnées c = arc.getCoordonnées(positionRelative);
        latitude = c.getLatitude();
        longitude = c.getLongitude();
        coordonnéesCalculées = true;
    }

    /**
     * Renvoie l'arc sur lequel se situe ce point.
     *
     * @return l'arc sur lequel se situe ce point.
     */
    public Arc getArc() {
        return arc;
    }

    /**
     * Renvoie la position relative de ce point sur l'arc.
     *
     * @return la position relative de ce point sur l'arc.
     */
    public double getPositionRelative() {
        return positionRelative;
    }

    @Override
    public Latitude getLatitude() {
        if (!coordonnéesCalculées)
            calculerCoordonnées();
        return latitude;
    }

    @Override
    public Longitude getLongitude() {
        if (!coordonnéesCalculées)
            calculerCoordonnées();
        return longitude;
    }
}
