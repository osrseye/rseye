var worldMap = new RuneMap('map');

var playerMinimaps = new Map();
var playerMinimapMarkers = new Map();

var mapController = document.getElementById('map');
var mapMouse = [false, false, false];
var followedPlayer;

setTimeout(function () {
    worldMap.map.invalidateSize(true);
    worldMap.panToFast(8244, 36716); // varrock & falador
}, 100);

function display() {
    $('.loading-screen').remove();
    $('.content').toggle();
}

display();
connect();

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
            $('#followed-player-ui').addClass("hidden");
            $('#player-data').find("[class^=player]").addClass("hidden");
            $('#player-data').find("[class=status]").addClass("hidden");
            $('.pillars').addClass("hidden");
        }
    }
});

$('#map-plane-up').click(function() {
    worldMap.updateLayer(worldMap.plane + 1);
});

$('#map-plane-down').click(function() {
    worldMap.updateLayer(worldMap.plane - 1);
});

$(document).on('input','[class~=bank-input]',function() {
    var value = $(this).val().toLowerCase();
    $(this).next("ul").find("li").each(function() {
        $(this).toggle($(this).attr('title').toLowerCase().includes(value));
    });
});

function updatePosition(player) {
    var x = player.position.offx;
    var y = player.position.offy;
    var plane = player.position.plane;
    var planeUpdated = ($("#"+player.username.encoded).attr("aria-plane") != plane);

    // store player position on their div
    $("#"+player.username.encoded).attr("aria-x", x);
    $("#"+player.username.encoded).attr("aria-y", y);
    $("#"+player.username.encoded).attr("aria-plane", plane);

    // update world map
    if(followedPlayer != null && followedPlayer.attr("aria-username-sane") === $("#"+player.username.encoded).attr("aria-username-sane")) {
        worldMap.panTo(x, y, plane);
    }
    worldMap.updatePlayerMarker(player.username.encoded, x, y);

    // update player minimap
    minimap = playerMinimaps.get(player.username.encoded);
    if(minimap) {
        minimap.panTo(x, y, plane);
        minimap.updatePlayerMarker(player.username.encoded, x, y);
    }
}

function updatePlayerContainer(container, player, data) {
    $("#"+player.username.encoded+'-container').find(container).replaceWith(data); // replace dom
    if(followedPlayer != null && followedPlayer.attr("aria-username-sane") === player.username.encoded) {
        const obj = $('#followed-player-' + player.username.encoded).find(container);
        const style = obj.attr('style');
        obj.replaceWith(data);
        $('#followed-player-' + player.username.encoded).find(container).attr("style", style); // have to get dom again since obj will still contain old data even after .replaceWith()
    }
}

$(document).on('click','[class~=locator]', function() {
    $('#followed-player').html("<span class='title-text'>FOLLOWING</span><br><span class='feed-player'>" + $(this).attr("aria-username") + "</span>");

    followedPlayer = $(this);

    $('.player-div').hide();

    // followed player
    $('#'+followedPlayer.attr("aria-username-sane") + "-container").toggle();

    $('#inventory-button').addClass("container-visible");
    $('#equipment-button, #skills-button, #quests-button, #bank-button').removeClass("container-visible");
    $('#followed-player-ui').removeClass("hidden");
    $('.pillars').removeClass("hidden");
    $('#player-data').find(".status").removeClass("hidden");
    $('#player-data').find("[class^=player]").addClass("hidden");
    $('#'+followedPlayer.attr("aria-username-sane") + "-container").find(".player-inventory").removeClass("hidden");

    // pan map to followed player
    let player = $('#'+followedPlayer.attr("aria-username-sane"));
    worldMap.panTo(player.attr("aria-x"), player.attr("aria-y"), player.attr("aria-plane"));
});

// followed player container buttons (bottom right)
$('.ui-button').click(function() {
    if(followedPlayer == null) {
        return;
    }

    // deal with bank ui / container individually
    if($(this).attr("aria-container") === ".player-bank") {
        $(this).toggleClass("container-visible");
        $('#player-data').find($(this).attr("aria-container")).toggleClass("hidden");
        return;
    }

    // deal with non-bank ui buttons
    $('#followed-player-ui').find("[class$=container-visible]").not("[id=bank-button]").removeClass("container-visible");
    $(this).addClass("container-visible");

    // deal with non-bank player containers
    $('#player-data').find("[class^=player]").not("[class=player-bank]").addClass("hidden");
    $('#player-data').find($(this).attr("aria-container")).removeClass("hidden");
});

// loot tracker visibility
$('.loot-tracker-ui').click(function() {
    $('.sub-content').addClass("visible");
});

$(document).on('click','#loot-tracker-exit-ui',function() {
    $('.sub-content').removeClass("visible");
});

function clearFeed() {
    if($('.feed-item').length > 5) {
        $('.update-feed').find(".feed-item:last").fadeOut("slow", function() {
            $(this).remove();
            clearFeed(); // calls function recursively AFTER the element has been removed
        });
    }
}