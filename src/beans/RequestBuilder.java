package beans;


import core.DataMapper;

public class RequestBuilder implements MessageBuilder{
    private DataMapper dataMapper;

    public DataMapper getDataMapper() {
        return dataMapper;
    }

    public void setDataMapper(DataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    public void process(byte[] message) {

    }
}
