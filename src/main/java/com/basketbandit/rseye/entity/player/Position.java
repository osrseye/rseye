package com.basketbandit.rseye.entity.player;

public record Position(Integer plane, Integer x, Integer y, Integer offx, Integer offy){
    public Position() {
        // varrock magic numbers
        this(0,3213, 3428, (3213-1024)*4, (256*178-((3428-1215)*4)));
    }
}
