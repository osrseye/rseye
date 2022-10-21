var deltaX = -6650;
var deltaY = -36125;
var cWidth = 12544;
var cHeight = 1424;
var tHeight = cHeight * 32;

// load the map globally
var count = 0;
var ready = false;
var map = new Array();
for(var x = 0; x < 32; ++x) {
    map[x] = new Image();
    map[x].src = "/data/map/map_" + x + ".webp";
    map[x].onload = function() {
        if(count++ == 31) {
            ready = true;
        }
    }
}

$(document).ready(async function() {
    $('#canvas-container').css({'width':'' + cWidth + 'px','height':'' + tHeight + 'px','transform': 'translate(' + deltaX + 'px,' + deltaY + 'px)'});

    var ctx = new Array();
    for(var i = 0; i < 32 ; ++i) {
        var canvas = document.querySelector('.c'+i);
            canvas.width = 12544;
            canvas.height = 1424;
        var ct = canvas.getContext('2d', {antialias: false});
            ct.textBaseline = 'middle';
            ct.textAlign = 'center';
            ct.font = '16px sans-serif';
        ctx[i] = ct;
    }

    while(!ready) {
        await new Promise(r => setTimeout(r, 100));
    }

    for(var i = 0; i < 32 ; ++i) {
        ctx[i].clearRect(0, 0, cWidth, cHeight);
        ctx[i].drawImage(map[i], 0, 0);
    }
    display();
    connect();

    function display() {
        $('.loading-screen').remove();
        $('.content').toggle();
    }
});