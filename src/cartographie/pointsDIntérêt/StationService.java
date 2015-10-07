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
public class StationService extends PointDIntérêt {

    /**
     * Le nom de l'entreprise gérant la station service.
     */
    private String entreprise;

    //TODO Implémenter la gestion des prix des carburants proposés par la station service.
    /**
     * Construit un objet à partir d'un élément DOM (utile pour le décodage XML).
     *
     * @param e un élément DOM réprésentant une station service
     * @param c la carte à laquelle appartient la station service.
     */
    public StationService(Element e, Carte c) {
        super((Element) e.getElementsByTagName("poi").item(0), c);
        entreprise = e.getElementsByTagName("entreprise").item(0).getTextContent();
    }

    /**
     * Renvoie le nom de l'entreprise gérant cette station service.
     *
     * @return le nom de l'entreprise gérant cette station service.
     */
    public String getEntreprise() {
        return entreprise;
    }

    @Override
    public void dessiner(Graphics g, Point origine, double échelle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Element convertirEnXML(Document doc) {
        Element e = doc.createElement("stationservice");

        e.appendChild(convertirInfoPoIEnXML(doc));

        Element entrepriseElement = doc.createElement("entreprise");
        entrepriseElement.appendChild(doc.createTextNode(entreprise));
        e.appendChild(entrepriseElement);

        return e;
    }
}
