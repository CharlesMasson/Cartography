package cartographie.gestionDonnées;

/**
 * L'interface {@code Identifiable} représente un élément identifiable, c'est-à-dire un élément auquel est associé un identifiant ({@code long}).
 *
 * @author Charles Masson
 */
public interface Identifiable {

    /**
     * Renvoie l'identifiant de cet identifiable.
     *
     * @return le {@code long} identifiant cet élément.
     */
    public long getID();
}
