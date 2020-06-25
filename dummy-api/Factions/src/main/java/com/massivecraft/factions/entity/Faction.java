package com.massivecraft.factions.entity;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.RelationParticipator;

public class Faction implements RelationParticipator {
    @Override
    public Rel getRelationTo(RelationParticipator observer) {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    public MPlayer getLeader() {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    public String getId() {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    public String getName() {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    public boolean isNone() {
        throw new UnsupportedOperationException("Dummy API");
    }
}