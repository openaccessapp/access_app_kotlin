package app.downloadaccess.places.navigation.child;

import app.downloadaccess.places.models.Place;

public interface FragmentCallback {
    void onPlaceClicked(Place place);
    void onAddPlaceClicked(Place place);
    void editPlace(Place place);
}
