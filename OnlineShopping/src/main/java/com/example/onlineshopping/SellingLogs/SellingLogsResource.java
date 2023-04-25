package com.example.onlineshopping.SellingLogs;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/v1/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class SellingLogsResource {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();


    @POST
    @Path("/add")
    public String addToLog( SellingLog order) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(order);
            entityManager.getTransaction().commit();
            return "Log Saved Successfully" ;
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while Saving log";
        }

    }

    @GET
    @Path("/getAllSellingLogs")
    public List<SellingLog> getAllSellingLogs() {
        return entityManager.createQuery("SELECT s FROM SellingLog s", SellingLog.class).getResultList();
    }

    //get all Selling Logs with State =Shipping Request and Shipping Company = null and Shipping Address={Geos}
    @GET
    @Path("/getShippingRequests/{Geo}")
    public List<SellingLog> getShippingRequests(@PathParam("Geo") String geo) {
        String [] geoArray = geo.split(",");
        List<SellingLog> sellingLog= entityManager.createQuery("SELECT s FROM SellingLog s WHERE s.shippingState = 'Shipping Request' AND s.shippingCompanyName IS NULL", SellingLog.class).getResultList();
        List<SellingLog> sellingLogsInGeo=new ArrayList<>();
        for(SellingLog sellingLog1:sellingLog){
            for(String geo1:geoArray){
                if(sellingLog1.getShippingAddress().contains(geo1)){
                    sellingLogsInGeo.add(sellingLog1);
                }
            }
        }
        return sellingLogsInGeo;
    }
    @DELETE
    @Path("/delete/{id}")
    public String deleteLog(@PathParam("id") int id) {
        try {
            entityManager.getTransaction().begin();
            SellingLog sellingLog = entityManager.find(SellingLog.class, id);
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