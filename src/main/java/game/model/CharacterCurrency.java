package game.model;

import java.util.Objects;

/**
 * Represents a character's currency amounts.
 * This is a join entity between Character and Currency.
 */
public class CharacterCurrency {
    private Character character;
    private Currency currency;
    private int amount;
    private int weeklyAmountAcquired;

    public CharacterCurrency(Character character, Currency currency, int amount, int weeklyAmountAcquired) {
        this.character = character;
        this.currency = currency;
        this.amount = amount;
        this.weeklyAmountAcquired = weeklyAmountAcquired;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getWeeklyAmountAcquired() {
        return weeklyAmountAcquired;
    }

    public void setWeeklyAmountAcquired(int weeklyAmountAcquired) {
        this.weeklyAmountAcquired = weeklyAmountAcquired;
    }

    /**
     * Calculate how much more currency can be acquired this week before hitting cap
     */
    public int getWeeklyAmountRemaining() {
        if (currency.getWeeklyCap() == null) {
            return Integer.MAX_VALUE; // No weekly cap
        }
        return Math.max(0, currency.getWeeklyCap() - weeklyAmountAcquired);
    }

    /**
     * Calculate how much more currency can be held before hitting cap
     */
    public int getAmountUntilCap() {
        if (currency.getCap() == null) {
            return Integer.MAX_VALUE; // No cap
        }
        return Math.max(0, currency.getCap() - amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterCurrency that = (CharacterCurrency) o;
        return amount == that.amount && 
               weeklyAmountAcquired == that.weeklyAmountAcquired && 
               Objects.equals(character, that.character) && 
               Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, currency, amount, weeklyAmountAcquired);
    }

    @Override
    public String toString() {
        return String.format("CharacterCurrency(Character(%d), Currency(%d), %d, %d)", 
                character.getCharacterID(), currency.getCurrencyID(), 
                amount, weeklyAmountAcquired);
    }
} 