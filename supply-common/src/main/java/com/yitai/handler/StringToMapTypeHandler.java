package com.yitai.handler;

import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: StringToMapTypeHandler
 * Package: com.yitai.handler
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/13 16:12
 * @Version: 1.0
 */
public class StringToMapTypeHandler extends BaseTypeHandler<Map<String,Integer>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    Map<String, Integer> parameter, JdbcType jdbcType) throws SQLException {
        // 设置非空参数，这里你可以自定义需求
        if (parameter != null) {
            ps.setString(i, mapToString(parameter));
        } else {
            ps.setNull(i, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public Map<String, Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return stringToMap(rs.getString(columnName));
    }

    @Override
    public Map<String, Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return stringToMap(rs.getString(columnIndex));
    }

    @Override
    public Map<String, Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return stringToMap(cs.getString(columnIndex));
    }

    // 将字符串转换成 Map 的方法
    private Map<String, Integer> stringToMap(String result) {
        Map<String, Integer> map = new HashMap<>();
        if (!StrUtil.isEmpty(result)) {
            String[] pairs = result.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    int value = Integer.parseInt(keyValue[1]);
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    private String mapToString(Map<String, Integer> map) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            result.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        return result.deleteCharAt(result.length() - 1).toString();
    }
}
