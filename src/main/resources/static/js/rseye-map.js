const tileSize = 256;
const tileColumns = 51;
const tileRows = 178;

class RuneMap {
    constructor(name) {
        this.name = name;
        this.playerMarkers = new Map();
        this.tileColumns = tileColumns;
        this.tileRows = tileRows;
        this.tileSize = tileSize;
        this.bounds = L.latLngBounds([
            L.latLng(0, 0),
            L.latLng(this.tileSize * this.tileRows, this.tileSize * this.tileColumns) // latlng is yx
        ]);
        this.map = this.init();
    }

    init() {
        L.CRS.OSRS = L.extend({}, L.CRS.Simple, {
            transformation: new L.Transformation(1 / this.tileRows, 0, 1 / this.tileColumns, 0)
        });

        const map = L.map(this.name, {
            crs: L.CRS.OSRS,
            minZoom: 8,
            maxZoom: 8,
            zoomControl: false,
            scrollWheelZoom: false,
            attributionControl: false,
            renderer: L.canvas()
        }).setView([0, 0], 8);

        L.tileLayer('./data/map/0/{x}/{y}.png', {
            bounds: this.bounds,
            noWrap: true,
            tms: true
        }).addTo(map);

        return map;
    }

    updateLayer(layer) {
        layer = (layer < 0) ? 0 : (layer > 3) ? 3 : layer; // ensure layer is between 0-3
        L.tileLayer('./data/map/' + layer + '/{x}/{y}.png', {
            bounds: this.bounds,
            noWrap: true,
            tms: true
        }).addTo(this.map);
    }

    panTo(x, y) {
        this.map.panTo(this.map.unproject(L.point(x, y)));
    }

    setView(x, y) {
        this.map.setView(this.map.unproject(L.point(x, y)), 8);
    }

    playerMarker(username) {
        return this.playerMarkers.get(username);
    }

    updatePlayerMarker(username, x, y) {
        let playerMarker = this.playerMarkers.get(username);
        if(playerMarker) {
            playerMarker.setLatLng(this.map.unproject(L.point(x, y)));
        }
    }

    addPlayerMarker(player, map) {
        let icon = L.divIcon({
            iconSize: [4, 4], // size of the icon
            iconAnchor: [0, 0], // remove any offset
            html: "<span class='marker-name'>" + player.username.natural + "</span>"
        });
        let marker = L.marker([0,0], {title: player.username.encoded + "-" + map, icon: icon});
        this.playerMarkers.set(player.username.encoded, marker);
        this.playerMarkers.get(player.username.encoded).addTo(this.map);
    }
}