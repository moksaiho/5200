package game.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.Objects;

/**
 * General utilities for data-access methods
 */
public class Utils {
  private Utils() { }

  /**
   * Converts a Java date value to a SQL timestamp.
   * @param d Date to be converted; must not be null
   * @return equivalent SQL timestamp value
   */
  public static Timestamp dateToTimestamp(Date d) {
    return new Timestamp(Objects.requireNonNull(d).getTime());
  }

  /**
   * Converts a SQL timestamp value to a Java date.
   * @param t Timestamp to be converted; must not be null
   * @return equivalent Java date value
   */
  public static Date timestampToDate(Timestamp t) {
    return new Date(Objects.requireNonNull(t).getTime());
  }

  /**
   * Returns a SQL Timestamp value representing the current time.
   */
  public static Timestamp currentTimestamp() {
    return new Timestamp(System.currentTimeMillis());
  }

  /**
   * Retrieves the value of the AUTO_INCREMENT key for the first record
   * inserted by the given statement.
   * @param insertStmt statement that inserted one or more records
   * @return Value of the primary key for the newly-created record
   * @throws SQLException if we cannot retrieve the key value
   */
  public static int getAutoIncrementKey(
    final PreparedStatement insertStmt
  ) throws SQLException {
    try (ResultSet rs = insertStmt.getGeneratedKeys()) {
      if (rs.next()) {
        return rs.getInt(1);
      } else {
        throw new SQLException("Unable to retrieve auto-generated key");
      }
    }
  }

  /**
   * 为PreparedStatement设置可能为null的String参数
   * @param stmt PreparedStatement对象
   * @param index 参数索引
   * @param value 可能为null的String值
   * @throws SQLException 如果设置参数时发生SQL错误
   */
  public static void setNullableString(PreparedStatement stmt, int index, String value) throws SQLException {
    if (value == null) {
      stmt.setNull(index, Types.VARCHAR);
    } else {
      stmt.setString(index, value);
    }
  }

  /**
   * 为PreparedStatement设置可能为null的Integer参数
   * @param stmt PreparedStatement对象
   * @param index 参数索引
   * @param value 可能为null的Integer值
   * @throws SQLException 如果设置参数时发生SQL错误
   */
  public static void setNullableInt(PreparedStatement stmt, int index, Integer value) throws SQLException {
    if (value == null) {
      stmt.setNull(index, Types.INTEGER);
    } else {
      stmt.setInt(index, value);
    }
  }

  /**
   * 为PreparedStatement设置可能为null的Float参数
   * @param stmt PreparedStatement对象
   * @param index 参数索引
   * @param value 可能为null的Float值
   * @throws SQLException 如果设置参数时发生SQL错误
   */
  public static void setNullableFloat(PreparedStatement stmt, int index, Float value) throws SQLException {
    if (value == null) {
      stmt.setNull(index, Types.FLOAT);
    } else {
      stmt.setFloat(index, value);
    }
  }

  /**
   * 从ResultSet中获取可能为null的Integer值
   * @param rs ResultSet对象
   * @param columnName 列名
   * @return 列值，如果为NULL则返回null
   * @throws SQLException 如果获取列值时发生SQL错误
   */
  public static Integer getNullableInt(ResultSet rs, String columnName) throws SQLException {
    int value = rs.getInt(columnName);
    return rs.wasNull() ? null : value;
  }

  /**
   * 从ResultSet中获取可能为null的Float值
   * @param rs ResultSet对象
   * @param columnName 列名
   * @return 列值，如果为NULL则返回null
   * @throws SQLException 如果获取列值时发生SQL错误
   */
  public static Float getNullableFloat(ResultSet rs, String columnName) throws SQLException {
    float value = rs.getFloat(columnName);
    return rs.wasNull() ? null : value;
  }

  /**
   * 为SQL WHERE子句构建处理null值的条件
   * 例如：columnName = ? OR (columnName IS NULL AND ? IS NULL)
   * @param columnName 列名
   * @return 处理null值的WHERE条件
   */
  public static String nullableEqualCondition(String columnName) {
    return columnName + " = ? OR (" + columnName + " IS NULL AND ? IS NULL)";
  }
}
