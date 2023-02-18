package de.hhu.cs.dbs.propra.presentation.rest;

import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.Map;

@Path("/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class ExampleController {
    @Inject
    private DataSource dataSource;

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    /*

    @GET // GET http://localhost:8080
    public String halloWelt() {
        return "Hallo Welt!";
    }

    @Path("foo")
    @RolesAllowed({"USER", "EMPLOYEE", "ADMIN"})
    @GET
    // GET http://localhost:8080/foo => OK, wenn Benutzer die Rolle "USER", "EMPLOYEE" oder "ADMIN" hat. Siehe SQLiteUserRepository.
    public String halloFoo() {
        return "Hallo " + securityContext.getUserPrincipal() + "!";
    }

    @Path("foo2/{bar}")
    @GET // GET http://localhost:8080/foo/xyz
    public String halloFoo2(@PathParam("bar") String bar) {
        if (!bar.equals("foo")) throw new NotFoundException("Resource '" + bar + "' not found");
        return "Hallo " + bar + "!";
    }

    @Path("foo3")
    @GET // GET http://localhost:8080/foo3?bar=xyz
    public String halloFoo3(@QueryParam("bar") String bar) {
        return "Hallo " + bar + "!";
    }

    @Path("bar")
    @GET // GET http://localhost:8080/bar => Bad Request; http://localhost/bar?foo=xyz => OK
    public Response halloBar(@QueryParam("foo") String foo) {
        if (foo == null) throw new BadRequestException();
        return Response.status(Response.Status.OK).entity("Hallo Bar!").build();
    }

    @Path("bar2")
    @GET // GET http://localhost:8080/bar2
    public List<Map<String, Object>> halloBar2(@QueryParam("name") @DefaultValue("Max Mustermann") List<String> names) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ?;");
        preparedStatement.closeOnCompletion();
        int random = ThreadLocalRandom.current().nextInt(0, names.size());
        preparedStatement.setObject(1, names.get(random));
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new HashMap<>();
            entity.put("name", resultSet.getObject(1));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("foo")
    @POST // POST http://localhost:8080/foo
    public Response einUpload(@FormDataParam("name") String name, @FormDataParam("file") InputStream file) {
        if (name == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (file == null) return Response.status(Response.Status.BAD_REQUEST).build();
        byte[] bytes;
        try {
            bytes = IOUtils.toByteArray(file);
        } catch (IOException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.created(uriInfo.getAbsolutePathBuilder().path("234235").build()).build();
    }

    */

    @Path("nutzer")
    @GET // GET http://localhost:8080/nutzer
    public List<Map<String, Object>> getNutzer(@QueryParam("email") String email) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT rowid, * FROM Nutzer;");
        if (email != null) {
            preparedStatement = connection.prepareStatement("SELECT rowid, * FROM Nutzer WHERE E_Mail_Adresse LIKE ?;");
            preparedStatement.setObject(1, "%" + email + "%");
        }
        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("email", resultSet.getObject(2));
            entity.put("passwort", resultSet.getObject(3));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }


    @Path("nutzer")
    @POST // POST http://localhost:8080/nutzer
    public Response postNutzer(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (email == null || passwort == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, passwort);
            preparedStatement.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(email).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } finally {
            connection.close();
        }
    }

    @Path("kunden")
    @GET // GET http://localhost:8080/nutzer
    public List<Map<String, Object>> getKunden(@QueryParam("email") String email, @QueryParam("telefonnummer") String telefonnummer) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Nutzer.rowid, Kunde.rowid, * FROM Nutzer, Kunde WHERE Kunde.E_Mail_Adresse = Nutzer.E_Mail_Adresse;");
        if (email != null && telefonnummer != null) {
            preparedStatement = connection.prepareStatement("SELECT n.ROWID, k.ROWID ,* FROM Nutzer n, Kunde k WHERE k.E_Mail_Adresse = n.E_Mail_Adresse AND k.E_Mail_Adresse LIKE ? AND k.Telefonnummer LIKE ?;");
            preparedStatement.setObject(1, "%" + email + "%");
            preparedStatement.setObject(2, "%" + telefonnummer + "%");
        }
        if (email != null && telefonnummer == null) {
            preparedStatement = connection.prepareStatement("SELECT n.ROWID, k.ROWID ,* FROM Nutzer n, Kunde k WHERE k.E_Mail_Adresse = n.E_Mail_Adresse AND k.E_Mail_Adresse LIKE ?;");
            preparedStatement.setObject(1, "%" + email + "%");
        }
        //preparedStatement.closeOnCompletion();
        if (telefonnummer != null && email == null) {
            preparedStatement = connection.prepareStatement("SELECT n.ROWID, k.ROWID,* FROM Nutzer n, Kunde k WHERE k.E_Mail_Adresse = n.E_Mail_Adresse AND k.Telefonnummer LIKE ?;");
            preparedStatement.setObject(1, "%" + telefonnummer + "%");
        }
        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("kundenid", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entity.put("telefon", resultSet.getObject(5));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("kunden")
    @POST // POST http://localhost:8080/nutzer
    public Response postKunden(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("telefon") String telefon) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (email == null || passwort == null || telefon == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, passwort);
            preparedStatement.executeUpdate();

            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO Kunde VALUES (?,?);");
            preparedStatement1.closeOnCompletion();
            preparedStatement1.setObject(1, telefon);
            preparedStatement1.setObject(2, email);
            preparedStatement1.executeUpdate();


            return Response.created(uriInfo.getAbsolutePathBuilder().path(email).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } finally {
            connection.close();
        }
    }

    @Path("projektleiter")
    @GET // GET http://localhost:8080/nutzer
    public List<Map<String, Object>> getProjektleiter(@QueryParam("email") String email, @QueryParam("gehalt") String gehalt) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT n.ROWID, p.ROWID, * FROM Nutzer n, Projektleiter p WHERE n.E_Mail_Adresse = p.E_Mail_Adresse;");

        if (email != null && gehalt != null) {
            preparedStatement = connection.prepareStatement("SELECT n.ROWID, p.ROWID, * FROM Nutzer n, Projektleiter p WHERE n.E_Mail_Adresse = p.E_Mail_Adresse AND p.E_Mail_Adresse LIKE ? AND p.gehalt > ?;");
            preparedStatement.setObject(1, "%" + email + "%");
            preparedStatement.setObject(2, gehalt);
        }
        if (email != null && gehalt == null) {
            preparedStatement = connection.prepareStatement("SELECT n.ROWID, p.ROWID, * FROM Nutzer n, Projektleiter p WHERE n.E_Mail_Adresse = p.E_Mail_Adresse AND p.E_Mail_Adresse LIKE ?;");
            preparedStatement.setObject(1, "%" + email + "%");
        }
        preparedStatement.closeOnCompletion();
        if (gehalt != null && email == null) {
            preparedStatement = connection.prepareStatement("SELECT n.ROWID, p.ROWID, * FROM Nutzer n, Projektleiter p WHERE n.E_Mail_Adresse = p.E_Mail_Adresse AND p.Gehalt > ?;");
            preparedStatement.setObject(1, gehalt);
        }
        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("projektleiterid", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entity.put("gehalt", resultSet.getObject(6));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("projektleiter")
    @POST // POST http://localhost:8080/nutzer
    public Response postProjektleiter(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("gehalt") Integer gehalt) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (email == null || passwort == null || gehalt == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, passwort);
            preparedStatement.executeUpdate();

            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO Projektleiter VALUES (?,?);");
            preparedStatement1.closeOnCompletion();
            preparedStatement1.setObject(1, email);
            preparedStatement1.setObject(2, gehalt);
            preparedStatement1.executeUpdate();


            return Response.created(uriInfo.getAbsolutePathBuilder().path(email).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } finally {
            connection.close();
        }
    }

    @Path("spezialisten")
    @GET // GET http://localhost:8080/nutzer
    public List<Map<String, Object>> getSpezialisten(@QueryParam("email") String email, @QueryParam("verfuegbar") String verfuegbar) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT n.ROWID, s.ROWID, * FROM Nutzer n, Spezialist s WHERE n.E_Mail_Adresse = s.E_Mail_Adresse;");

        if (email != null && verfuegbar != null) {
            preparedStatement = connection.prepareStatement("SELECT n.ROWID, s.ROWID, * FROM Nutzer n, Spezialist s WHERE n.E_Mail_Adresse = s.E_Mail_Adresse AND s.E_Mail_Adresse LIKE ? AND s.Verfuegbarkeitsstatus = ?;");
            preparedStatement.setObject(1, "%" + email + "%");
            preparedStatement.setObject(2, verfuegbar);
        }
        if (email != null && verfuegbar == null) {
            preparedStatement = connection.prepareStatement("SELECT n.ROWID, s.ROWID, * FROM Nutzer n, Spezialist s WHERE n.E_Mail_Adresse = s.E_Mail_Adresse AND s.E_Mail_Adresse LIKE ?;");
            preparedStatement.setObject(1, "%" + email + "%");
        }
        preparedStatement.closeOnCompletion();
        if (verfuegbar != null && email == null) {
            preparedStatement = connection.prepareStatement("SELECT n.ROWID, s.ROWID, * FROM Nutzer n, Spezialist s WHERE n.E_Mail_Adresse = s.E_Mail_Adresse AND s.Verfuegbarkeitsstatus = ?;");
            preparedStatement.setObject(1, verfuegbar);
        }
        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("spezialistid", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entity.put("verfuegbarkeitsstatus", resultSet.getObject(6));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("spezialisten")
    @POST // POST http://localhost:8080/nutzer
    public Response postSpezialisten(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("verfuegbarkeitsstatus") String verfuegbarkeitsstatus) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (email == null || passwort == null || verfuegbarkeitsstatus == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, passwort);
            preparedStatement.executeUpdate();

            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO Spezialist VALUES (?,?);");
            preparedStatement1.closeOnCompletion();
            preparedStatement1.setObject(1, email);
            preparedStatement1.setObject(2, verfuegbarkeitsstatus);
            preparedStatement1.executeUpdate();


            return Response.created(uriInfo.getAbsolutePathBuilder().path(email).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } finally {
            connection.close();
        }
    }

    @Path("projekte")
    @GET // GET http://localhost:8080/nutzer
    public List<Map<String, Object>> getProjekte() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, Projektdeadline, Projektname FROM Projekt;");
        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("projektid", resultSet.getObject(1));
            entity.put("name", resultSet.getObject(3));
            entity.put("deadline", resultSet.getObject(2));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("entwickler")
    @GET // GET http://localhost:8080/nutzer
    public List<Map<String, Object>> getEntwickler(@QueryParam("kuerzel") String kuerzel) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT n.ROWID, s.ROWID, e.ROWID, e.E_Mail_Adresse, n.Passwort, s.Verfuegbarkeitsstatus, e.Kuerzel FROM Entwickler e, Spezialist s, Nutzer n WHERE e.E_Mail_Adresse = s.E_Mail_Adresse AND n.E_Mail_Adresse = s.E_Mail_Adresse;");
        if (kuerzel != null) {
            preparedStatement = connection.prepareStatement("SELECT n.ROWID, s.ROWID, e.ROWID, e.E_Mail_Adresse, n.Passwort, s.Verfuegbarkeitsstatus, e.Kuerzel FROM Entwickler e, Spezialist s, Nutzer n WHERE e.E_Mail_Adresse = s.E_Mail_Adresse AND n.E_Mail_Adresse = s.E_Mail_Adresse AND e.Kuerzel LIKE ?;");
            preparedStatement.setObject(1, "%" + kuerzel + "%");
        }
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("spezialistid", resultSet.getObject(2));
            entity.put("entwicklerid", resultSet.getObject(3));
            entity.put("email", resultSet.getObject(4));
            entity.put("passwort", resultSet.getObject(5));
            entity.put("verfuegbarkeitsstatus", resultSet.getObject(6));
            entity.put("kuerzel", resultSet.getObject(7));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("projekte/{projektid}/bewertungen")
    @GET // GET http://localhost:8080/nutzer
    public List<Map<String, Object>> getBewertungen(@PathParam("projektid") Integer projektid) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT b.id, b.Bepunktung, b.Text FROM Bewertung b WHERE b.Projekt_id = ?;");
        preparedStatement.closeOnCompletion();
        preparedStatement.setObject(1, projektid);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("bewertungid", resultSet.getObject(1));
            entity.put("punktzahl", resultSet.getObject(2));
            entity.put("text", resultSet.getObject(3));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("projekte/{projektid}/aufgaben")
    @GET // GET http://localhost:8080/nutzer
    public List<Map<String, Object>> getAufgaben(@PathParam("projektid") Integer projektid) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT a.ROWID, Deadline, Beschreibung, Status, Priorisierung FROM Aufgabe a WHERE Projekt_id = ?;");
        preparedStatement.closeOnCompletion();
        preparedStatement.setObject(1, projektid);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("aufgabeid", resultSet.getObject(1));
            entity.put("deadline", resultSet.getObject(2));
            entity.put("beschreibung", resultSet.getObject(3));
            entity.put("status", resultSet.getObject(4));
            entity.put("prioritaet", resultSet.getObject(5));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("projekte/{projektid}/spezialisten")
    @GET // GET http://localhost:8080/nutzer
    public List<Map<String, Object>> getSpezialisten(@PathParam("projektid") Integer projektid) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT s.ROWID, s.Verfuegbarkeitsstatus, s.E_Mail_Adresse, n.Passwort FROM Spezialist s, Nutzer n, Spezialist_arbeitet_an_Projekt sap WHERE s.E_Mail_Adresse = n.E_Mail_Adresse AND s.E_Mail_Adresse = sap.E_Mail_Adresse AND sap.id = ?;");
        preparedStatement.closeOnCompletion();
        preparedStatement.setObject(1, projektid);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("spezialistid", resultSet.getObject(1));
            entity.put("verfuegbarkeitsstatus", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("entwickler")
    @POST // POST http://localhost:8080/nutzer
    public Response postEntwickler(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("verfuegbarkeitsstatus") String verfuegbarkeitsstatus, @FormDataParam("kuerzel") String kuerzel, @FormDataParam("benennung") String benennung) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (email == null || passwort == null || verfuegbarkeitsstatus == null || kuerzel == null || benennung == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, passwort);
            preparedStatement.executeUpdate();

            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO Spezialist VALUES (?,?);");
            preparedStatement1.closeOnCompletion();
            preparedStatement1.setObject(1, email);
            preparedStatement1.setObject(2, verfuegbarkeitsstatus);
            preparedStatement1.executeUpdate();

            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Entwickler VALUES (?,?);");
            preparedStatement2.closeOnCompletion();
            preparedStatement2.setObject(1, kuerzel);
            preparedStatement2.setObject(2, email);
            preparedStatement2.executeUpdate();

            boolean duplicate = checkDuplicate(benennung);
            PreparedStatement preparedStatement3;
            if(!duplicate){
                preparedStatement3 = connection.prepareStatement("INSERT INTO Programmiersprache VALUES (?);");
                preparedStatement3.closeOnCompletion();
            }
            else {
                preparedStatement3 = connection.prepareStatement("UPDATE Programmiersprache SET Name = ? WHERE Name = ?;");
                preparedStatement3.closeOnCompletion();
                preparedStatement3.setObject(1, benennung);
                preparedStatement3.setObject(2, benennung);
            }
            preparedStatement3.setObject(1, benennung);
            preparedStatement3.executeUpdate();

            PreparedStatement preparedStatement4 = connection.prepareStatement("INSERT INTO Entwickler_beherrscht_Programmiersprache VALUES (?,?,1);");
            preparedStatement4.closeOnCompletion();
            preparedStatement4.setObject(1, kuerzel);
            preparedStatement4.setObject(2, benennung);
            preparedStatement4.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(email).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } finally {
            connection.close();
        }
    }

    private boolean checkDuplicate(String name) throws SQLException{
        Connection connection = dataSource.getConnection();
        boolean check = true;
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Name FROM Programmiersprache WHERE Name = ?;");
        preparedStatement.closeOnCompletion();
        preparedStatement.setObject(1,name);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("Name", resultSet.getObject(1));
            entities.add(entity);
        }
        if(entities.isEmpty()){
            check = false;
        }
        resultSet.close();
        connection.close();
        return check;
    }

    @Path("programmierer")
    @GET // GET http://localhost:8080/nutzer
    public Response getProgrammierer() throws SQLException {
        URI uri = uriInfo.getBaseUriBuilder().path("entwickler").build();
        return Response.status(Response.Status.MOVED_PERMANENTLY).location(uri).build();
    }


    @Path("projekte")
    @RolesAllowed({"KUNDE"})
    @POST // POST http://localhost:8080/nutzer
    public Response postProjekte(@FormDataParam("name") String name, @FormDataParam("deadline") String deadline)throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (name == null || deadline == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT  Telefonnummer FROM Kunde WHERE E_Mail_Adresse = ?");
            preparedStatement1.closeOnCompletion();
            String emailVonKunde= securityContext.getUserPrincipal().getName();
            preparedStatement1.setString(1, emailVonKunde);
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object telefonnummer = resultSet.getObject(1);

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Projekt VALUES (?,?,?,'leber1763@icloud.com',?);");
            preparedStatement.closeOnCompletion();
            int id = getIDProjekte();
            preparedStatement.setObject(1, String.valueOf(id));
            preparedStatement.setObject(2, deadline);
            preparedStatement.setObject(3, name);
            preparedStatement.setObject(4, telefonnummer);
            preparedStatement.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(name).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }




    @Path("projekte/{projektid}/bewertungen")
    @RolesAllowed({"KUNDE"})
    @POST // POST http://localhost:8080/nutzer
    public Response postBewertungen(@PathParam("projektid") Integer projektid, @FormDataParam("punktzahl") Integer punktzahl, @FormDataParam("text") String text)throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (projektid == null || punktzahl == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT  Telefonnummer FROM Kunde WHERE E_Mail_Adresse = ?");
            preparedStatement1.closeOnCompletion();
            String kundeTelefonnummer= securityContext.getUserPrincipal().getName();
            preparedStatement1.setString(1, kundeTelefonnummer);
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object telefonnummer = resultSet.getObject(1);


            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM Projekt WHERE id = ?;");
            preparedStatement3.closeOnCompletion();
            preparedStatement3.setObject(1, projektid);
            ResultSet resultSet3 = preparedStatement3.executeQuery();

            if(!resultSet3.next()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Bewertung VALUES (?,?,?,?,?);");
            preparedStatement.closeOnCompletion();
            int id = getIDBewertung();
            preparedStatement.setObject(1, String.valueOf(id));
            preparedStatement.setObject(2, text);
            preparedStatement.setObject(3, punktzahl);
            preparedStatement.setObject(4, telefonnummer);
            preparedStatement.setObject(5, projektid);
            preparedStatement.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }



    @Path("bewertungen/{bewertungid}")
    @RolesAllowed({"KUNDE"})
    @PATCH // POST http://localhost:8080/nutzer
    public Response patchBewertungen(@PathParam("bewertungid") Integer bewertungid, @FormDataParam("punktzahl") Integer punktzahl, @FormDataParam("text") String text) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (bewertungid == null || punktzahl == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            String nutzerEmail = securityContext.getUserPrincipal().getName();
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT Telefonnummer FROM Kunde WHERE E_Mail_Adresse = ?;");
            preparedStatement1.setObject(1, nutzerEmail);
            preparedStatement1.executeQuery();
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object telefonnummer = resultSet.getObject(1);

            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM Bewertung WHERE id = ?;");
            preparedStatement3.setObject(1, bewertungid);
            preparedStatement3.executeQuery();
            ResultSet resultSet3 = preparedStatement3.executeQuery();

            if (!resultSet3.next()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM Bewertung WHERE id = ? AND Telefonnummer = ?;");
            preparedStatement2.setObject(1, bewertungid);
            preparedStatement2.setObject(2, telefonnummer);
            preparedStatement2.executeQuery();
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            if (!resultSet2.next()) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }


            if (text == null) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Bewertung SET Bepunktung = ? WHERE id = ? AND Telefonnummer = ?;");
                preparedStatement.closeOnCompletion();
                preparedStatement.setObject(1, punktzahl);
                preparedStatement.setObject(2, bewertungid);
                preparedStatement.setObject(3, telefonnummer);
                preparedStatement.executeUpdate();
            } else {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Bewertung SET Bepunktung = ?, Text = ? WHERE id = ? AND Telefonnummer = ?;");
                preparedStatement.closeOnCompletion();
                preparedStatement.setObject(1, punktzahl);
                preparedStatement.setObject(2, text);
                preparedStatement.setObject(3, bewertungid);
                preparedStatement.setObject(4, telefonnummer);
                preparedStatement.executeUpdate();
            }

            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } finally {
            connection.close();
        }
    }

    @Path("bewertungen/{bewertungid}")
    @RolesAllowed({"KUNDE"})
    @DELETE // POST http://localhost:8080/nutzer
    public Response deleteBewertungen(@PathParam("bewertungid") Integer bewertungid) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (bewertungid == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            String nutzerEmail = securityContext.getUserPrincipal().getName();
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT Telefonnummer FROM Kunde WHERE E_Mail_Adresse = ?;");
            preparedStatement1.setObject(1, nutzerEmail);
            preparedStatement1.executeQuery();
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object telefonnummer = resultSet.getObject(1);

            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM Bewertung WHERE id = ?;");
            preparedStatement3.setObject(1, bewertungid);
            preparedStatement3.executeQuery();
            ResultSet resultSet3 = preparedStatement3.executeQuery();

            if (!resultSet3.next()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM Bewertung WHERE id = ? AND Telefonnummer = ?;");
            preparedStatement2.setObject(1, bewertungid);
            preparedStatement2.setObject(2, telefonnummer);
            preparedStatement2.executeQuery();
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            if (!resultSet2.next()) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Bewertung WHERE id = ? AND Telefonnummer = ?;");
            preparedStatement.setObject(1, bewertungid);
            preparedStatement.setObject(2, telefonnummer);
            preparedStatement.executeUpdate();
            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } finally {
            connection.close();
        }
    }

    @Path("projekte/{projektid}/aufgaben")
    @RolesAllowed({"PROJEKTLEITER"})
    @POST // POST http://localhost:8080/nutzer
    public Response postProjekteAufgaben(@PathParam("projektid") Integer projektid, @FormDataParam("deadline") String deadline, @FormDataParam("beschreibung") String beschreibung, @FormDataParam("status") String status, @FormDataParam("prioritaet") String prioritaet) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (projektid == null || deadline == null || beschreibung == null || status == null || prioritaet == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM Projekt WHERE id = ?;");
            preparedStatement3.setObject(1, projektid);
            preparedStatement3.executeQuery();
            ResultSet resultSet3 = preparedStatement3.executeQuery();

            if (!resultSet3.next()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            /*
            String nutzerEmail = securityContext.getUserPrincipal().getName();
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT id FROM Projekt WHERE E_Mail_Adresse = ?;");
            preparedStatement1.setObject(1, nutzerEmail);
            preparedStatement1.executeQuery();
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object currentProjektid = resultSet.getObject(1);


            if (!currentProjektid.equals(projektid)) {
                System.out.println(currentProjektid);
                System.out.println(String.valueOf(projektid));
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            */


            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Aufgabe VALUES (?,?,?,?,?,?);");
            preparedStatement.closeOnCompletion();
            int id = getIDAufgabe();
            preparedStatement.setObject(1, String.valueOf(id));
            preparedStatement.setObject(2, deadline);
            preparedStatement.setObject(3, beschreibung);
            preparedStatement.setObject(4, status);
            preparedStatement.setObject(5, prioritaet);
            preparedStatement.setObject(6, projektid);
            preparedStatement.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } finally {
            connection.close();
        }
    }

    @Path("projekte/{projektid}/spezialisten")
    @RolesAllowed({"PROJEKTLEITER"})
    @POST // POST http://localhost:8080/nutzer
    public Response postSpezialistInProjekte(@PathParam("projektid") Integer projektid, @FormDataParam("spezialistid") String spezialistid) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (projektid == null || spezialistid == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM Projekt WHERE id = ?;");
            preparedStatement3.setObject(1, projektid);
            preparedStatement3.executeQuery();
            ResultSet resultSet3 = preparedStatement3.executeQuery();

            if (!resultSet3.next()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            PreparedStatement preparedStatement4 = connection.prepareStatement("SELECT * FROM Spezialist WHERE ROWID = ?;");
            preparedStatement4.setObject(1, spezialistid);
            preparedStatement4.executeQuery();
            ResultSet resultSet4 = preparedStatement4.executeQuery();

            if (!resultSet4.next()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT E_Mail_Adresse FROM Spezialist WHERE ROWID = ?;");
            preparedStatement1.setObject(1, spezialistid);
            preparedStatement1.executeQuery();
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object emailSpezialist = resultSet.getObject(1);

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Spezialist_arbeitet_an_Projekt VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1, emailSpezialist);
            preparedStatement.setObject(2, projektid);
            preparedStatement.executeUpdate();


            return Response.created(uriInfo.getAbsolutePathBuilder().path(spezialistid).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } finally {
            connection.close();
        }
    }

    private int getIDProjekte() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ROWID) FROM Projekt;");
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 1 + resultSet.getInt(1);
        return id;
    }

    private int getIDBewertung() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ROWID) FROM Bewertung;");
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 1 + resultSet.getInt(1);
        return id;
    }

    private int getIDAufgabe() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ROWID) FROM Aufgabe;");
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 1 + resultSet.getInt(1);
        return id;
    }
}
