package cartographie.gestionDonnées;

/**
 * L'interface {@code Localisable} représente un élément localisable, c'est-à-dire un élément qui a une position géographique.
 *
 * @author Charles Masson
 */
public interface Localisable {

    /**
     * @return une zone (souvent la plus petite) contenant entièrement l'élément localisable.
     */
    public Zone calculerZone();
}
