package cartographie.éléments;

import cartographie.positionnementGéographique.Point;
import java.awt.Graphics;

/**
 * L'interface {@code Dessinable} représente un objet dessinable, c'est-à-dire un objet qui peut être dessiner dans un {@code Graphics}.
 *
 * @author Charles Masson
 */
public interface Dessinable {

    /**
     * Dessine l'élément dans un {@code Graphics}, à la position correspondant aux paramètres spécifiés.
     *
     * @param g le {@code Graphics} dans lequel doit être dessiné l'objet.
     * @param origine le point géographique correspondant à l'origine du {@code Graphics} (coin supérieur gauche).
     * @param échelle l'échelle en pixels par mètre.
     */
    public void dessiner(Graphics g, Point origine, double échelle);
}
