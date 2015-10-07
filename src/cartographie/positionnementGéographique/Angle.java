package cartographie.positionnementGéographique;

import org.w3c.dom.Element;

/**
 * La classe {@code Angle} représente un angle.
 *
 * @author Charles Masson
 */
public class Angle {

    /**
     * valeur toujours comprise entre -<i>pi</i> exclu et <i>pi</i>.
     */
    private double valeurRadians;

    /**
     * Construit un angle à partir d'une valeur d'angle en radians.
     *
     * @param valeurRadians un angle en radian
     */
    public Angle(double valeurRadians) {
        this.valeurRadians = normaliser(valeurRadians);
    }

    /**
     * Construit un angle à partir d'une valeur d'angle en degrés, minutes et secondes.
     *
     * @param valeurDegrés la part en degrés de l'angle
     * @param valeurMinutes la part en minutes de l'angle.
     * @param valeurSecondes la part en secondes de l'angle.
     */
    public Angle(double valeurDegrés, double valeurMinutes, double valeurSecondes) {
        valeurRadians = (valeurDegrés + valeurMinutes / 60 + valeurSecondes / 3600) * Math.PI / 180;
    }

    /**
     * Construit un angle à partir d'un élément DOM (utile pour le décodage XML).
     *
     * @param e un élément DOM représentant un angle.
     */
    public Angle(Element e) {
        this(Double.valueOf(e.getElementsByTagName("degrés").item(0).getTextContent()),
                e.getElementsByTagName("minutes").item(0) == null ? 0 : Double.valueOf(e.getElementsByTagName("minutes").item(0).getTextContent()),
                e.getElementsByTagName("secondes").item(0) == null ? 0 : Double.valueOf(e.getElementsByTagName("secondes").item(0).getTextContent()));
    }

    /**
     * Normalise un angle donné en radians.
     *
     * @param angle valeur donnée en radians.
     * @return la valeur entre -<i>pi</i> exclu et <i>pi</i> correspondant à ce même angle.
     */
    private static double normaliser(double angle) {
        return (angle + Math.PI) % (2 * Math.PI) + (angle >= -Math.PI ? -Math.PI : Math.PI);
    }

    /**
     * Soustrait un angle à cet angle.
     *
     * @param référence l'angle que l'on soustrait.
     * @return le résultat de la soustraction, sous forme d'angle.
     */
    public double getAngleRelatifRadians(Angle référence) {
        return normaliser(valeurRadians - référence.valeurRadians);
    }

    /**
     * Renvoie la valeur de cet angle en radians.
     *
     * @return la valeur (double) de cet angle en radians.
     */
    public double getValeurRadians() {
        return valeurRadians;
    }

    /**
     * Renvoie la valeur de cet angle en degrés.
     *
     * @return la valeur (double) de cet angle en degrés.
     */
    public double getValeurDegrés() {
        return Math.toDegrees(valeurRadians);
    }

    /**
     * Renvoie le cosinus de cet angle.
     *
     * @return le cosinus de cet angle.
     */
    public double cos() {
        return Math.cos(valeurRadians);
    }
}
