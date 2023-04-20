package com.example.adminservices;

import Admin.Admin;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.SessionScoped;
import jakarta.jms.Session;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


@Path("/v1/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SessionScoped
public class AdminService implements Serializable {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();

    @EJB
    private Admin admin;

    @POST
    @Path("/register")
    public String register( Admin admin ){
        try {
            entityManager.getTransaction().begin();
            //if user exists return error
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 != null) {
                return "User already exists";
            }
            entityManager.persist(admin);
            entityManager.getTransaction().commit();
            return "Registered Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while registering";
        }


    }
    @GET
    @Path("/login")
    public String login(@Context HttpServletRequest request, Admin admin){
        try {
            entityManager.getTransaction().begin();
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 == null) {
                entityManager.getTransaction().rollback();
                return "User does not exist";
            }
            if (admin1.getPassword().equals(admin.getPassword())) {
                request.getSession(true).setAttribute("username", admin1.getUsername());
                request.getSession(true).setAttribute("password", admin1.getPassword());
                this.admin = admin1;
                entityManager.getTransaction().commit();
                return "Logged in Successfully ";
            }

            return "Wrong password";

        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while logging in";
        }
    }
    @DELETE
    @Path("/logout")
    public String logout(@Context HttpServletRequest request){
        try {
            entityManager.getTransaction().begin();
            request.getSession().invalidate();
            entityManager.getTransaction().commit();
            return "Logged out Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while logging out";
        }
    }
    @GET
    @Path("/getAllAdmins")
    public Response getAllAdmins( @Context HttpServletRequest request ) {
        if (request.getSession(false).getAttribute("username")== null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(entityManager.createQuery("SELECT a FROM Admin a", Admin.class).getResultList()).build();
    }

    @GET
    @Path("/getLoggedInAdmin")
    public Response getLoggedInAdmin( @Context HttpServletRequest request ) {
        if (request.getSession(false).getAttribute("username") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(this.admin).build();
    }



    @DELETE
    @Path("/deleteAdmin/{username}")
    public String deleteAdmin(@Context HttpServletRequest request,@PathParam("username") String username) {
        if (request.getSession(false).getAttribute("username") == null) {
            return "You are not logged in";
        }
        try {
            entityManager.getTransaction().begin();
            Admin admin = entityManager.find(Admin.class, username);
            if (admin == null) {
                entityManager.getTransaction().rollback();
                return "User does not exist";
            }
            entityManager.remove(admin);
            entityManager.getTransaction().commit();
            return "User Deleted Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while deleting";
        }
    }


    @PUT
    @Path("/updateAdmin")
    public String updateAdmin(@Context HttpServletRequest request,Admin admin) {
        if (request.getSession(false).getAttribute("username")== null) {
            return "You are not logged in";
        }

        try {
            entityManager.getTransaction().begin();
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 == null) {
                entityManager.getTransaction().rollback();
                return "User does not exist";
            }
            entityManager.merge(admin);
            entityManager.getTransaction().commit();
            return "User Updated Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while updating";
        }
    }
    @POST
    @Path("/addSellingCompany/{sellingCompanyName}")
    public String addSellingCompany(@Context HttpServletRequest request,@PathParam("sellingCompanyName") String sellingCompanyName) {
        if (request.getSession(false).getAttribute("username")== null) {
            return "You are not logged in";
        }
        //send HTTPrequest to selling company to add selling company
        String url = "http://localhost:8080/SellingCompany-1.0-SNAPSHOT/api/v1/sellingCompany/add/" + sellingCompanyName;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}