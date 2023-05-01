package com.example.onlineshopping.SellingLogs;

import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.jms.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.PathParam;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.lang.IllegalStateException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SellingLogBean {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();
    @Resource(mappedName = "java:/jms/queue/orders")
    private Queue shippingRequestsQueue;

    public String addLog( SellingLog order) {
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
    public List<SellingLog> getAllLogs() {
        return entityManager.createQuery("SELECT s FROM SellingLog s", SellingLog.class).getResultList();
    }


    public List<SellingLog> getShippingRequestsInGeo(@PathParam("Geo") String geo) {
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

    public String acceptShippingRequest(@PathParam("id") int id, @PathParam("shippingCompanyName") String shippingCompanyName) {
        try {
            entityManager.getTransaction().begin();
            SellingLog sellingLog = entityManager.find(SellingLog.class, id);
            if (sellingLog == null) {
                entityManager.getTransaction().rollback();
                return "Selling Log does not exist";
            }
            sellingLog.setShippingState("Shipping Company Assigned");
            sellingLog.setShippingCompanyName(shippingCompanyName);
            pushToQueue(sellingLog,"Your Shipping Request has been accepted by "+shippingCompanyName+"--> Your Shipping Company Name:"+shippingCompanyName);
            entityManager.merge(sellingLog);
            entityManager.getTransaction().commit();
            return "Selling Log Updated Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while updating";
        }
    }
    public void pushToQueue(SellingLog request,String addmsg) {
        try {
            Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("java:/ConnectionFactory");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(this.shippingRequestsQueue);
            ObjectMessage message = session.createObjectMessage();
            String text = request.getCustomerName()+","+addmsg+
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
    public String assignDelivered(@PathParam("id") int id) {
        try {
            entityManager.getTransaction().begin();
            SellingLog sellingLog = entityManager.find(SellingLog.class, id);
            if (sellingLog == null) {
                entityManager.getTransaction().rollback();
                return "Selling Log does not exist";
            }
            sellingLog.setShippingState("Delivered");
            entityManager.merge(sellingLog);
            entityManager.getTransaction().commit();
            return "Selling Log Updated Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while updating";
        }
    }

    public List<SellingLog> getShippingRequestsByShippingCompany(@PathParam("shippingCompanyName") String shippingCompanyName) {
        return entityManager.createQuery("SELECT s FROM SellingLog s WHERE s.shippingCompanyName = :shippingCompanyName", SellingLog.class).setParameter("shippingCompanyName", shippingCompanyName).getResultList();
    }

    public String markAsDelivered(String id){
        try {
            entityManager.getTransaction().begin();
            SellingLog sellingLog = entityManager.find(SellingLog.class, Integer.parseInt(id));
            if (sellingLog == null) {
                entityManager.getTransaction().rollback();
                return "Selling Log does not exist";
            }
            sellingLog.setShippingState("Delivered");
            entityManager.merge(sellingLog);
            entityManager.getTransaction().commit();
            return "Selling Log Updated Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while updating";
        }

    }

    public List<SellingLog> getShippingRequestsBySellingCompany(String sellingCompanyName) {
        return entityManager.createQuery("SELECT s FROM SellingLog s WHERE s.sellingCompanyName = :sellingCompanyName", SellingLog.class).setParameter("sellingCompanyName", sellingCompanyName).getResultList();
    }
}
