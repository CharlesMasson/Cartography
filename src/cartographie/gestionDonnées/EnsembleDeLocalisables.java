package cartographie.gestionDonnées;

import cartographie.positionnementGéographique.Latitude;
import cartographie.positionnementGéographique.Longitude;
import cartographie.positionnementGéographique.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * La classe {@code EnsembleDeLocalisable} représente un ensemble d'éléments localisables. Cette classe permet de tirer profit du fait que les éléments qu'elle
 * contient sont localisables : elle contient une méthode renvoyant les éléments contenus dans une zone géographique donnée, ce qui rend par la suite plus
 * rapides des opérations, notamment de recherche d'éléments, où il est inutile de considérer tous les éléments de l'ensemble.
 *
 * <p>Concrètement, la classe utilise un maillage de la Terre suivant ses méridiens et ses parallèes. Le nombre de mailles suivant les deux directions est donné
 * par les attributs {@code nbDivLat} et {@code nbDivLong}. À chaque maille est associé un couple d'entiers (ses coordonnées) et un ensemble. Un élément se
 * situant géographiquement dans une maille donnée est stocké dans l'ensemble correspondant à la maille. Tous les ensembles sont stockés dans une table de
 * hachage dont les clés sont les couples de coordonnées correspondants. De cette manière, on accède en temps constant aux éléments d'une zone donnée de la
 * Terre.
 *
 * @author Charles Masson
 * @param <T> le type de données, qui doit implémenter l'interface {@link Localisable}.
 */
public class EnsembleDeLocalisables<T extends Localisable> {

    /**
     * taille du maillage.
     */
    private int nbDivLat, nbDivLong;
    /**
     * table de hachage contenant les ensembles associés à chaque maille.
     */
    private Map<Paire<Integer>, Set<T>> sections;

    /**
     * Construit un ensemble d'éléments localisables vide, avec la taille de maillage spécifiée.
     *
     * @param nbDivLat nombre de mailles suivant la direction d'un méridien.
     * @param nbDivLong nombre de mailles suivant la direction d'un parallèle.
     */
    public EnsembleDeLocalisables(int nbDivLat, int nbDivLong) {
        this.nbDivLat = nbDivLat;
        this.nbDivLong = nbDivLong;
        sections = new HashMap<>();
    }

    /**
     * Renvoie la coordonnée correspondant à une latitude.
     *
     * @param latitude une latitude.
     * @return la coordonnée correspondant à la latitude donnée.
     */
    private int indiceLatitude(Latitude latitude) {
        return (int) (latitude.getValeurRadians() * nbDivLat / (Math.PI / 2));
    }

    /**
     * Renvoie la coordonnée correspondant à une longitude.
     *
     * @param longitude une longitude.
     * @return la coordonnée correspondant à la longitude donnée.
     */
    private int indiceLongitude(Longitude longitude) {
        return (int) (longitude.getValeurRadians() * nbDivLong / Math.PI);
    }

    /**
     * Ajoute un élément à l'ensemble.
     *
     * @param élément l'élément à ajouter.
     */
    public void ajouterÉlément(T élément) {
        Zone zoneÉlément = élément.calculerZone();
        for (int i = indiceLatitude(zoneÉlément.getLatitudeMin()); i <= indiceLatitude(zoneÉlément.getLatitudeMax()); i++)
            for (int j = indiceLongitude(zoneÉlément.getLongitudeMin()); j <= indiceLongitude(zoneÉlément.getLongitudeMax()); j++) {
                Paire p = new Paire(i, j);
                if (sections.get(p) == null)
                    sections.put(p, new HashSet<T>());
                sections.get(p).add(élément);
            }
    }

    /**
     * Renvoie les éléments contenus dans la zone spécifiée. En réalité, l'ensemble renvoyé contient également d'autres éléments, situés aux alentours de la
     * zone.
     *
     * @param z une zone.
     * @return un ensemble contenant au moins tous les éléments situés dans la zone spécifiée.
     */
    public Set<T> getÉlémentsDansZone(Zone z) {
        HashSet<T> éléments = new HashSet<>();
        for (int i = indiceLatitude(z.getLatitudeMin()); i <= indiceLatitude(z.getLatitudeMax()); i++)
            for (int j = indiceLongitude(z.getLongitudeMin()); j <= indiceLongitude(z.getLongitudeMax()); j++)
                if (sections.containsKey(new Paire(i, j)))
                    éléments.addAll(sections.get(new Paire(i, j)));
        return éléments;
    }

    /**
     * Renvoie les éléments contenus dans le disque spécifié.
     *
     * @param centre centre du disque.
     * @param rayonMétrique rayon du disque en mètres.
     * @return un ensemble contenant au moins tous les éléments situés dans le disque spécifié.
     */
    public Set<T> getÉlémentsDansZone(Point centre, double rayonMétrique) {
        return getÉlémentsDansZone(new Zone(centre, rayonMétrique));
    }
}
