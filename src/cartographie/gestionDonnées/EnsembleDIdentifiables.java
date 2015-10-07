package cartographie.gestionDonnées;

import java.util.Collection;
import java.util.HashMap;

/**
 * La classe {@code EnsembleDIdentifiables} représente un ensemble d'éléments identifiables. Sa principale utilité est d'implémenter une table de hachage qui
 * permet de récupérer en temps constant un élément de cet ensemble à partir de son identifiant.
 *
 * @author Charles Masson
 * @param <T> le type des objets à stocker dans l'ensemble, qui doit implémenter l'interface {@link Identifiable} (chaque instance de {@code T} a un identifiant
 * unique.
 */
public class EnsembleDIdentifiables<T extends Identifiable> {

    /**
     * la table de hachage stockant tous les objets.
     */
    private HashMap<Long, T> table;

    /**
     * Construit un ensemble vide.
     */
    public EnsembleDIdentifiables() {
        table = new HashMap<>();
    }

    /**
     * Ajoute un élément à l'ensemble.
     *
     * @param élément l'élément à ajouter.
     */
    public void ajouterÉlément(T élément) {
        table.put(élément.getID(), élément);
    }

    /**
     * Renvoie un objet de l'ensemble à partir de son identifiant.
     *
     * @param id l'identifiant de l'objet à récupérer.
     * @return l'élément correspondant.
     */
    public T getÉlément(long id) {
        return table.get(id);
    }

    /**
     * Renvoie tous les objets de l'ensemble.
     *
     * @return une collection contenant tous les éléments de l'ensemble.
     */
    public Collection<T> getÉléments() {
        return table.values();
    }
}
