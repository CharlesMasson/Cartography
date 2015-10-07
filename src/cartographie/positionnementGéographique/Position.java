package cartographie.positionnementGéographique;

import cartographie.éléments.Arc;

/**
 * @author Charles Masson
 */
class Position extends Point {

    private Point point;

    public Position(Latitude latitude, Longitude longitude) {
        point = new Coordonnées(latitude, longitude);
    }

    public Position(double latitudeDegrés, double latitudeMinutes, double latitudeSecondes, double longitudeDegrés, double longitudeMinutes,
            double longitudeSecondes) {
        point = new Coordonnées(latitudeDegrés, latitudeMinutes, latitudeSecondes, longitudeDegrés, longitudeMinutes, longitudeSecondes);
    }

    public Position(Arc arc, double positionRelative) {
        point = new PositionSurArc(arc, positionRelative);
    }

    @Override
    public Latitude getLatitude() {
        return point.getLatitude();
    }

    @Override
    public Longitude getLongitude() {
        return point.getLongitude();
    }
}
