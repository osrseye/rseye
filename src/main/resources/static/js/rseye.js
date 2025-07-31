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
            $('#followed-player-ui').addClass("ui-disabled");
            $('#followed-player-data').html("");
            $('#followed-player-ui > .container-visible').removeClass("container-visible");
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

$(document).on('click','[class~=locator]',function() {
    followedPlayer = $(this);
    $('#followed-player').html("<span class='title-text'>FOLLOWING</span><br><span class='feed-player'>" + $(this).attr("aria-username") + "</span>");

    // followed player
    const playerDiv = $('#'+$(this).attr("aria-username-sane") + "-container");
    $('#inventory-button').addClass("container-visible");
    $('#equipment-button, #skills-button, #quests-button, #bank-button').removeClass("container-visible");
    $('#followed-player-ui').removeClass("ui-disabled");
    $('#followed-player-data').html("<div id='followed-player-" + $(this).attr("aria-username-sane") + "'></div>");
    const followDiv = $('#followed-player-' + $(this).attr("aria-username-sane"));
    followDiv.append(playerDiv.find(".equipment-container").clone(true).toggle());
    followDiv.append(playerDiv.find(".inventory-container").clone(true));
    followDiv.append(playerDiv.find(".skills-container").clone(true).toggle());
    followDiv.append(playerDiv.find(".quests-container").clone(true).toggle());
    followDiv.append(playerDiv.find(".bank-container").clone(true).toggle());
    followDiv.find(".bank-container").find(":input").val("").trigger("input"); // stops search input being cloned and resets any toggles

    // pan map to followed player
    let player = $('#'+$(this).attr("aria-username-sane"));
    worldMap.panTo(player.attr("aria-x"), player.attr("aria-y"), player.attr("aria-plane"));
});

// followed player container buttons (bottom right)
$('.ui-button').click(function() {
    if(followedPlayer == null) {
        return;
    }
    const container = $('#followed-player-data').find($(this).attr("aria-container")).toggle();
    container.is(':hidden') ? $(this).removeClass("container-visible") : $(this).addClass("container-visible");
});

// loot tracker visibility
$('.loot-tracker-ui').click(function() {
    $('.sub-content').addClass("visible");
});

$('.loot-tracker-exit-ui').click(function() {
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