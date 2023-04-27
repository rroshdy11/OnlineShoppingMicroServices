package com.example.onlineshopping.Customer;

import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@MessageDriven(
        activationConfig = {
                @jakarta.ejb.ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
                @jakarta.ejb.ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/orders")
        },
        mappedName= "java:/jms/queue/orders", name = "OrderMessageBean"  )

public class CustomerNotficationBean implements MessageListener {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();
    @Override
    public void onMessage(Message message) {
        try {
            String orderRequest = message.getBody(String.class);
            //split orderRequest to get customerName and message
            String [] orderRequestArray = orderRequest.split(",");
            String customerName = orderRequestArray[0];
            String message1 = orderRequestArray[1];
            System.out.println("Customer Name: "+customerName);
            //create new CustomerNotification
            CustomerNotification customerNotification = new CustomerNotification();
            //get customer from database
            Customer customer = entityManager.createQuery("SELECT c FROM Customer c WHERE c.username = :name", Customer.class).setParameter("name", customerName).getSingleResult();
            customerNotification.setCustomer(customer);
            customerNotification.setMessage(message1);
            //presist orderRequest in database
            entityManager.getTransaction().begin();
            entityManager.persist(customerNotification);
            entityManager.getTransaction().commit();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

    }
}
