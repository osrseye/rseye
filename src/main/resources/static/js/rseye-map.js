var deltaX = -6650;
var deltaY = -36125;
var cWidth = 12544;
var cHeight = 1424;
var tHeight = cHeight * 32;

var marker = L.icon({
    iconUrl: './data/map/marker.png',
    iconSize: [4, 4], // size of the icon
    iconAnchor: [0, 0] // remove any offset
});

var yx = L.latLng;
var xy = function(x, y) {
    if (L.Util.isArray(x)) { // When doing xy([x, y]);
    return yx(x[1], x[0]);
    }
    return yx(y, x); // When doing xy(x, y);
};

var bounds = L.latLngBounds([
    xy(0, 0),
    xy(256*49, 256*178)
]);

L.CRS.OSRS = L.extend({}, L.CRS.Simple, {
    transformation: new L.Transformation(1 / 178, 0, 1 / 49, 0) // Compute a and c coefficients so that  tile 0/0/0 is from [0, 0] to [4096, 4096]
});

var map = L.map("map", {
    crs: L.CRS.OSRS, // http://leafletjs.com/reference-1.0.3.html#map-crs
    minZoom: 8,
    maxZoom: 8,
    zoomControl: false,
    scrollWheelZoom: false,
    renderer: L.canvas()
}).setView([0,0], 8);

L.tileLayer('./data/map/{x}/{y}.png', {
    bounds: bounds, // http://leafletjs.com/reference-1.0.3.html#gridlayer-bounds
    noWrap: true,
    tms: true
}).addTo(map);

setTimeout(function () {
    map.invalidateSize(true);
    map.setView(map.unproject(L.point(8276, 37552))); // lumbridge castle
}, 100);

display();
connect();

function panWorldMap(x, y) {
    map.panTo(map.unproject(L.point(x, y)));
}

function display() {
    $('.loading-screen').remove();
    $('.content').toggle();
}