package com.msolo.stockeye.service.db.build;

/**
 * Created by mSolo on 2014/8/17.
 */
public abstract class SchemaTranRec {

    protected final String DATABASE_TEMPLATE_TABLE_NAME = "SX000000";

    protected final String DATATYPE_INT = " integer, ";
    protected final String DATATYPE_LONG = " bigint, ";
    protected final String DATATYPE_TEXT = " text, ";
    protected final String DATATYPE_TEXT_CREATE_TABLE_END = " text);";

    protected String getCreationTableIndexTemplateSqlStatement() { return null; }
    protected String getCreationTableTemplateSqlStatement() { return null; }

    protected String getDropTableSqlStatement() {
        return "DROP TABLE " + DATABASE_TEMPLATE_TABLE_NAME + ";";
    }

    protected String getDatabaseName() { return null; }
    protected int getDatabaseVersion() { return 1; }

    protected String getDatabaseTemplateTableName() {
        return DATABASE_TEMPLATE_TABLE_NAME;
    }

    protected String getTemplateInsertSqlStatement() { return null; }

}
