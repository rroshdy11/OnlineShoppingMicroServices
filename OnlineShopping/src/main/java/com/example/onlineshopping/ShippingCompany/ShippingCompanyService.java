package com.example.onlineshopping.ShippingCompany;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Path("/v1/shippingCompany")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class ShippingCompanyService {
    @EJB
    private ShippingCompanyBean shippingCompanyBean;
    @GET
    @Path("/getAll")
    public List<ShippingCompany> getAllShippingCompanies() {
        return shippingCompanyBean.getAllShipping();
    }

    @POST
    @Path("/add")
    public String addShippingCompany( ShippingCompany shippingCompany) {
        return shippingCompanyBean.create(shippingCompany);

    }

    @POST
    @Path("/login")
    public Response login(ShippingCompany ShippingCompany){
        return shippingCompanyBean.validate(ShippingCompany);
    }

    @DELETE
    @Path("/deleteShippingCompany/{name}")
    public String deleteShippingCompany(@PathParam("name") String name) {
        return shippingCompanyBean.deleteShipping(name);
    }

    @GET
    @Path("/getShippingCompanyForGeoLocation/{location}")
    public List<ShippingCompany> getShippingCompanyForGeoLocation(@PathParam("location") String location) {
        return shippingCompanyBean.getShippingCompanyForGeoLocation(location);
    }
    @GET
    @Path("/getAllShippingRequestForShippingCompany/{name}")
    public String getAllShippingRequestForShippingCompany(@PathParam("name") String name) {
           return shippingCompanyBean.getAllShippingRequestForShippingCompany(name);
    }
    @POST
    @Path("/acceptShippingRequest/{id}/{companyName}")
    public String acceptShippingRequest(@PathParam("companyName") String username,@PathParam("id") int id) {
        return shippingCompanyBean.acceptShippingRequest(username,id);
    }
    @GET
    @Path("/getMyShippings/{name}")
    public String getMyShippings(@PathParam("name") String name) {
        return shippingCompanyBean.getMyShippings(name);
    }
    @POST
    @Path("/markAsDelivered/{id}")
    public String markAsDelivered(@PathParam("id") int id) {
        return shippingCompanyBean.markAsDelivered(String.valueOf(id));
    }
}
