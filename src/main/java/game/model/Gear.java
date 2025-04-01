package game.model;

import java.util.Objects;

/**
 * Represents equippable gear in the game.
 */
public class Gear extends Item {
    public enum GearType {
        weapon, armor, accessory
    }
    
    public enum SlotType {
        head, body, legs, feet, hands, weapon, offhand, ring, earring
    }
    
    private GearType gearType;
    private SlotType slot;
    private int requiredLevel;
    private Integer damage; // Null for non-weapons

    public Gear(int itemID, String itemName, int itemLevel, int itemMaxStackSize, int itemPrice,
               GearType gearType, SlotType slot, int requiredLevel, Integer damage) {
        super(itemID, itemName, itemLevel, itemMaxStackSize, itemPrice);
        this.gearType = gearType;
        this.slot = slot;
        this.requiredLevel = requiredLevel;
        this.damage = damage;
    }

    public GearType getGearType() {
        return gearType;
    }

    public void setGearType(GearType gearType) {
        this.gearType = gearType;
    }

    public SlotType getSlot() {
        return slot;
    }

    public void setSlot(SlotType slot) {
        this.slot = slot;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Gear gear = (Gear) o;
        return requiredLevel == gear.requiredLevel && 
               gearType == gear.gearType && 
               slot == gear.slot && 
               Objects.equals(damage, gear.damage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gearType, slot, requiredLevel, damage);
    }

    @Override
    public String toString() {
        return String.format("Gear(%s, %s, %s, %d, %s)", 
                super.toString(), gearType, slot, requiredLevel, 
                damage != null ? damage : "null");
    }
} 