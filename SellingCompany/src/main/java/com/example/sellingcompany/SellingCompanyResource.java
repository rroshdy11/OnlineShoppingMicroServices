package com.example.sellingcompany;

import SellingCompany.SellingCompany;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/v1/sellingcompany")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class SellingCompanyResource {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();
    @GET
    public String hello() {
        return "Hello, World!";
    }
    @GET
    @Path("/getAllSellingCompanies")
    public List<SellingCompany> getAllAdmins() {
        return entityManager.createQuery("SELECT s FROM SellingCompany s", SellingCompany.class).getResultList();
    }

    @POST
    @Path("/register")
    public String register( SellingCompany sellingCompany) {
        try {
            entityManager.getTransaction().begin();
            //if user exists return error
            SellingCompany sellingCompany1 = entityManager.find(SellingCompany.class, sellingCompany.getName());
            if (sellingCompany1 != null) {
                return "Selling Company already exists";
            }
            entityManager.persist(sellingCompany);
            entityManager.getTransaction().commit();
            return "Registered Successfully" ;
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while registering";
        }

    }

    @GET
    @Path("/login")
    public String login(SellingCompany sellingCompany){
        try {
            entityManager.getTransaction().begin();
            SellingCompany sellingCompany1 = entityManager.find(SellingCompany.class,sellingCompany.getName());
            if (sellingCompany1 == null) {
                entityManager.getTransaction().rollback();
                return "Selling Company does not exist";
            }
            if (sellingCompany1.getPassword().equals(sellingCompany.getPassword())) {
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
    @Path("/deleteSellingCompany/{name}")
    public String deleteAdmin(@PathParam("name") String name) {
        try {
            entityManager.getTransaction().begin();
            SellingCompany sellingCompany = entityManager.find(SellingCompany.class, name);
            if (sellingCompany == null) {
                entityManager.getTransaction().rollback();
                return "Selling Company does not exist";
            }
            entityManager.remove(sellingCompany);
            entityManager.getTransaction().commit();
            return "Selling Company Deleted Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while deleting";
        }
    }
}