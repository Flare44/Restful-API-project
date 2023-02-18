package de.hhu.cs.dbs.propra.application.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.sql.SQLException;

@Provider
public class SQLExceptionMapper implements ExceptionMapper<SQLException> {
    @Override
    public Response toResponse(SQLException exception) {
        int code = 500;
        if (exception.getErrorCode() == 19) code = 400;
        return Response.status(code).entity(new APIError(exception)).build();
    }
}
