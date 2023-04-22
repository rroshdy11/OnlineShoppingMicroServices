package com.example.ShoppingApp.SellingCompany;

import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.Serializable;

@Path("/v1/sellingCompany")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@SessionScoped
public class SellingCompanyController implements Serializable {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();

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
}
