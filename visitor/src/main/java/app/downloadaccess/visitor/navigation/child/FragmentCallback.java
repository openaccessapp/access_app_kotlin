package app.downloadaccess.visitor.navigation.child;

import app.downloadaccess.resources.models.Place;

public interface FragmentCallback {
    void onPlaceClicked(Place place);
    void onInfoClicked(Place place);
    void onLocationClick();
}
