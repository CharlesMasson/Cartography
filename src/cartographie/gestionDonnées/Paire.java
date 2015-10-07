package cartographie.gestionDonnées;

import java.util.Objects;

/**
 * La classe {@code Paire} représente un couple d'objets.
 *
 * @author Charles Masson
 * @param <T> le type d'objet.
 */
public class Paire<T> {

    /**
     * élément de la paire
     */
    private T premier, second;

    /**
     * Construit la paire à partir de ses deux éléments.
     *
     * @param premier le premier élément.
     * @param second le second élément.
     */
    public Paire(T premier, T second) {
        this.premier = premier;
        this.second = second;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.premier);
        hash = 89 * hash + Objects.hashCode(this.second);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Paire<T> other = (Paire<T>) obj;
        if (!Objects.equals(this.premier, other.premier))
            return false;
        if (!Objects.equals(this.second, other.second))
            return false;
        return true;
    }
}
