package cartographie.positionnementGéographique;

/**
 * La classe {@code Terre} est une classe statique non instantiable contenant des données sur la Terre.
 *
 * @author Charles Masson
 */
public final class Terre {

    /**
     * le rayon de la Terre (il s'agit en fait d'une approximation, car la Terre est aplatie aux pôles)
     */
    public static final double RAYON = 6365000;

    /**
     * Contructeur privé empêchant l'instantiation de la classe.
     */
    private Terre() {
    }

    /**
     * Renvoie le rayon du parallèle à la latitude donnée, c'est-à-dire la distance entre l'axe polaire et un point à la surface de la Terre, de latitude
     * donnée.
     * <p>Il s'agit en fait d'une approximation car le résultat envoyé ne prend pas en compte le fait que la Terre est aplatie aux pôles.
     *
     * @param l une latitude.
     * @return le rayon du parallèle à la latitude donnée.
     */
    public static double rayon(Latitude l) {
        return RAYON * Math.cos(l.getValeurRadians());
    }

    /**
     * Renvoie le rayon du parallèle passant par le point spécifié.
     * <p>Il s'agit en fait d'une approximation car le résultat envoyé ne prend pas en compte le fait que la Terre est aplatie aux pôles.
     *
     * @param p un point de la surface de la Terre.
     * @return le rayon du parallèle passant par ce point.
     */
    public static double rayon(Point p) {
        return rayon(p.getLatitude());
    }
}
