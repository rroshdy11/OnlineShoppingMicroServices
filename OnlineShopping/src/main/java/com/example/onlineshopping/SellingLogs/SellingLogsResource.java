package com.example.onlineshopping.SellingLogs;

import jakarta.annotation.Resource;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.Singleton;
import jakarta.jms.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.Serializable;
import java.lang.IllegalStateException;
import java.util.ArrayList;
import java.util.List;

@Path("/v1/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton

public class SellingLogsResource{
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();
    @Resource(mappedName = "java:/jms/queue/orders")
    private Queue shippingRequestsQueue;


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


    public void submitOrder(SellingLog request) {
        try {
            Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("java:/ConnectionFactory");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(this.shippingRequestsQueue);
            ObjectMessage message = session.createObjectMessage();
            String text = request.getCustomerName()+","+
                    "(Your Address:"+request.getShippingAddress()+"):"+"( Your Product ID:"+request.getProductId()+"):"+
                    "(Your Selling Company Name:"+request.getSellingCompanyName()+"):"+
                    "(Your Shipping Company Name:"+request.getShippingCompanyName()+")";
            message.setObject(text);
            producer.send(message);
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GET
    @Path("/proccessShippingRequest/{id}/{shippingCompanyName}")
    public String proccessShippingRequest(@PathParam("id") int id,@PathParam("shippingCompanyName") String shippingCompanyName) {
        try {
            entityManager.getTransaction().begin();
            SellingLog sellingLog = entityManager.find(SellingLog.class, id);
            if (sellingLog == null) {
                entityManager.getTransaction().rollback();
                return "Selling Log does not exist";
            }
            sellingLog.setShippingState("Shipping Company Assigned");
            sellingLog.setShippingCompanyName(shippingCompanyName);
            submitOrder(sellingLog);
            entityManager.merge(sellingLog);
            entityManager.getTransaction().commit();
            return "Selling Log Updated Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while updating";
        }
    }
}