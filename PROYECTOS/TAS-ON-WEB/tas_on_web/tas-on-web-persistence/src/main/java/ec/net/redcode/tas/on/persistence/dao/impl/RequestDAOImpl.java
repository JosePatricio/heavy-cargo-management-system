package ec.net.redcode.tas.on.persistence.dao.impl;

import ec.net.redcode.tas.on.persistence.dao.RequestDAO;
import ec.net.redcode.tas.on.persistence.entities.Request;

public class RequestDAOImpl extends GenericDAOImpl<Request, Integer> implements RequestDAO {

    public RequestDAOImpl(){
        super(Request.class);
    }

}
