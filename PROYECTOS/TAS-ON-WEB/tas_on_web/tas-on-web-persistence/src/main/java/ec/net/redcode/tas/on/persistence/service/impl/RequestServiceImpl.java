package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.RequestDAO;
import ec.net.redcode.tas.on.persistence.entities.Request;

import ec.net.redcode.tas.on.persistence.service.RequestService;
import lombok.Setter;

public class RequestServiceImpl implements RequestService {

    @Setter
    private RequestDAO requestDAO;

    @Override
    public void create(Request request) {
        requestDAO.create(request);
    }

}
