package com.szd.z000.base.common.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义导出Excel数据注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelExport {
    String name() default "";
    String dateFormat() default "";
    String readConverterExp() default "";
    ColumnType cellType() default ColumnType.STRING;
    double height() default 14;
    double width() default 16;
    String suffix() default "";
    String defaultValue() default "";
    String prompt() default "";
    String[] combo() default {};
    boolean isExport() default true;
    String targetAttr() default "";
    Type type() default Type.ALL;

    enum Type {
        ALL(0), EXPORT(1), IMPORT(2);
        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    enum ColumnType {
        NUMERIC(0), STRING(1);
        private final int value;

        ColumnType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
