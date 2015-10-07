package cartographie;

import cartographie.éléments.Itinéraire;
import cartographie.positionnementGéographique.PositionSurArc;
import cartographie.positionnementGéographique.Point;
import cartographie.éléments.PointDIntérêt;
import cartographie.éléments.Noeud;
import cartographie.éléments.ArcComposé;
import cartographie.éléments.ArcSimple;
import cartographie.éléments.Route;
import cartographie.éléments.Arc;
import cartographie.gestionDonnées.Zone;
import cartographie.gestionDonnées.Ensemble;
import cartographie.pointsDIntérêt.Parking;
import cartographie.pointsDIntérêt.StationService;
import dijkstra.GraphGPS;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * La classe {@code Carte} représente une carte complète, incluant tous les éléments de cartographie : arcs, noeuds, routes et points d'intérêt, stockés dans
 * des ensemble (voir {@link Ensemble}).
 * <p>La carte se charge à partir d'un fichier XML spécifié en paramètre du constructeur.
 *
 * @author Charles Masson
 */
public class Carte {

    /**
     * taille par défaut du maillage
     */
    private static final double TAILLE_MAILLAGE_PAR_DÉFAUT = 1000;
    /**
     * constante utilisée par la méthode {@code déterminerCheminOptimal}
     */
    public static final short CHEMIN_LE_PLUS_COURT = 0, CHEMIN_LE_PLUS_RAPIDE_NOMINAL = 1, CHEMIN_LE_PLUS_RAPIDE_ACTUEL = 2;
    /**
     * ensemble des arcs de la carte
     */
    private Ensemble<Arc> arcs;
    /**
     * ensemble des noeuds de la carte
     */
    private Ensemble<Noeud> noeuds;
    /**
     * ensemble des points d'intérêt de la carte
     */
    private Ensemble<PointDIntérêt> pointsDIntérêts;
    /**
     * ensemble des routes de la carte
     */
    private Ensemble<Route> routes;

    /**
     * Construit une carte à partir d'un fichier XML et de la taille de maillage spécifiée.
     *
     * @param xmlCarte un fichier XML représentant une carte.
     * @param taille la taille du maillage, utilisée lors de la création des ensemble d'éléments de cartographie (arcs, noeuds, routes et points d'intérêt).
     */
    public Carte(File xmlCarte, double taille) {
        arcs = new Ensemble<>(taille);
        noeuds = new Ensemble<>(taille);
        pointsDIntérêts = new Ensemble<>(taille);
        routes = new Ensemble<>(taille);
        chargerCarte(xmlCarte);
    }

    /**
     * Construit une carte à partir de deux fichiers XML (un pour la carte et un pour les points d'intérêt) et la taille du maillage spécifiée.
     *
     * @param xmlCarte un fichier XML représentant une carte.
     * @param xmlPoI un fichier XML représentant des points d'intérêt.
     * @param taille la taille du maillage, utilisée lors de la création des ensembles d'éléments de cartographie (arcs, noeuds, routes et points d'intérêt).
     */
    public Carte(File xmlCarte, File xmlPoI, double taille) {
        this(xmlCarte, taille);
        chargerPoI(xmlPoI);
    }

    /**
     * Construit une carte à partir d'un fichier XML. La taille utilisée pour le maillage est celle par défaut.
     *
     * @param xmlCarte un fichier XML représentant une carte.
     */
    public Carte(File xmlCarte) {
        this(xmlCarte, TAILLE_MAILLAGE_PAR_DÉFAUT);
    }

    /**
     * Construit une carte à partir de deux fichiers XML (un pour la carte et un pour les points d'intérêt). La taille utilisée pour le maillage est celle par
     * défaut.
     *
     * @param xmlCarte un fichier XML représentant une carte.
     * @param xmlPoI un fichier XML représentant des points d'intérêt.
     */
    public Carte(File xmlCarte, File xmlPoI) {
        this(xmlCarte, xmlPoI, TAILLE_MAILLAGE_PAR_DÉFAUT);
    }

