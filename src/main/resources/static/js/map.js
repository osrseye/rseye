$(document).ready(function() {
    var ping;
    var baseX = 1152, baseY = 1215;
    var clickX = 0, clickY = 0, transX = -7120, transY = -37175, deltaX = -7120, deltaY = -37175, zoom = 1.0; // weird offsets to center pre-zoomed canvas
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
            }
        }
    });

    canvasController.addEventListener('wheel', (e) => {
        var canvasMinZoomRatio = 0.1;
        var canvasMaxZoomRatio = 4.0;
        e.preventDefault();
        if(e.deltaY !== 0) {
            //var delta = (e.deltaY < 0) ? 0.1 : -0.1;
            //var temp = zoom + delta;
            //temp = (temp < canvasMinZoomRatio) ? canvasMinZoomRatio : temp > canvasMaxZoomRatio ? canvasMaxZoomRatio : temp; // minimum 0.1, maximum 4
            //zoom = Math.round(temp * 10) / 10; // deal with strange non-precise math
            //$('#canvas-container').css('transform', 'translate('+deltaX+'px,'+deltaY+'px) scale('+zoom+')');
        }
    });

    function updatePosition(player) {
        const x = (Number(player.position.x)-baseX)*4;
        const y = tHeight - ((Number(player.position.y)-baseY)*4);
        const pn = $("#"+player.username.split(" ").join("-")).find(".locator");
        pn.attr("aria-tx", x);
        pn.attr("aria-ty", y);

        if(!$("#"+player.username.split(" ").join("-")+"-position").length) {
            $("#canvas-container").append("<div id='"+ player.username.split(" ").join("-") + "-position' class='player-position' style='top:"+y+"px; left:"+x+"px'><img src='/img/map/map-pointer.webp'/><span class='player-position-label'>" + player.username + " (level-" + player.combatLevel + ")</span></div>");
        } else {
            $("#"+player.username.split(" ").join("-")+"-position").css({"top": y, "left": x})
        }

        if(followedPlayer != null && followedPlayer.attr("aria-username") === pn.attr("aria-username")) {
            const tx = pn.attr("aria-tx") * -1;
            const ty = pn.attr("aria-ty") * -1;
            deltaX = transX = tx + ($('.canvas-main').width()/2);
            deltaY = transY = ty + ($('.canvas-main').height()/2);
            $('#canvas-container').css({'transform': 'translate('+deltaX+'px,'+deltaY+'px)', 'transition': 'all 2s'});
        }
    }

    $(document).on('click','[class^=locator]',function() {
        followedPlayer = $(this);
        $('#followed-player').html("<span class='title-text'>FOLLOWING</span><br><span class='feed-player'>" + $(this).attr("aria-username") + "</span>");
        const tx = $(this).attr("aria-tx") * -1;
        const ty = $(this).attr("aria-ty") * -1;
        deltaX = transX = tx + ($('.canvas-main').width()/2);
        deltaY = transY = ty + ($('.canvas-main').height()/2);
        $('#canvas-container').css({'transform': 'translate('+deltaX+'px,'+deltaY+'px)', 'transition': 'all 2s'});
    });

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
                        data.includes("LOGGED_IN") ? $(".player-online").append(data) : $(".player-offline").append(data)
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
                    data.includes("LOGGED_IN") ? $(".player-online").append(data) : $(".player-offline").append(data)
                });
                return;
            }

            if(data.startsWith("login_state")) {
                const json = data.substring("login_state:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/api/v1/player/"+player.username+"/login_state", function(data) {
                    const pn = $("#"+player.username.split(" ").join("-"));
                    const badge = pn.find(".badge");
                    if(data == "LOGGED_IN") {
                        badge.removeClass("badge-danger").addClass("badge-success").text("Online");
                        pn.detach().appendTo(".player-online");
                        return;
                    }
                    badge.removeClass("badge-success").addClass("badge-danger").text("Offline");
                    pn.detach().appendTo(".player-offline");
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
                    $("#"+player.username.split(" ").join("-")).find(".inventory-container").replaceWith(data);
                });
                return;
            }

            if(data.startsWith("bank:")) {
                const json = data.substring("bank:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/player/"+player.username+"/bank", function(data) {
                    $("#"+player.username.split(" ").join("-")).find(".bank-container").replaceWith(data);
                });
                return;
            }

            if(data.startsWith("level_data:")) {
                const json = data.substring("level_data:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/player/"+player.username+"/stats", function(data) {
                    $("#"+player.username.split(" ").join("-")).find(".stats-container").replaceWith(data);
                });
                return;
            }

            if(data.startsWith("quest_change:")) {
                const json = data.substring("quest_change:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/player/"+player.username+"/quests", function(data) {
                    $("#"+player.username.split(" ").join("-")).find(".quests-container").replaceWith(data);
                });
                return;
            }

            if(data.startsWith("equipped_items:")) {
                const json = data.substring("equipped_items:".length, data.length);
                const player = JSON.parse(json);
                updatePosition(player);
                $.get("/player/"+player.username+"/equipment", function(data) {
                    $("#"+player.username.split(" ").join("-")).find(".equipment-container").replaceWith(data);
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