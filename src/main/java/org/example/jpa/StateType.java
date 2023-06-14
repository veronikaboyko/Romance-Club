package org.example.jpa;

import org.example.keyboard.FinalStateAutomate;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class StateType extends EnumType<FinalStateAutomate> {

  @Override
  public void nullSafeSet(
      PreparedStatement st, Object o, int index, SharedSessionContractImplementor session)
      throws SQLException {
    if (o == null) {
      st.setNull(index, Types.OTHER);
    } else {
      st.setObject(index, ((FinalStateAutomate) o).name(), Types.OTHER);
    }
  }

  @Override
  public Object nullSafeGet(
      ResultSet rs,
      String[] position,
      SharedSessionContractImplementor sessionContractImplementor,
      Object owner)
      throws SQLException {
    if (rs.wasNull()) {
      return null;
    }
    String label = rs.getString(Integer.parseInt(position[0]));
    return FinalStateAutomate.valueOf(label);
  }
}
