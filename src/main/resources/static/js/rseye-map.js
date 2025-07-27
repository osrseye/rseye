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
        this.plane = 0;
        this.currentLayer = null;
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

        this.currentLayer = L.tileLayer('./data/map/0/{x}/{y}.png', {
            bounds: this.bounds,
            noWrap: true,
            tms: true,
            tileSize: 256,
            format: 'image/webp'
            edgeBufferTiles: (this.name == "map") ? 3 : 1
        }).addTo(map);

        return map;
    }

    updateLayer(layer) {
        if(isNaN(layer)) {
            return;
        }

        layer = Math.max(0, Math.min(layer, 3));
        if(this.plane == layer) {
            return
        }

        this.plane = layer;
        let layerPath = './data/map/' + this.plane + '/{x}/{y}.png';

        if(this.currentLayer) {
            this.map.removeLayer(this.currentLayer);
        }

        this.currentLayer = L.tileLayer(layerPath, {
            bounds: this.bounds,
            noWrap: true,
            tms: true,
            tileSize: 256,
            format: 'image/webp'
            edgeBufferTiles: (this.name == "map") ? 3 : 1
        }).addTo(this.map);
    }

    panToFast(x, y, plane) {
        this.updateLayer(plane);
        this.map.panTo(this.map.unproject(L.point(x, y)));
    }

    panTo(x, y, plane) {
        this.updateLayer(plane);

        const duration = 600;
        const target = this.map.unproject(L.point(x, y));
        const start = this.map.getCenter();
        const startTime = performance.now();

        const animate = (time) => {
            const elapsed = time - startTime;
            const t = Math.min(elapsed / duration, 1);
            const lat = start.lat + (target.lat - start.lat) * t;
            const lng = start.lng + (target.lng - start.lng) * t;

            this.map.panTo([lat, lng], { animate: false }); // disable default animation

            if(t < 1) {
                requestAnimationFrame(animate);
            }
        };

        requestAnimationFrame(animate);
    }

    setView(x, y) {
        this.map.setView(this.map.unproject(L.point(x, y)), 8);
    }

    playerMarker(username) {
        return this.playerMarkers.get(username);
    }

    updatePlayerMarker(username, x, y) {
        let marker = this.playerMarkers.get(username);
        if(!marker) {
            return;
        }

        const duration = 600;
        const startLatLng = marker.getLatLng();
        const endLatLng = this.map.unproject(L.point(x, y));
        const startTime = performance.now();

        const animate = (time) => {
            const elapsed = time - startTime;
            const t = Math.min(elapsed / duration, 1);
            const currentLat = startLatLng.lat + (endLatLng.lat - startLatLng.lat) * t;
            const currentLng = startLatLng.lng + (endLatLng.lng - startLatLng.lng) * t;
            marker.setLatLng([currentLat, currentLng]);
            if(t < 1) {
                requestAnimationFrame(animate);
            }
        };

        requestAnimationFrame(animate);
    }

    addPlayerMarker(player, showUsername, map) {
        let iconHtml = showUsername ? "<span class='marker-name'>" + player.username.natural + "</span>" : ""
        let icon = L.divIcon({
            iconSize: [4, 4], // size of the icon
            iconAnchor: [0, 0], // remove any offset
            html: iconHtml
        });
        let marker = L.marker([0,0], {title: player.username.encoded + "-" + map, icon: icon});
        this.playerMarkers.set(player.username.encoded, marker);
        this.playerMarkers.get(player.username.encoded).addTo(this.map);
    }
}