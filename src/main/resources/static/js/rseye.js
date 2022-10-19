$(document).ready(function() {
    var baseX = 1152;
    var baseY = 1215;
    var clickX = 0;
    var clickY = 0;
    var transX = -6650;
    var transY = -36125;
    var deltaX = -6650;
    var deltaY = -36125;
    var cWidth = 12544;
    var cHeight = 1424;
    var tHeight = cHeight * 32;
    var canvasController = document.getElementById('canvas-controller');
    var canvasMouse = Array.from({length: 3}, i => i = false);
    $('#canvas-container').css({'width':'' + cWidth + 'px','height':'' + tHeight + 'px','transform': 'translate(' + deltaX + 'px,' + deltaY + 'px)'});
    var followedPlayer;

    var ctx = new Array();
    var map = new Array();
    var count = 0;
    for(var i = 0; i < 32 ; ++i) {
        var canvas = document.querySelector('.c'+i);
            canvas.width = 12544;
            canvas.height = 1424;
        var ct = canvas.getContext('2d', {antialias: false});
            ct.textBaseline = 'middle';
            ct.textAlign = 'center';
            ct.font = '16px sans-serif';
        ctx[i] = ct;

        map[i] = new Image();
        map[i].src = "/data/map/map_" + i + ".webp";
        map[i].onload = function() {
            if(count++ == 31) {
                load(); // don't load map or connect to server unless entire map has loaded
                connect();
                display();
            }
        }
    }

    async function load() {
        for(var i = 0; i < 32 ; ++i) {
            ctx[i].clearRect(0, 0, cWidth, cHeight);
            ctx[i].drawImage(map[i], 0, 0);
        }
    }

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

        // update marker on the map
        $("#"+player.usernameEncoded+"-position").css({"top": y, "left": x})

        if(followedPlayer != null && followedPlayer.attr("aria-username-sane") === map.attr("aria-username-sane")) {
            const tx = map.attr("aria-tx") * -1;
            const ty = map.attr("aria-ty") * -1;
            deltaX = transX = tx + ($('#map').width()/2);
            deltaY = transY = ty + ($('#map').height()/2);
            $('#canvas-container').css({'transform': 'translate('+deltaX+'px,'+deltaY+'px)', 'transition': 'all 2s'});
        }
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

    /*******************************************************/
    /*******************************************************/
    /*******************************************************/

    function display() {
        $('.loading-screen').remove();
        $('.content').toggle();
    }

    function connect() {
        ws = new WebSocket('wss://' + location.host + ':' + location.port + '/map/events');

        ws.onopen = function(event) {
            $(".player-data").empty(); // solution to disconnects (might flicker !BAD!)
            send("fetchLatestData");
        };

        ws.onerror = function(event) {
            ws.close();
        };

        ws.onclose = function(event) {
            connect();
        };

        ws.onmessage = function(event) {
            const data = event.data;

            if(data.startsWith("fetchLatestData:")) {
                $.each(JSON.parse(data.substring("fetchLatestData:".length, data.length)), function(username, player) {
                    $.get("/player/"+player.username, function(data) {
                        if(data.includes("LOGGED_IN")) {
                            $(".player-online").append(data);
                            $("#map-status-"+player.usernameEncoded).detach().appendTo(".map-player-online");
                        } else {
                            $(".player-offline").append(data);
                            $("#map-status-"+player.usernameEncoded).detach().appendTo(".map-player-offline");
                        }
                        $("#"+player.usernameEncoded+"-position").detach().appendTo('#canvas-container');
                        $('[data-toggle="tooltip"]').tooltip() // initialise tooltips
                        updatePosition(player);
                    });
                });
                return;
            }

            if(data.startsWith("new_player:")) {
                const player = JSON.parse(data.substring("new_player:".length, data.length));
                $.get("/player/"+player.username, function(data) {
                    $(".player-offline").append(data);
                    $("#map-status-"+player.usernameEncoded).detach().appendTo(".map-player-offline");
                    $("#"+player.usernameEncoded+"-position").detach().appendTo('#canvas-container');
                });
                return;
            }

            if(data.startsWith("login_update:")) {
                const player = JSON.parse(data.substring("login_update:".length, data.length));
                $.get("/api/v2/player/"+player.username+"/login_state", function(data) {
                    const pn = $("#"+player.usernameEncoded);
                    const map = $("#map-status-"+player.usernameEncoded);
                    const badge = pn.find(".badge");
                    const mapBadge = map.find(".badge");
                    if((data == "LOGGED_IN" || data == "HOPPING") && badge.hasClass("badge-danger")) {
                        badge.removeClass("badge-danger").addClass("badge-success").text("Online");
                        pn.detach().appendTo(".player-online");
                        mapBadge.removeClass("badge-danger").addClass("badge-success").text("Online");
                        map.detach().appendTo(".map-player-online");
                        return;
                    }
                    badge.removeClass("badge-success").addClass("badge-danger").text("Offline");
                    pn.detach().appendTo(".player-offline");
                    mapBadge.removeClass("badge-success").addClass("badge-danger").text("Offline");
                    map.detach().appendTo(".map-player-offline");
                });
                return;
            }

            if(data.startsWith("position_update:")) {
                updatePosition(JSON.parse(data.substring("position_update:".length, data.length)));
                return;
            }

            if(data.startsWith("combat_loot_update:")) {
                $.get("/combat/latest", function(data) {
                    $(".update-feed").css({top:-150});
                    $(".update-feed").prepend(data);
                    $(".update-feed").animate({top: 5}, 1000);
                    clearFeed();
                });
                return;
            }

            if(data.startsWith("raid_loot_update:")) {
                $.get("/raid/latest", function(data) {
                    $(".update-feed").css({top:-150});
                    $(".update-feed").prepend(data);
                    $(".update-feed").animate({top: 5}, 1000);
                    clearFeed();
                });
                return;
            }

            if(data.startsWith("stat_update:")) {
                $.get("/growth/latest", function(data) {
                    $(".update-feed").prepend(data);
                    clearFeed();
                });
                return;
            }

            if(data.startsWith("quest_update:")) {
                $.get("/quest/latest", function(data) {
                    $(".update-feed").prepend(data);
                    clearFeed();
                });
                return;
            }

            if(data.startsWith("exp_update")) {
                const player = JSON.parse(data.substring("exp_update:".length, data.length));
                var updateString = "";
                for(const [key, value] of Object.entries(player.data)) {
                    updateString += "<img class='xp-drop-icon' src='/data/icons/skill/"+key+".png'/><span>"+value+"</span><br>";
                }
                var update = $("<span class='xp-drop'>" + updateString + "</span>");
                $("#"+player.usernameEncoded+"-position").append(update);
                update.css({top:(-25+-update.height())});
                update.animate({
                    opacity: '0',
                    top: -225+-update.height()
                }, 3000, function(){
                  update.remove();
                });
                return;
            }

            if(data.startsWith("inventory_update:")) {
                const player = JSON.parse(data.substring("inventory_update:".length, data.length));
                $.get("/player/"+player.username+"/inventory", function(data) {
                    updatePlayerContainer(".inventory-container", player, data);
                });
                return;
            }

            if(data.startsWith("bank_update:")) {
                const player = JSON.parse(data.substring("bank_update:".length, data.length));
                $.get("/player/"+player.username+"/bank", function(data) {
                    updatePlayerContainer(".bank-container", player, data);
                });
                return;
            }

            if(data.startsWith("equipment_update:")) {
                const player = JSON.parse(data.substring("equipment_update:".length, data.length));
                $.get("/player/"+player.username+"/equipment", function(data) {
                    updatePlayerContainer(".equipment-container", player, data);
                });
                return;
            }

            if(data.startsWith("status_update")) {
                // loads the player current hitpoints/prayer
                const player = JSON.parse(data.substring("status_update:".length, data.length));
                $.get("/player/"+player.username+"/status", function(data) {
                    $("#map-status-"+player.usernameEncoded).find(".player-current-state").replaceWith(data);
                });
                return;
            }

            if(data.startsWith("stat_data:")) {
                const player = JSON.parse(data.substring("stat_data:".length, data.length));
                $.get("/player/"+player.username+"/stats", function(data) {
                    updatePlayerContainer(".stats-container", player, data);
                });
                return;
            }

            if(data.startsWith("quest_data:")) {
                const player = JSON.parse(data.substring("quest_data:".length, data.length));
                $.get("/player/"+player.username+"/quests", function(data) {
                    updatePlayerContainer(".quests-container", player, data);
                });
                return;
            }

            if(data.startsWith("overhead_update:")) {
                const player = JSON.parse(data.substring("overhead_update:".length, data.length));
                $("#map-status-"+player.usernameEncoded).find(".player-overheads").children().hide();
                $("#map-status-"+player.usernameEncoded).find("."+player.overhead).show();
                return;
            }

            if(data.startsWith("skull_update:")) {
                const player = JSON.parse(data.substring("skull_update:".length, data.length));
                $("#map-status-"+player.usernameEncoded).find(".player-skulls").children().hide();
                $("#map-status-"+player.usernameEncoded).find("."+player.skull).show();
                return;
            }
        };
    }

    function send(data) {
        if(ws.readyState == 0) {
            setTimeout(() => {
                send(data);
            }, 10);
            return;
        }
        if(ws.readyState == 1) {
            ws.send(data);
        }
    }
})