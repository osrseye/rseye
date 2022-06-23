$(document).ready(function() {
    var ping;
    var baseX = 1152, baseY = 1215;
    var clickX = 0, clickY = 0, transX = -6650, transY = -36125, deltaX = -6650, deltaY = -36125, zoom = 1.0; // weird offsets to center pre-zoomed canvas
    var cWidth = 12544;
    var cHeight = 1424;
    var tHeight = cHeight * 32;
    var canvasZoom = 1; // multiplier
    var canvasController = document.getElementById('canvas-controller');
    var canvasMouse = Array.from({length: 3}, i => i = false);
    $('#canvas-container').css({'width': '' + cWidth + 'px','height':'' + tHeight + 'px','transform': 'translate(' + deltaX + 'px,' + deltaY + 'px)'});
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
        map[i].src = "/img/map/map_" + i + ".webp";
        map[i].onload = function() {
            count++;
            if(count == 32) {
                // don't load map or connect to server unless entire map has loaded
                load();
                connect();
            }
        }
    }

    function load() {
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

    //canvasController.addEventListener('wheel', (e) => {
        //var canvasMinZoomRatio = 0.1;
        //var canvasMaxZoomRatio = 4.0;
        //e.preventDefault();
        //if(e.deltaY !== 0) {
            //var delta = (e.deltaY < 0) ? 0.1 : -0.1;
            //var temp = zoom + delta;
            //temp = (temp < canvasMinZoomRatio) ? canvasMinZoomRatio : temp > canvasMaxZoomRatio ? canvasMaxZoomRatio : temp; // minimum 0.1, maximum 4
            //zoom = Math.round(temp * 10) / 10; // deal with strange non-precise math
            //$('#canvas-container').css('transform', 'translate('+deltaX+'px,'+deltaY+'px) scale('+zoom+')');
        //}
    //});

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
        const pn = $("#"+player.urlUsername).find(".locator");
        const map = $("#map-status-"+player.urlUsername);
        pn.attr("aria-tx", x);
        pn.attr("aria-ty", y);
        map.attr("aria-tx", x);
        map.attr("aria-ty", y);

        if(!$("#"+player.urlUsername+"-position").length) {
            $("#canvas-container").append("<div id='"+ player.urlUsername + "-position' class='player-position' style='top:"+y+"px; left:"+x+"px'><img src='/img/map/map-pointer.webp'/><span class='player-position-label'>" + player.username + " (level-" + player.combatLevel + ")</span></div>");
        } else {
            $("#"+player.urlUsername+"-position").css({"top": y, "left": x})
        }

        if(followedPlayer != null && followedPlayer.attr("aria-username") === map.attr("aria-username")) {
            const tx = map.attr("aria-tx") * -1;
            const ty = map.attr("aria-ty") * -1;
            deltaX = transX = tx + ($('#map').width()/2);
            deltaY = transY = ty + ($('#map').height()/2);
            $('#canvas-container').css({'transform': 'translate('+deltaX+'px,'+deltaY+'px)', 'transition': 'all 2s'});
        }
    }

    function updatePlayerContainer(container, player, data) {
        $("#"+player.urlUsername).find(container).replaceWith(data);
        if(followedPlayer != null && followedPlayer.attr("aria-username") === player.urlUsername) {
            const obj = $('#followed-player-' + player.urlUsername).find(container);
            const style = obj.attr('style');
            obj.replaceWith(data);
            $('#followed-player-' + player.urlUsername).find(container).attr("style", style); // have to get dom again since obj will still contain old data even after .replaceWith()
        }
    }

    $(document).on('click','[class~=locator]',function() {
        followedPlayer = $(this);
        $('#followed-player').html("<span class='title-text'>FOLLOWING</span><br><span class='feed-player'>" + $(this).attr("aria-username") + "</span>");
        const tx = $(this).attr("aria-tx") * -1;
        const ty = $(this).attr("aria-ty") * -1;
        deltaX = transX = tx + ($('#map').width()/2);
        deltaY = transY = ty + ($('#map').height()/2);
        $('#canvas-container').css({'transform': 'translate('+deltaX+'px,'+deltaY+'px)', 'transition': 'all 2s'});

        // followed player
        $('#inventory-button').addClass("container-visible");
        $('#equipment-button').removeClass("container-visible");
        $('#stats-button').removeClass("container-visible");
        $('#quests-button').removeClass("container-visible");
        $('#bank-button').removeClass("container-visible");
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

    /*******************************************************/
    /*******************************************************/
    /*******************************************************/

    function connect() {
        ws = new WebSocket('wss://' + location.host + ':' + location.port + '/map/events');

        ws.onopen = function(event) {
            ping = setInterval(function(){ send("ping"); }, 30000); // ping the server every 30 seconds to keep the connection alive
            $(".player-data").empty(); // newZoom solution to disconnects (might flicker !BAD!)
            send("fetchLatestData");
        };

        ws.onerror = function(event) {
            ws.close();
        };

        ws.onclose = function(event) {
            clearInterval(ping); // clear the ping interval to stop pinging the server after it has closed
            connect();
        };

        ws.onmessage = function(event) {
            const data = event.data;

            if(data.startsWith("fetchLatestData:")) {
                const json = data.substring("fetchLatestData:".length, data.length);
                $.each(JSON.parse(json), function(username, player) {
                    $.get("/player/"+player.username, function(data) {
                        if(data.includes("LOGGED_IN")) {
                            $(".player-online").append(data);
                            $("#map-status-"+player.urlUsername).detach().appendTo(".map-player-online");
                        } else {
                            $(".player-offline").append(data);
                            $("#map-status-"+player.urlUsername).detach().appendTo(".map-player-offline");
                        }
                        updatePosition(player);
                    });
                });
                return;
            }

            if(data.startsWith("new_player:")) {
                const json = data.substring("new_player:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/player/"+player.username, function(data) {
                    if(data.includes("LOGGED_IN")) {
                        $(".player-online").append(data);
                        $("#map-status-"+player.urlUsername).detach().appendTo(".map-player-online");
                    } else {
                        $(".player-offline").append(data);
                        $("#map-status-"+player.urlUsername).detach().appendTo(".map-player-offline");
                    }
                });
                return;
            }

            if(data.startsWith("login_state")) {
                const json = data.substring("login_state:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/api/v1/player/"+player.username+"/login_state", function(data) {
                    const pn = $("#"+player.urlUsername);
                    const map = $("#map-status-"+player.urlUsername);
                    const badge = pn.find(".badge");
                    const mapBadge = map.find(".badge");
                    if(data == "LOGGED_IN") {
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

            if(data.startsWith("npc_kill")) {
                updatePosition(JSON.parse(data.substring("npc_kill:".length, data.length)));
                $.get("/combat/latest", function(data) {
                    $(".update-feed").prepend(data);
                });
                if($('.feed-item').length > 9) {
                    $('.update-feed').find(".feed-item:last").remove();
                }
                return;
            }

            if(data.startsWith("level_change:")) {
                updatePosition(JSON.parse(data.substring("level_change:".length, data.length)));
                $.get("/growth/latest", function(data) {
                    $(".update-feed").prepend(data);
                });
                if($('.feed-item').length > 9) {
                    $('.update-feed').find(".feed-item:last").remove();
                }
                return;
            }

            if(data.startsWith("inventory_items:")) {
                const json = data.substring("inventory_items:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/player/"+player.username+"/inventory", function(data) {
                    updatePlayerContainer(".inventory-container", player, data);
                });
                return;
            }

            if(data.startsWith("bank:")) {
                const json = data.substring("bank:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/player/"+player.username+"/bank", function(data) {
                    updatePlayerContainer(".bank-container", player, data);
                });
                return;
            }

            if(data.startsWith("level_data:")) {
                const json = data.substring("level_data:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/player/"+player.username+"/stats", function(data) {
                    updatePlayerContainer(".stats-container", player, data);
                });
                return;
            }

            if(data.startsWith("quest_change:")) {
                const json = data.substring("quest_change:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/player/"+player.username+"/quests", function(data) {
                    updatePlayerContainer(".quests-container", player, data);
                });
                return;
            }

            if(data.startsWith("equipped_items:")) {
                const json = data.substring("equipped_items:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/player/"+player.username+"/equipment", function(data) {
                    updatePlayerContainer(".equipment-container", player, data);
                });
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