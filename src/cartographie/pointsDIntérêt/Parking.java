package cartographie.pointsDIntérêt;

import cartographie.Carte;
import cartographie.positionnementGéographique.Point;
import cartographie.éléments.PointDIntérêt;
import java.awt.Graphics;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * La classe {@code Parking} représente un certain type de points d'intérêt.
 *
 * @author Charles Masson
 */
public class Parking extends PointDIntérêt {

    /**
     * nom du parking
     */
    private String nom;
    /**
     * nombre de places dans le parking
     */
    private short nbPlaces;
    /**
     * nombre de places libres dans le parking
     */
    private short nbPlacesLibres;

    /**
     * Construit un objet à partir d'un élément DOM (utile pour le décodage XML).
     *
     * @param e un élément DOM représentant un parking.
     * @param c la carte à laquelle appartient le parking.
     */
    public Parking(Element e, Carte c) {
        super((Element) e.getElementsByTagName("poi").item(0), c);
        nom = e.getElementsByTagName("nom").item(0).getTextContent();
        nbPlaces = Short.valueOf(e.getElementsByTagName("nbplaces").item(0).getTextContent());
    }

    /**
     * Renvoie le nom de ce parking.
     *
     * @return le nom de ce parking.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Renvoie le nombre de places dans ce parking.
     *
     * @return le nombre de places dans ce parking.
     */
    public short getNbPlaces() {
        return nbPlaces;
    }

    /**
     * Renvoie le nombre de places libres dans ce parking.
     *
     * @return le nombre de places libres dans ce parking.
     */
    public short getNbPlacesLibres() {
        return nbPlacesLibres;
    }

    /**
     * Permet de savoir si le parking est complet.
     *
     * @return {@code true} si le parking est complet, {@code false} sinon.
     */
    public boolean estComplet() {
        return nbPlacesLibres == 0;
    }

    /**
     * Spécifie le nombre de places actuellement libres dans le parking.
     *
     * @param nbPlacesLibres le nombre de places actuellement libres.
     */
    public void setNbPlacesLibres(short nbPlacesLibres) {
        this.nbPlacesLibres = nbPlacesLibres;
    }

    /**
     * Prend en compte l'arrivée d'une voiture et décrémente le nombre de places libres.
     */
    public void comptabiliserVoitureEntrante() {
        nbPlacesLibres--;
    }

    /**
     * Prend en compte la sortie d'une voiture et incrémente le nombre de places libres.
     */
    public void comptabiliserVoitureSortante() {
        nbPlacesLibres++;
    }

    @Override
    public void dessiner(Graphics g, Point origine, double échelle) {
        //TODO Programmation en cours
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Element convertirEnXML(Document doc) {
        Element e = doc.createElement("parking");

        e.appendChild(convertirInfoPoIEnXML(doc));

        Element nomElement = doc.createElement("nom");
        nomElement.appendChild(doc.createTextNode(nom));
        e.appendChild(nomElement);

        Element nbplaceseElement = doc.createElement("nbplaces");
        nbplaceseElement.appendChild(doc.createTextNode(String.valueOf(nbPlaces)));
        e.appendChild(nbplaceseElement);

        return e;
    }
}
