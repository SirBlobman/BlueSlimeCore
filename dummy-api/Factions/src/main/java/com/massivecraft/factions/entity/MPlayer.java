package com.massivecraft.factions.entity;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.RelationParticipator;
import com.massivecraft.massivecore.store.SenderEntity;

public class MPlayer extends SenderEntity<MPlayer> implements RelationParticipator {
    public static MPlayer get(Object object) {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    @Override
    public Rel getRelationTo(RelationParticipator observer) {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    public Faction getFaction() {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    public boolean hasFaction() {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    public boolean isOverriding() {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    public Rel getRole() {
        throw new UnsupportedOperationException("Dummy API");
    }
}