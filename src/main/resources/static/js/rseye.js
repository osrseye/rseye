var mapController = document.getElementById('map');
var mapMouse = Array.from({length: 3}, i => i = false);
var mapPlaneUp = document.getElementById('mapPlaneUp');
var mapPlaneDown = document.getElementById('mapPlaneUp');
var mapPlane = 0;
var followedPlayer;

mapController.addEventListener('mousedown', (e) => {
    if(e.button !== 0 && e.button !== 1 && e.button !== 2) { // middle click || right click
        return;
    }
    mapMouse[e.button] = true;
});

mapController.addEventListener('mouseup', (e) => {
    if(e.button !== 0 && e.button !== 1 && e.button !== 2) { // middle click || right click
        return;
    }
    mapMouse[e.button] = false;
});

mapController.addEventListener('mousemove', (e) => {
    if(mapMouse[0] || mapMouse[1] || mapMouse[2]) {
        if(followedPlayer != null) {
            followedPlayer = null;
            $('#followed-player').html("<span class='title-text'>NOT FOLLOWING</span>");
            $('#followed-player-ui').addClass("ui-disabled");
            $('#followed-player-data').html("");
            $('#followed-player-ui > .container-visible').removeClass("container-visible");
        }
    }
});

$('#map-plane-up').click(function() {
    if(mapPlane < 3) {
        mapPlane += 1
        L.tileLayer('./data/map/'+mapPlane+'/{x}/{y}.png', {
            bounds: bounds, // http://leafletjs.com/reference-1.0.3.html#gridlayer-bounds
            noWrap: true,
            tms: true
        }).addTo(map);
    }
});

$('#map-plane-down').click(function() {
    if(mapPlane > 0) {
        mapPlane += -1
        L.tileLayer('./data/map/'+mapPlane+'/{x}/{y}.png', {
            bounds: bounds, // http://leafletjs.com/reference-1.0.3.html#gridlayer-bounds
            noWrap: true,
            tms: true
        }).addTo(map);
    }
});

$(document).on('input','[class~=bank-input]',function() {
    var value = $(this).val().toLowerCase();
    $(this).next("ul").find("li").each(function() {
        $(this).toggle($(this).attr('title').toLowerCase().includes(value));
    });
});

function updatePosition(player) {
    var x = player.offsetPosition.x;
    var y = player.offsetPosition.y;
    var plane = player.position.plane
    var planeUpdated = ($("#"+player.usernameEncoded).attr("aria-plane") != plane)

    // store player position on their div
    $("#"+player.usernameEncoded).attr("aria-x", x);
    $("#"+player.usernameEncoded).attr("aria-y", y);
    $("#"+player.usernameEncoded).attr("aria-plane", plane);

    // update world map
    const smap = $("#map-status-"+player.usernameEncoded)
    if(followedPlayer != null && followedPlayer.attr("aria-username-sane") === smap.attr("aria-username-sane")) {
        if(planeUpdated) {
            mapPlane = plane;
            L.tileLayer('./data/map/'+plane+'/{x}/{y}.png', {
                bounds: bounds, // http://leafletjs.com/reference-1.0.3.html#gridlayer-bounds
                noWrap: true,
                tms: true
            }).addTo(map);
        }
        panWorldMap(x, y);
    }
    var marker = player.usernameEncoded + "WorldmapMarker";
    window[marker].setLatLng(map.unproject(L.point(x, y)));

    // update player minimap
    var minimap = player.usernameEncoded + "Minimap";
    var pan = player.usernameEncoded + "MinimapPan";
    var minimarker = player.usernameEncoded + "MinimapMarker";
    if(planeUpdated) {
        L.tileLayer('./data/map/'+plane+'/{x}/{y}.png', {
            bounds: bounds,
            noWrap: true,
            tms: true
        }).addTo(window[minimap]);
    }
    window[pan](x, y);
    window[minimarker].setLatLng(window[minimap].unproject(L.point(x, y)));
}

function updatePlayerContainer(container, player, data) {
    $("#"+player.usernameEncoded).find('[data-container="'+container+'"]').tooltip('dispose'); // removes tooltip before replacing dom (or it'll stick)
    $("#"+player.usernameEncoded).find(container).replaceWith(data); // replace dom
    if(followedPlayer != null && followedPlayer.attr("aria-username-sane") === player.usernameEncoded) {
        const obj = $('#followed-player-' + player.usernameEncoded).find(container);
        const style = obj.attr('style');
        obj.replaceWith(data);
        $('#followed-player-' + player.usernameEncoded).find(container).attr("style", style); // have to get dom again since obj will still contain old data even after .replaceWith()
    }
    $('[data-toggle="tooltip"]').tooltip()
}

$(document).on('click','[class~=locator]',function() {
    followedPlayer = $(this);
    $('#followed-player').html("<span class='title-text'>FOLLOWING</span><br><span class='feed-player'>" + $(this).attr("aria-username") + "</span>");

    // followed player
    const playerDiv = $('#'+$(this).attr("aria-username-sane"));
    $('#inventory-button').addClass("container-visible");
    $('#equipment-button, #stats-button, #quests-button, #bank-button').removeClass("container-visible");
    $('#followed-player-ui').removeClass("ui-disabled");
    $('#followed-player-data').html("<div id='followed-player-" + $(this).attr("aria-username-sane") + "'></div>");
    const followDiv = $('#followed-player-' + $(this).attr("aria-username-sane"));
    followDiv.append(playerDiv.find(".equipment-container").clone(true).toggle());
    followDiv.append(playerDiv.find(".inventory-container").clone(true));
    followDiv.append(playerDiv.find(".stats-container").clone(true).toggle());
    followDiv.append(playerDiv.find(".quests-container").clone(true).toggle());
    followDiv.append(playerDiv.find(".bank-container").clone(true).toggle());
    followDiv.find(".bank-container").find(":input").val("").trigger("input"); // stops search input being cloned and resets any toggles
    $('[data-toggle="tooltip"]').tooltip() // initialise tooltips

    // pan map to followed player
    panWorldMap(playerDiv.attr("aria-x"), playerDiv.attr("aria-y"));
});

function clearFeed() {
    $('[data-toggle="tooltip"]').tooltip();
    if($('.feed-item').length > 5) {
        $('.update-feed').find(".feed-item:last").fadeOut("slow", function() {
            $('[data-container=".feed-item"]').tooltip('dispose');
            $(this).remove();
            clearFeed(); // calls function recursively AFTER the element has been removed
        });
    }
}

////////////////////////////////////////////////////////////////////

function openPage(pageName, button) {
    // Hide all elements with class="tabcontent" by default */
    var tabcontent = document.getElementsByClassName("tabcontent");
    for(var i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Remove the background color of all tablinks/buttons
    var tablinks = document.getElementsByClassName("tablink");
    for(var i = 0; i < tablinks.length; i++) {
        tablinks[i].style.backgroundColor = "";
    }

    // Show the specific tab content
    var tabsToDisplay = document.getElementsByClassName(pageName);
    for(var i = 0; i < tabsToDisplay.length; i++) {
        tabsToDisplay[i].style.display = "flex";
    }

    // Add the specific color to the button used to open the tab content
    button.style.backgroundColor = '#222222';
}