package com.example.onlineshopping.SellingLogs;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
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
public class SellingLogsService{


    @EJB
    private SellingLogBean sellingLogBean;
    @POST
    @Path("/add")
    public String addToLog( SellingLog order) {
        return sellingLogBean.addLog(order);
    }

    @GET
    @Path("/getAllSellingLogs")
    public List<SellingLog> getAllSellingLogs() {
        return sellingLogBean.getAllLogs();
    }

    //get all Selling Logs with State =Shipping Request and Shipping Company = null and Shipping Address={Geos}
    @GET
    @Path("/getShippingRequests/{Geo}")
    public List<SellingLog> getShippingRequests(@PathParam("Geo") String geo) {
        return sellingLogBean.getShippingRequestsInGeo(geo);
    }



    @PUT
    @Path("/proccessShippingRequest/{id}/{shippingCompanyName}")
    public String proccessShippingRequest(@PathParam("id") int id,@PathParam("shippingCompanyName") String shippingCompanyName) {
        return sellingLogBean.acceptShippingRequest(id,shippingCompanyName);
    }

    @POST
    @Path("/ShippingRequestdelivered/{id}")
    public String ShippingRequestdelivered(@PathParam("id") int id) {
        return sellingLogBean.assignDelivered(id);
    }
    @GET
    @Path("/getShippingRequestsByShippingCompany/{shippingCompanyName}")
    public List<SellingLog> getShippingRequestsByShippingCompany(@PathParam("shippingCompanyName") String shippingCompanyName) {
        return sellingLogBean.getShippingRequestsByShippingCompany(shippingCompanyName);
    }
    @PUT
    @Path("/markAsDelivered/{id}")
    public String markAsDelivered(@PathParam("id") String id) {
        return sellingLogBean.markAsDelivered(id);
    }

    @GET
    @Path("/getShippingRequestsBySeller/{sellerName}")
    public List<SellingLog> getShippingRequestsBySeller(@PathParam("sellerName") String sellerName) {
        return sellingLogBean.getShippingRequestsBySellingCompany(sellerName);
    }
}