package game.model;

import java.util.Objects;

/**
 * Represents the stat bonuses given by an item.
 * This is a join entity between Item and StatisticType.
 */
public class StatisticBonus {
    private Item item;
    private StatisticType statisticType;
    private String bonusType;
    private Integer bonusFlatValue;
    private Float bonusPercentageValue;
    private Integer bonusCap;

    public StatisticBonus(Item item, StatisticType statisticType, String bonusType, 
                         Integer bonusFlatValue, Float bonusPercentageValue, Integer bonusCap) {
        this.item = item;
        this.statisticType = statisticType;
        this.bonusType = bonusType;
        this.bonusFlatValue = bonusFlatValue;
        this.bonusPercentageValue = bonusPercentageValue;
        this.bonusCap = bonusCap;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public StatisticType getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(StatisticType statisticType) {
        this.statisticType = statisticType;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }

    public Integer getBonusFlatValue() {
        return bonusFlatValue;
    }

    public void setBonusFlatValue(Integer bonusFlatValue) {
        this.bonusFlatValue = bonusFlatValue;
    }

    public Float getBonusPercentageValue() {
        return bonusPercentageValue;
    }

    public void setBonusPercentageValue(Float bonusPercentageValue) {
        this.bonusPercentageValue = bonusPercentageValue;
    }

    public Integer getBonusCap() {
        return bonusCap;
    }

    public void setBonusCap(Integer bonusCap) {
        this.bonusCap = bonusCap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticBonus that = (StatisticBonus) o;
        return Objects.equals(item, that.item) && 
               Objects.equals(statisticType, that.statisticType) && 
               Objects.equals(bonusType, that.bonusType) && 
               Objects.equals(bonusFlatValue, that.bonusFlatValue) && 
               Objects.equals(bonusPercentageValue, that.bonusPercentageValue) && 
               Objects.equals(bonusCap, that.bonusCap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, statisticType, bonusType, bonusFlatValue, 
                           bonusPercentageValue, bonusCap);
    }

    @Override
    public String toString() {
        return String.format("StatisticBonus(Item(%d), StatisticType(\"%s\"), \"%s\", %s, %s, %s)", 
                item.getItemID(), statisticType.getStatName(), bonusType, 
                bonusFlatValue != null ? bonusFlatValue : "null", 
                bonusPercentageValue != null ? bonusPercentageValue : "null", 
                bonusCap != null ? bonusCap : "null");
    }
} 