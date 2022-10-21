var baseX = 1152;
var baseY = 1215;
var clickX = 0;
var clickY = 0;
var transX = -6650;
var transY = -36125;
var canvasController = document.getElementById('canvas-controller');
var canvasMouse = Array.from({length: 3}, i => i = false);
var followedPlayer;

canvasController.addEventListener('mousedown', (e) => {
    if(e.button !== 0 && e.button !== 1 && e.button !== 2) { // middle click || right click
        return;
    }
    canvasMouse[e.button] = true;
    clickX = e.clientX; // store initial mouse location when translation begins
    clickY = e.clientY;
    transX = deltaX; // set origin of translation to final value of previous translation
    transY = deltaY;
});

canvasController.addEventListener('mouseup', (e) => {
    if(e.button !== 0 && e.button !== 1 && e.button !== 2) { // middle click || right click
        return;
    }
    canvasMouse[e.button] = false;
});

canvasController.addEventListener('mousemove', (e) => {
    if(canvasMouse[0] || canvasMouse[1] || canvasMouse[2]) {
        // deltaX|Y is final translation value, where transX|Y is initial value
        deltaX = transX + (e.clientX - clickX);
        deltaY = transY + (e.clientY - clickY);
        $('#canvas-container').css({'transform': 'translate('+deltaX+'px,'+deltaY+'px)', 'transition': ''});

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
    const x = (Number(player.position.x)-baseX)*4;
    const y = tHeight - ((Number(player.position.y)-baseY)*4);
    const pn = $("#"+player.usernameEncoded).find(".locator");
    const map = $("#map-status-"+player.usernameEncoded);
    pn.attr("aria-tx", x);
    pn.attr("aria-ty", y);
    map.attr("aria-tx", x);
    map.attr("aria-ty", y);

    // calculate center of the map
    const tx = map.attr("aria-tx") * -1;
    const ty = map.attr("aria-ty") * -1;
    deltaX = transX = tx + ($('#map').width()/2);
    deltaY = transY = ty + ($('#map').height()/2);
    miniX = transX = tx + (500/2);
    miniY = transY = ty + (500/2);

    // update main map - player marker & map position (if followed)
    $("#"+player.usernameEncoded+"-position").css({"top": y, "left": x})
    if(followedPlayer != null && followedPlayer.attr("aria-username-sane") === map.attr("aria-username-sane")) {
        $('#canvas-container').css({'transform': 'translate('+deltaX+'px,'+deltaY+'px)', 'transition': 'all 2s'});
    }

    // update player minimap
    $("#"+player.usernameEncoded+"-minimap-position").css({"top": y, "left": x})
    $('#' + player.usernameEncoded + '-minimap-container').css({'transform': 'translate('+miniX+'px,'+miniY+'px)', 'transition': 'all 2s'});
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
    deltaX = transX = ($(this).attr("aria-tx")*-1) + ($('#map').width()/2);
    deltaY = transY = ($(this).attr("aria-ty")*-1) + ($('#map').height()/2);
    $('#canvas-container').css({'transform': 'translate('+deltaX+'px,'+deltaY+'px)', 'transition': 'all 2s'});

    // followed player
    $('#inventory-button').addClass("container-visible");
    $('#equipment-button, #stats-button, #quests-button, #bank-button').removeClass("container-visible");
    $('#followed-player-ui').removeClass("ui-disabled");
    $('#followed-player-data').html("<div id='followed-player-" + $(this).attr("aria-username-sane") + "'></div>");
    const followedContainers = $('#followed-player-' + $(this).attr("aria-username-sane"));
    followedContainers.append($('#'+$(this).attr("aria-username-sane")).find(".equipment-container").clone(true).toggle());
    followedContainers.append($('#'+$(this).attr("aria-username-sane")).find(".inventory-container").clone(true));
    followedContainers.append($('#'+$(this).attr("aria-username-sane")).find(".stats-container").clone(true).toggle());
    followedContainers.append($('#'+$(this).attr("aria-username-sane")).find(".quests-container").clone(true).toggle());
    followedContainers.append($('#'+$(this).attr("aria-username-sane")).find(".bank-container").clone(true).toggle());
    followedContainers.find(".bank-container").find(":input").val("").trigger("input"); // stops search input being cloned and resets any toggles
    $('[data-toggle="tooltip"]').tooltip() // initialise tooltips
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