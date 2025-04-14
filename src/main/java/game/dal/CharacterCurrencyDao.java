package game.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import game.model.Character;
import game.model.Currency;
import game.model.CharacterCurrency;


public class CharacterCurrencyDao {
	
	

	/**
     * Creates a CharacterCurrency record (associates a character with a currency).
     */
    public static CharacterCurrency create(Connection cxn, Character character, Currency currency, int amount, int weeklyAmountAcquired) throws SQLException {
        String sql = "INSERT INTO CharacterCurrency (characterID, currencyID, amount, weeklyAmountAcquired) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            stmt.setInt(2, currency.getCurrencyID());
            stmt.setInt(3, amount);
            stmt.setInt(4, weeklyAmountAcquired);
            stmt.executeUpdate();
            return new CharacterCurrency(character, currency, amount, weeklyAmountAcquired);
        }
    }

    /**
     * Retrieves a CharacterCurrency by character ID and currency ID.
     */
    public static CharacterCurrency getByCharacterAndCurrency(Connection cxn, int characterID, int currencyID) throws SQLException {
        String sql = "SELECT * FROM CharacterCurrency WHERE characterID = ? AND currencyID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, characterID);
            stmt.setInt(2, currencyID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int amount = rs.getInt("amount");
                    int weeklyAmountAcquired = rs.getInt("weeklyAmountAcquired");

                    Character character = CharacterDao.getCharacterByID(cxn, characterID);
                    Currency currency = CurrencyDao.getCurrencyByID(cxn, currencyID);

                    return new CharacterCurrency(character, currency, amount, weeklyAmountAcquired);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Gets all currencies for a character.
     */
    public static List<CharacterCurrency> getCurrenciesForCharacter(Connection cxn, Character character) throws SQLException {
        String sql = "SELECT cc.currencyID, cc.amount, cc.weeklyAmountAcquired FROM CharacterCurrency cc WHERE cc.characterID = ?";
        List<CharacterCurrency> result = new ArrayList<>();
        
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int currencyID = rs.getInt("currencyID");
                    int amount = rs.getInt("amount");
                    int weeklyAmountAcquired = rs.getInt("weeklyAmountAcquired");
                    
                    Currency currency = CurrencyDao.getCurrencyByID(cxn, currencyID);
                    result.add(new CharacterCurrency(character, currency, amount, weeklyAmountAcquired));
                }
            }
        }
        
        return result;
    }
    
    /**
     * Updates the amount of a specific currency for a character.
     */
    public static CharacterCurrency updateAmount(Connection cxn, CharacterCurrency characterCurrency, int newAmount) throws SQLException {
        String sql = "UPDATE CharacterCurrency SET amount = ? WHERE characterID = ? AND currencyID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, newAmount);
            stmt.setInt(2, characterCurrency.getCharacter().getCharacterID());
            stmt.setInt(3, characterCurrency.getCurrency().getCurrencyID());
            int updated = stmt.executeUpdate();
            
            if (updated == 1) {
                characterCurrency.setAmount(newAmount);
                return characterCurrency;
            } else {
                return null;
            }
        }
    }

}
