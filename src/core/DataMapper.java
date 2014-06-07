package core;

import java.util.ArrayList;

public interface DataMapper {

    public void save(Object obj) throws DataMapperException;

    public Object load(long id, Class clazz) throws DataMapperException;

    public ArrayList<Object> loadAll(Class clazz) throws DataMapperException;

    public void update(long id, Object obj) throws DataMapperException;


}
