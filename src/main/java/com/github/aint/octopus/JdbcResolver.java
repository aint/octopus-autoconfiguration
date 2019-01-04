package com.github.aint.octopus;

public class JdbcResolver {

    private static final String MY_SQL = "MySQL";
    private static final String ORACLE = "Oracle";
    private static final String DB_2 = "DB2";
    private static final String SYBASE = "Sybase";
    private static final String TERADATA = "Teradata";
    private static final String SQL_SERVER = "SQLServer";
    private static final String POSTGRE_SQL = "PostgreSql";

    public static String getDbName(String jdbcDriverClassName) {
        if (jdbcDriverClassName.contains(MY_SQL.toLowerCase())) {
            return MY_SQL;
        } else if (jdbcDriverClassName.contains(ORACLE.toLowerCase())) {
            return ORACLE;
        } else if (jdbcDriverClassName.contains(DB_2.toLowerCase())) {
            return DB_2;
        } else if (jdbcDriverClassName.contains(SYBASE.toLowerCase())) {
            return SYBASE;
        } else if (jdbcDriverClassName.contains(TERADATA.toLowerCase())) {
            return TERADATA;
        } else if (jdbcDriverClassName.contains(SQL_SERVER.toLowerCase())) {
            return SQL_SERVER;
        } else if (jdbcDriverClassName.contains(POSTGRE_SQL.toLowerCase())) {
            return POSTGRE_SQL;
        } else {
            return "Unknown DB";
        }
    }

}