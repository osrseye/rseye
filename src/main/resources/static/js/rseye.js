var mapController = document.getElementById('map');
var mapMouse = Array.from({length: 3}, i => i = false);
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

$('.ui-button').click(function() {
    if(followedPlayer == null) {
        return;
    }
    const container = $('#followed-player-data').find($(this).attr("aria-container")).toggle();
    container.is(':hidden') ? $(this).removeClass("container-visible") : $(this).addClass("container-visible");
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

    // store player position on their div
    $("#"+player.usernameEncoded).attr("aria-x", x);
    $("#"+player.usernameEncoded).attr("aria-y", y);

    // update world map
    const smap = $("#map-status-"+player.usernameEncoded)
    if(followedPlayer != null && followedPlayer.attr("aria-username-sane") === smap.attr("aria-username-sane")) {
        panWorldMap(x, y);
    }
    var marker = player.usernameEncoded + "WorldmapMarker";
    window[marker].setLatLng(map.unproject(L.point(x, y)));

    // update player minimap
    var minimap = player.usernameEncoded + "Minimap";
    var pan = player.usernameEncoded + "MinimapPan";
    var minimarker = player.usernameEncoded + "MinimapMarker";
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