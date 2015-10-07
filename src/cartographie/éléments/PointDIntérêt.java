package cartographie.éléments;

import cartographie.Carte;
import cartographie.positionnementGéographique.PositionSurArc;
import cartographie.gestionDonnées.Identifiable;
import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * La classe {@code PointDIntérêt} représente un point d'intérêt associé à un arc ou plusieurs arcs. Un point d'intérêt se situe nécessairement sur un arc.
 *
 * @author Charles Masson
 */
public abstract class PointDIntérêt extends PositionSurArc implements Dessinable, Identifiable {

    /**
     * l'identifiant de l'arc
     */
    private long id;
    /**
     * l'ensemble des arcs associés à l'arc
     */
    private Set<Arc> arcsAssociés = new HashSet<>();

    /**
     * Construit un point d'intérêt.
     *
     * @param id l'identifiant du point d'intérêt.
     * @param arc l'arc sur lequel se situe le point d'intérêt.
     * @param positionRelative la position relative du point d'intérêt sur l'arc.
     * @param arcsAssociés les arcs associés au point d'intérêt (contenant généralement au moins l'arc sur lequel il se situe).
     */
    public PointDIntérêt(long id, Arc arc, double positionRelative, Arc... arcsAssociés) {
        super(arc, positionRelative);
        this.id = id;
        for (Arc a : arcsAssociés)
            a.ajouterPointDIntérêt(id);
    }

    /**
     * Construit un point d'intérêt à partir d'un élément DOM (utile pour le décodage XML).
     *
     * @param e un élément DOM représentant un point d'intérêt.
     * @param c la carte à laquelle appartient le point d'intérêt.
     */
    public PointDIntérêt(Element e, Carte c) {
        this(Long.valueOf(e.getAttribute("id")),
                c.getArc(Long.valueOf(e.getElementsByTagName("idarc").item(0).getTextContent())),
                Double.valueOf(e.getElementsByTagName("positionrelative").item(0).getTextContent()));

        NodeList idArcsAssociésXML = e.getElementsByTagName("idarcassocié");
        for (int i = 0; i < idArcsAssociésXML.getLength(); i++) {
            Arc a = c.getArc(Long.valueOf(idArcsAssociésXML.item(i).getTextContent()));
            arcsAssociés.add(a);
            a.ajouterPointDIntérêt(id);
        }
    }

    /**
     * Convertit ce point d'intérêt en objet de type {@code Element} (DOM), qui sera ensuite utilisé pour l'enregistrement au formal XML.
     *
     * @param doc le document DOM auquel doit appartenir l'élément.
     * @return l'élément DOM représentant ce point d'intérêt.
     */
    public abstract Element convertirEnXML(Document doc);

    /**
     * Convertit les informations générales de ce point d'intérêt (position, arcs associés, indépendantes du type de point d'intérêt) en objet de type
     * {@code Element} (DOM), qui sera ensuite utilisé pour l'enregistrement au formal XML.
     *
     * @param doc le document DOM auquel doit appartenir l'élément.
     * @return l'élément DOM de ce points d'intérêt.
     */
    public Element convertirInfoPoIEnXML(Document doc) {
        Element e = doc.createElement("poi");
        e.setAttribute("id", String.valueOf(id));

        Element idarcElement = doc.createElement("idarc");
        idarcElement.appendChild(doc.createTextNode(String.valueOf(getArc().getID())));
        e.appendChild(idarcElement);

        Element positionrelativeElement = doc.createElement("positionrelative");
        positionrelativeElement.appendChild(doc.createTextNode(String.valueOf(getPositionRelative())));
        e.appendChild(positionrelativeElement);

        for (Arc arc : arcsAssociés) {
            Element idarcassociéElement = doc.createElement("idarcassocié");
            idarcassociéElement.appendChild(doc.createTextNode(String.valueOf(arc.getID())));
            e.appendChild(idarcassociéElement);
        }

        return e;
    }

    @Override
    public long getID() {
        return id;
    }
}