    /**
     * Charge les éléments de cartographie à partir du fichier XML spécifié.
     *
     * @param fichier un fichier XML représentant une carte.
     */
    private void chargerCarte(File fichier) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fichier);

            doc.getDocumentElement().normalize();

            NodeList noeudsXML = doc.getElementsByTagName("noeud");
            NodeList arcsSimplesListXML = doc.getElementsByTagName("arcsimple");
            NodeList arcsComposésXML = doc.getElementsByTagName("arccomposé");
            NodeList routesXML = doc.getElementsByTagName("route");

            // Ajout des noeuds
            for (int i = 0; i < noeudsXML.getLength(); i++)
                ajouterNoeud(new Noeud((Element) noeudsXML.item(i)));

            // Ajout des arcs
            for (int i = 0; i < arcsSimplesListXML.getLength(); i++)
                ajouterArc(new ArcSimple((Element) arcsSimplesListXML.item(i), this));
            for (int i = 0; i < arcsComposésXML.getLength(); i++)
                ajouterArc(new ArcComposé((Element) arcsComposésXML.item(i), this));

            // Ajout des routes
            for (int i = 0; i < routesXML.getLength(); i++)
                ajouterRoute(new Route((Element) routesXML.item(i), this));

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(Carte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Charge les points d'intérêt à partir du fichier XML spécifié.
     *
     * @param fichier un fichier XML représentant des points d'intérêt.
     */
    public final void chargerPoI(File fichier) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fichier);

            doc.getDocumentElement().normalize();

            NodeList parkingsXML = doc.getElementsByTagName("parking");
            NodeList stationsServiceXML = doc.getElementsByTagName("stationservice");

            // Ajout des parkings
            for (int i = 0; i < parkingsXML.getLength(); i++)
                ajouterPoI(new Parking((Element) parkingsXML.item(i), this));

            // Ajout des stations service
            for (int i = 0; i < stationsServiceXML.getLength(); i++)
                ajouterPoI(new StationService((Element) stationsServiceXML.item(i), this));

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(Carte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Enregistre les points d'intérêt dans le fichier XML spécifié.
     *
     * @param fichier le fichier XLM dans lequel les points d'intérêts doivent être enregistrés.
     */
    public void enregistrerPoI(File fichier) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element racine = doc.createElement("pointsdintérêt");
            doc.appendChild(racine);

            for (PointDIntérêt p : pointsDIntérêts.getÉléments())
                racine.appendChild(p.convertirEnXML(doc));

            // Écriture dans le fichier XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fichier);

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(Carte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Ajoute un arc à la carte.
     *
     * @param a l'arc à ajouter.
     */
    public final void ajouterArc(Arc a) {
        arcs.ajouterÉlément(a);
    }

    /**
     * Ajoute un noeud à la carte.
     *
     * @param n le noeud à ajouter.
     */
    public final void ajouterNoeud(Noeud n) {
        noeuds.ajouterÉlément(n);
    }

    /**
     * Ajouter un point d'intérêt à la carte.
     *
     * @param p le point d'intérêt à ajouter.
     */
    public final void ajouterPoI(PointDIntérêt p) {
        pointsDIntérêts.ajouterÉlément(p);
    }

    /**
     * Ajoute une route à la carte.
     *
     * @param r la route à ajouter.
     */
    public final void ajouterRoute(Route r) {
        routes.ajouterÉlément(r);
    }

    /**
     * Renvoie l'arc de la carte correspondant à l'identifiant spécifié.
     *
     * @param id un identifiant.
     * @return l'arc correspondant à l'identifiant.
     */
    public Arc getArc(long id) {
        return arcs.getÉlément(id);
    }

    /**
     * Renvoie le noeud de la carte correspondant à l'identifiant spécifié.
     *
     * @param id un identifiant.
     * @return le noeud correspondant à l'identifiant.
     */
    public Noeud getNoeud(long id) {
        return noeuds.getÉlément(id);
    }

    /**
     * Renvoie le point d'intérêt de la carte correspondant à l'identifiant spécifié.
     *
     * @param id un identifiant.
     * @return le point d'intérêt correspondant à l'identifiant.
     */
    public PointDIntérêt getPoI(long id) {
        return pointsDIntérêts.getÉlément(id);
    }

    /**
     * Renvoie la route de la carte correspondant à l'identifiant spécifié.
     *
     * @param id un identifiant.
     * @return la route correspondant à l'identifiant.
     */
    public Route getRoute(long id) {
        return routes.getÉlément(id);
    }
    
    public Collection<Arc> getArcs() {
        return arcs.getÉléments();
    }
    
    public Collection<Noeud> getNoeuds() {
        return noeuds.getÉléments();
    }

    public Collection<PointDIntérêt> getPoIs() {
        return pointsDIntérêts.getÉléments();
    }
    
    public Collection<Route> getRoutes() {
        return routes.getÉléments();
    }
    
    public Set<Arc> getArcs(Zone z) {
        return arcs.getÉléments(z);
    }
    
    public Set<Noeud> getNoeuds(Zone z) {
        return noeuds.getÉléments(z);
    }
    
    public Set<PointDIntérêt> getPoIs(Zone z) {
        return pointsDIntérêts.getÉléments(z);
    }
    
    public Set<Route> getRoutes(Zone z) {
        return routes.getÉléments(z);
    }
    
    /**
     * Renvoie la position sur arc la plus proche du point spécifié, parmi toutes les positions sur arc de cette carte.
     *
     * @param point point.
     * @return la position sur arc la plus proche.
     */
    public PositionSurArc calculerPositionSurArcLaPlusProche(Point point) {

        // Taille initiale du disque, puis incrément de cette taille
        final double INCRÉMENT = 1000;

        double distanceMinimale = 1. / 0.;
        PositionSurArc pointRéalisant = null;

        for (double rayon = INCRÉMENT; distanceMinimale > rayon; rayon += INCRÉMENT)
            for (Arc a : arcs.getÉléments(point, rayon))
                if (point.calculerDistanceMétrique(a.calculerPointLePlusProche(point)) < distanceMinimale) {
                    distanceMinimale = point.calculerDistanceMétrique(a.calculerPointLePlusProche(point));
                    pointRéalisant = a.calculerPointLePlusProche(point);
                }

        return pointRéalisant;
    }

    /**
     * Calcule l'itinéraire optimal entre deux noeuds.
     * <p>L'itinéraire optimal correspond à l'itinéraire le plus court (en longueur), le plus rapide dans les conditions normales de circulation ou le plus
     * rapide dans les conditions actuelles de circulation. Ce choix est spécifié par le paramètre {@code type}.
     *
     * @param départ le noeud départ de l'itinéraire.
     * @param arrivée le noeud arrivée de l'itinéraire.
     * @param type un paramètre pouvant prendre trois valeurs :
     * <ul><li>{@code CHEMIN_LE_PLUS_COURT} : l'itinéraire calculé est le plus court (en longueur),
     * <li>{@code CHEMIN_LE_PLUS_RAPIDE_NOMINAL} : l'itinéraire calculé est le plus rapide dans les conditions normales de circulation,
     * <li>{@code CHEMIN_LE_PLUS_RAPIDE_ACTUEL} : l'itinéraire calculé est le plus rapide dans les conditions actuelles de circulation.</ul>
     * @return Renvoie l'itinéraire optimal.
     */
    public Itinéraire déterminerCheminOptimal(Noeud départ, Noeud arrivée, short type) {

        // Définition de la zone à laquelle on se restreint pour la création du graphe sur lequel on appliquera Dijkstra
        final double MARGE = 0.5;
        double distance = départ.calculerDistanceMétrique(arrivée);
        Zone z = new Zone(new Zone(départ, MARGE * distance), new Zone(arrivée, MARGE * distance));

        // Ajout des noeuds
        Set<Noeud> noeudsDansZone = noeuds.getÉléments(z);
        long[] idNoeudsDansZones = new long[noeudsDansZone.size()];
        int i = 0;
        for (Noeud n : noeudsDansZone)
            idNoeudsDansZones[i++] = n.getID();

        // Ajout des arcs
        Set<Arc> arcsDansZone = arcs.getÉléments(z);
        long[] idArcsDansZone = new long[arcsDansZone.size()];
        long[] idDéparts = new long[arcsDansZone.size()];
        long[] idArrivées = new long[arcsDansZone.size()];
        double[] poids = new double[arcsDansZone.size()];
        i = 0;
        for (Arc a : arcsDansZone) {
            idArcsDansZone[i] = a.getID();
            idDéparts[i] = a.getDépart().getID();
            idArrivées[i] = a.getArrivée().getID();
            switch (type) {
                case CHEMIN_LE_PLUS_COURT:
                    poids[i++] = a.calculerLongueur();
                    break;
                case CHEMIN_LE_PLUS_RAPIDE_NOMINAL:
                    poids[i++] = a.calculerTempsParcoursEffectif();
                    break;
                case CHEMIN_LE_PLUS_RAPIDE_ACTUEL:
                    poids[i++] = a.calculerTempsParcoursActuel();
            }
        }

        // Calcul du plus court chemin par l'algorithme de Dijkstra
        GraphGPS g = new GraphGPS(idNoeudsDansZones, idArcsDansZone, idDéparts, idArrivées, poids);
        List<Long> idArcsItinéraire = g.determinerPlusCourtChemin(départ.getID(), arrivée.getID());

        // Recherche des arcs composant l'intinéraire à partir de leur id
        ArrayList<Arc> arcsItinéraire = new ArrayList<>();
        for (Long l : idArcsItinéraire)
            arcsItinéraire.add(getArc(l));

        return new Itinéraire(arcsItinéraire);
    }

    /**
     * Calcule l'itinéraire optimal entre deux noeuds.
     * <p>L'itinéraire optimal correspond à l'itinéraire le plus court (en longueur), le plus rapide dans les conditions normales de circulation ou le plus
     * rapide dans les conditions actuelles de circulation. Ce choix est spécifié par le paramètre {@code type}.
     *
     * @param départ l'identifiant du noeud départ de l'itinéraire.
     * @param arrivée l'identifiant du noeud arrivée de l'itinéraire.
     * @param type un paramètre pouvant prendre trois valeurs :
     * <ul><li>{@code CHEMIN_LE_PLUS_COURT} : l'itinéraire calculé est le plus court (en longueur),
     * <li>{@code CHEMIN_LE_PLUS_RAPIDE_NOMINAL} : l'itinéraire calculé est le plus rapide dans les conditions normales de circulation,
     * <li>{@code CHEMIN_LE_PLUS_RAPIDE_ACTUEL} : l'itinéraire calculé est le plus rapide dans les conditions actuelles de circulation.</ul>
     * @return Renvoie l'itinéraire optimal.
     */
    public Itinéraire déterminerCheminOptimal(long départ, long arrivée, short type) {
        return déterminerCheminOptimal(getNoeud(départ), getNoeud(arrivée), type);
    }

    /**
     * Renvoie le noeud de cette carte le plus proche du point spécifié.
     *
     * @param p un point.
     * @return le noeud le plus proche du point spécifié.
     */
    public Noeud déterminerPlusProcheNoeud(Point p) {
        return p.déterminerPlusProchePoint(noeuds);
    }

    /**
     * Renvoie le point d'intérêt de cette carte le plus proche du point spécifié.
     *
     * @param p un point.
     * @return le point d'intérêt le plus proche du point spécifié.
     */
    public PointDIntérêt déterminerPlusProchePointDIntérêt(Point p) {
        return p.déterminerPlusProchePoint(pointsDIntérêts);
    }

    public static void main(String[] args) {

        // Chargement de la carte
        Carte c = new Carte(new File("D:/Map2.xml"), new File("D:/PoI.xml"), 100);

        //for (Arc a : c.arcs.getÉléments())
        //    System.out.println(a.calculerTempsParcoursActuel());

        /* Test de la méthode de recherche de chemin optimal
         * Le test effectué ici est trivial, mais comme l'implémentation de l'algorithme de Dijkstra a déjà été testée indépendamment, le simple fait que l'on
         * visualise le résultat correct nous assure que la méthode de la classe Carte fonctionne correctement.
         */
        Itinéraire i1 = c.déterminerCheminOptimal(1L, 3L, CHEMIN_LE_PLUS_RAPIDE_ACTUEL);
        Itinéraire i2 = c.déterminerCheminOptimal(3L, 1L, CHEMIN_LE_PLUS_RAPIDE_ACTUEL);
        c.getArc(3).setArcBouché();
        Itinéraire i3 = c.déterminerCheminOptimal(1L, 3L, CHEMIN_LE_PLUS_RAPIDE_ACTUEL);
        System.out.println(i1);
        System.out.println(i2);
        System.out.println(i3);

        // Test de l'enregistrement des points d'intérêts sous format XML.
        c.enregistrerPoI(new File("D:/PoI2.xml"));

        // Test de la méthode de détermination du plus proche noeud.
        System.out.println(c.déterminerPlusProcheNoeud(c.getNoeud(1)).getID());

    }
}
