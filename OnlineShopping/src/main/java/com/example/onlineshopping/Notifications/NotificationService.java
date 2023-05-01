package com.example.onlineshopping.Notifications;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;

import java.util.List;

@Path("/v1/notification")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class NotificationService {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();
    @GET
    @Path("/getCustomerNotifications/{username}")
    public List<CustomerNotification> getCustomerNotifications(@PathParam("username") String username){
        return entityManager.createQuery("SELECT c FROM CustomerNotification c WHERE c.customer.username = :username", CustomerNotification.class).setParameter("username", username).getResultList();
    }


}
