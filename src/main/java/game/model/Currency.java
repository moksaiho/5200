package game.model;

import java.util.Objects;

/**
 * Represents a currency type in the game.
 */
public class Currency {
    private int currencyID;
    private String currencyName;
    private Integer cap;
    private Integer weeklyCap;

    public Currency(int currencyID, String currencyName, Integer cap, Integer weeklyCap) {
        this.currencyID = currencyID;
        this.currencyName = currencyName;
        this.cap = cap;
        this.weeklyCap = weeklyCap;
    }

    public int getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(int currencyID) {
        this.currencyID = currencyID;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public Integer getWeeklyCap() {
        return weeklyCap;
    }

    public void setWeeklyCap(Integer weeklyCap) {
        this.weeklyCap = weeklyCap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return currencyID == currency.currencyID && 
               Objects.equals(currencyName, currency.currencyName) && 
               Objects.equals(cap, currency.cap) && 
               Objects.equals(weeklyCap, currency.weeklyCap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyID, currencyName, cap, weeklyCap);
    }

    @Override
    public String toString() {
        return String.format("Currency(%d, \"%s\", %s, %s)", 
                currencyID, currencyName, 
                cap != null ? cap : "null", 
                weeklyCap != null ? weeklyCap : "null");
    }
} 