package com.ksptooi.uac.commons.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.InputStream;
import java.sql.*;

public class InputStreamTypeHandler extends BaseTypeHandler<InputStream> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, InputStream is, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public InputStream getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getBinaryStream(columnName);
    }

    @Override
    public InputStream getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBinaryStream(columnIndex);
    }

    @Override
    public InputStream getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getBlob(columnIndex).getBinaryStream();
    }
}
