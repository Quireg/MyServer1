package core;

import exampleClasses.Column;
import exampleClasses.Table;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class QueryBuilder {
    public static String buildInsertSql(Object o) throws FileNotFoundException {
       String tableName = o.getClass().getAnnotation(Table.class).name();
       ArrayList<String> arr = new ArrayList<>();

        for(Field field : o.getClass().getDeclaredFields()){
            String columnName= field.getAnnotation(Column.class).name();
            arr.add(columnName);
        }



        String params = "";
        String values = "";
        for (int i = 0; i < arr.size(); i++) {
            if(i != arr.size() -1){
                values = values + "?,";
                params = params + arr.get(i) + ", ";
            }
            else if(i == arr.size() -1){
                params = params + arr.get(i);
            values = values + "?";}

        }

        String sql = "INSERT INTO "+ tableName+ " ("+ params +") VALUES " + "("+values+")";
        System.out.println(sql);
        return sql;
    }
    public static String buildUpdateSql(Object o, long id) throws FileNotFoundException{
        String tableName = o.getClass().getAnnotation(Table.class).name();
        ArrayList<String> arr = new ArrayList<>();

        for(Field field : o.getClass().getDeclaredFields()){
            String columnName= field.getAnnotation(Column.class).name();
            arr.add(columnName);
        }



        String params = "";
        for (int i = 0; i < arr.size(); i++) {
            if(i != arr.size() -1){

                params = params + arr.get(i) + "=?, ";
            }
            else if(i == arr.size() -1){
                params = params + arr.get(i) + "=?";
               }

        }

        String sql = "UPDATE "+ tableName+ " SET " + params + " WHERE id"+tableName+"="+id;

        System.out.println(sql);
        return sql;
    }
}
