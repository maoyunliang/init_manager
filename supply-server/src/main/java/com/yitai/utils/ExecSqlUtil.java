package com.yitai.utils;

import com.yitai.config.CreateTableConfig;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Map;

/**
 * ClassName: ExecSqlUtil
 * Package: com.yitai.utils
 * Description: sql脚本执行工具
 *
 * @Author: 毛云亮
 * @Create: 2023/12/16 16:44
 * @Version: 1.0
 */
public class ExecSqlUtil {
    private static final CreateTableConfig CREATE_TABLE_CONFIG = SpringUtils.getBean(CreateTableConfig.class);

    private static final DataSource DATA_SOURCE = SpringUtils.getBean(DataSource.class);

    public static void execSql(String name, Map<String, String> replaceMap) throws SQLException {
        //获取sql模版
        String sql = CREATE_TABLE_CONFIG.get(name);
        for(Map.Entry<String,String> entry: replaceMap.entrySet()){
            sql =sql.replace(entry.getKey(),entry.getValue());
        }
        ScriptRunner scriptRunner = new ScriptRunner(DATA_SOURCE.getConnection());

        scriptRunner.runScript(new StringReader(sql));
    }
}
