package com.havaz.transport.dao.hibernate.dialect;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.spatial.dialect.mysql.MySQLSpatialDialect;
import org.hibernate.type.LocalDateTimeType;
import org.hibernate.type.LocalDateType;
import org.hibernate.type.LocalTimeType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.sql.Types;

@SuppressWarnings("unused")
public class MySQLHavazDialect extends MySQLSpatialDialect {
    private static final long serialVersionUID = 1L;

    public MySQLHavazDialect() {
        super();
        registerHibernateType(Types.TIME, LocalTimeType.INSTANCE.getName());
        registerHibernateType(Types.DATE, LocalDateType.INSTANCE.getName());
        registerHibernateType(Types.TIMESTAMP, LocalDateTimeType.INSTANCE.getName());
        registerHibernateType(Types.BIGINT, LongType.INSTANCE.getName());
        registerHibernateType(Types.CHAR, 1, StringType.INSTANCE.getName());
        registerFunction("group_concat", new StandardSQLFunction("group_concat", new StringType()));
    }

}
