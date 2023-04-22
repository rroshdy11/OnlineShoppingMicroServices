package com.example.onlineshopping.SellingLogs;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/v1/sellingLogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class SellingLogsResource {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();


    @POST
    @Path("/add")
    public String addToLog( SellingLogs sellingLogs) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(sellingLogs);
            entityManager.getTransaction().commit();
            return "Log Saved Successfully" ;
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while Saving log";
        }

    }

    @GET
    @Path("/getAllSellingLogs")
    public List<SellingLogs> getAllSellingLogs() {
        return entityManager.createQuery("SELECT s FROM SellingLogs s", SellingLogs.class).getResultList();
    }

    @DELETE
    @Path("/delete/{id}")
    public String deleteLog(@PathParam("id") int id) {
        try {
            entityManager.getTransaction().begin();
            SellingLogs sellingLog = entityManager.find(SellingLogs.class, id);
            if (sellingLog == null) {
                entityManager.getTransaction().rollback();
                return "Selling Company does not exist";
            }
            entityManager.remove(sellingLog);
            entityManager.getTransaction().commit();
            return "Selling Log Deleted Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while deleting";
        }
    }
}