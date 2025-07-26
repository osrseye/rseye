function connect() {
    ws = new WebSocket('wss://' + location.host + ':' + location.port + '/map/events');

    ws.onopen = function(event) {
        send("fetch");
    };

    ws.onerror = function(event) {
        ws.close();
    };

    ws.onclose = function(event) {
        connect();
    };

    ws.onmessage = function(event) {
        const data = event.data;

        if(data === "ping") {
            return send("pong");
        }

        const index = data.indexOf(":");
        if(index === -1) {
            return console.warn("Malformed message:", data);
        }

        const type = data.substring(0, index);
        const payload = data.substring(index + 1);
        switch(type) {
            case "fetch": return fetch(payload);
            case "new_player": return newPlayer(payload);
            case "login_update": return loginUpdate(payload);
            case "position_update": return positionUpdate(payload);
            case "combat_loot_update": return combatLootUpdate();
            case "raid_loot_update": return raidLootUpdate();
            case "skill_update": return skillUpdate();
            case "skill_data": return skillData(payload);
            case "quest_update": return questUpdate();
            case "exp_update": return expUpdate(payload);
            case "inventory_update": return inventoryUpdate(payload);
            case "bank_update": return bankUpdate(payload);
            case "equipment_update": return equipmentUpdate(payload);
            case "status_update": return statusUpdate(payload);
            case "quest_data": return questData(payload);
            case "overhead_update": return overheadUpdate(payload);
            case "skull_update": return skullUpdate(payload);
            default: return console.warn("Unhandled message type:", type);
        }
    };

    function fetch(payload) {
        // in case websocket connection is lost, clear old data
        $(".player-online").empty();
        $(".player-offline").empty();
        $(".map-player-online").empty();
        $(".map-player-offline").empty();
        $(".leaflet-marker-pane").empty(); // experimental

        // load each player
        $.each(JSON.parse(payload), function(username, player) {
            $.get("/player/"+player.username.natural, function(data) {
                $(".players").append(data); // hidden data
                $("#"+player.username.encoded).detach().appendTo(data.includes("LOGGED_IN") ? ".map-player-online" : ".map-player-offline");

                // add to world map
                worldMap.addPlayerMarker(player, true, "world");

                // create minimap
                playerMinimaps.set(player.username.encoded, new RuneMap(player.username.encoded + "-minimap"));
                minimap = playerMinimaps.get(player.username.encoded);
                minimap.setView(player.position.offx, player.position.offy);
                minimap.addPlayerMarker(player, false, "mini");

                updatePosition(player);
            });
        });
        $('[data-toggle="tooltip"]').tooltip() // initialise tooltips
    }

    function newPlayer(payload) {
        const player = JSON.parse(payload);
        $.get("/player/"+player.username.natural, function(data) {
            $(".players").append(data); // hidden data
            $("#"+player.username.encoded).detach().appendTo(data.includes("LOGGED_IN") ? ".map-player-online" : ".map-player-offline");
            $("#"+player.username.encoded+"-position").detach().appendTo('#canvas-container');

            // add to world map
            worldMap.addPlayerMarker(player, true, "world");

            // create minimap
            playerMinimaps.set(player.username.encoded, new RuneMap(player.username.encoded + "-minimap"));
            minimap = playerMinimaps.get(player.username.encoded);
            minimap.setView(player.position.offx, player.position.offy);
            minimap.addPlayerMarker(player, false, "mini");

            updatePosition(player);
        });
        $('[data-toggle="tooltip"]').tooltip() // initialise tooltips
    }

    function loginUpdate(payload) {
        const player = JSON.parse(payload);
        $.get("/api/v2/player/"+player.username.natural+"/login_state", function(data) {
            const pn = $("#"+player.username.encoded);
            const badge = pn.find(".badge");

            if(data == "LOGGED_IN" || data == "HOPPING") {
                if(badge.hasClass("badge-danger")) {
                    badge.removeClass("badge-danger").addClass("badge-success").text("Online");
                    pn.detach().appendTo(".map-player-online");
                }
                return;
            }

            badge.removeClass("badge-success").addClass("badge-danger").text("Offline");
            pn.detach().appendTo(".map-player-offline");
        });
    }

    function positionUpdate(payload) {
        const player = JSON.parse(payload);
        updatePosition(player);
    }

    function expUpdate(payload) {
        const player = JSON.parse(payload);

        // update xp in skill box
        $.get("/player/"+player.username.natural+"/skills", function(data) {
            updatePlayerContainer(".skills-container", player, data);
        });

        var updateString = "";
        for(const [key, value] of Object.entries(player.data)) {
            updateString += "<img class='xp-drop-icon' src='/data/icons/skill/"+key+".png'/><span>"+value+"</span><br>";
        }

        var updateWorld = $("<span class='xp-drop'>" + updateString + "</span>");
        var updateMini = $("<span class='xp-drop'>" + updateString + "</span>");
        $("div[title='"+player.username.encoded+"-world']").append(updateWorld);
        $("div[title='"+player.username.encoded+"-mini']").append(updateMini);
        updateWorld.css({top:(-15+-updateWorld.height())});
        updateWorld.animate({
            opacity: '0',
            top: -225+-updateWorld.height()
        }, 3000, function(){
          updateWorld.remove();
        });
        updateMini.css({top:(-15+-updateMini.height())});
        updateMini.animate({
            opacity: '0',
            top: -225+-updateMini.height()
        }, 3000, function(){
          updateMini.remove();
        });
    }

    function skillUpdate() {
        $.get("/growth/latest", function(data) {
            $(".update-feed").prepend(data);
            clearFeed();
        });
    }

    function skillData(payload) {
        const player = JSON.parse(payload);
        $.get("/player/"+player.username.natural+"/skills", function(data) {
            updatePlayerContainer(".skills-container", player, data);
        });
    }

    function questUpdate() {
        $.get("/quest/latest", function(data) {
            $(".update-feed").prepend(data);
            clearFeed();
        });
    }

    function questData(payload) {
        const player = JSON.parse(payload);
        $.get("/player/"+player.username.natural+"/quests", function(data) {
            updatePlayerContainer(".quests-container", player, data);
        });
    }

    function inventoryUpdate(payload) {
        const player = JSON.parse(payload);
        $.get("/player/"+player.username.natural+"/inventory", function(data) {
            updatePlayerContainer(".inventory-container", player, data);
        });
    }

    function equipmentUpdate(payload) {
        const player = JSON.parse(payload);
        $.get("/player/"+player.username.natural+"/equipment", function(data) {
            updatePlayerContainer(".equipment-container", player, data);
        });
    }

    function bankUpdate(payload) {
        const player = JSON.parse(payload);
        $.get("/player/"+player.username.natural+"/bank", function(data) {
            updatePlayerContainer(".bank-container", player, data);
        });
    }

    function statusUpdate(payload) {
        const player = JSON.parse(payload);
        $.get("/player/"+player.username.natural+"/status", function(data) {
            $("#"+player.username.encoded).find(".status").html(data);
        });
    }

    function overheadUpdate(payload) {
        const player = JSON.parse(payload);
        $("#"+player.username.encoded).find(".player-overheads").children().hide();
        $("#"+player.username.encoded).find("."+player.overhead).show();
    }

    function skullUpdate(payload) {
        const player = JSON.parse(payload);
        $("#"+player.username.encoded).find(".player-skulls").children().hide();
        $("#"+player.username.encoded).find("."+player.skull).show();
    }

    function combatLootUpdate() {
        $.get("/combat/latest", function(data) {
            $(".update-feed").css({top:-150});
            $(".update-feed").prepend(data);
            $(".update-feed").animate({top: 5}, 1000);
            clearFeed();
        });
        $.get("/loot-tracker", function(data) {
            $("#global-loot-tracker").html(data);

            // rejig the track to display items in a "masonry" layout
            const items = document.querySelectorAll('.monster');
            items.forEach(item => {
              const rowHeight = 86; // matches grid-auto-rows
              const rows = Math.ceil(item.getBoundingClientRect().height / rowHeight);
              item.style.setProperty('--rows', rows);
            });
        })
    }

    function raidLootUpdate() {
        $.get("/raid/latest", function(data) {
            $(".update-feed").css({top:-150});
            $(".update-feed").prepend(data);
            $(".update-feed").animate({top: 5}, 1000);
            clearFeed();
        });
    }
}

function send(data) {
    if(ws.readyState == 1) {
        ws.send(data);
    }
    if(ws.readyState == 0) {
        setTimeout(() => {
            send(data);
        }, 10);
        return;
    }
}