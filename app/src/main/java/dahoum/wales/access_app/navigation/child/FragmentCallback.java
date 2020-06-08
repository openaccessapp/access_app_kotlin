package dahoum.wales.access_app.navigation.child;

import dahoum.wales.access_app.models.Place;

public interface FragmentCallback {
    void onPlaceClicked(Place place);
    void onInfoClicked(Place place);
    void onLocationClick();
}
