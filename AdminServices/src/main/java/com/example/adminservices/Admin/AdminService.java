package com.example.adminservices.Admin;

import com.example.adminservices.Admin.Admin;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Path("/v1/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminService implements Serializable {

    @EJB
    private AdminBean adminBean;
    @POST
    @Path("/register")
    public String register( Admin admin ){
       return adminBean.addAdmin(admin);
    }
    @GET
    @Path("/login")
    public String login(Admin admin){
          return adminBean.validate(admin);
    }
    @GET
    @Path("/getAllAdmins")
    public Response getAllAdmins() {
        return Response.ok(adminBean.getAllAdmins()).build();
    }



    @DELETE
    @Path("/deleteAdmin/{username}")
    public String deleteAdmin(@PathParam("username") String username) {
       return adminBean.deleteAdmin(username);
    }


    @PUT
    @Path("/updateAdmin")
    public String updateAdmin(Admin admin) {
        return adminBean.updateAdmin(admin);
    }
    @POST
    @Path("/addSellingCompany")
    public String addSellingCompany(String sellingCompany) {

        //send HTTPrequest to selling company to add selling company
        String url = "http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/sellingCompany/add";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .POST(HttpRequest.BodyPublishers.ofString(sellingCompany))
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @GET
    @Path("/getAllSellingCompanies")
    public String getAllSellingCompanies() {
        //send HTTPrequest to selling company to get all selling companies
        String url = "http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/sellingCompany/getAllSellingCompanies";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .GET()
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @POST
    @Path("/addShippingCompany")
    public String addShippingCompany(String shippingCompany) {
        //send HTTPrequest to shipping company to add shipping company
        String url = "http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/shippingCompany/add";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .POST(HttpRequest.BodyPublishers.ofString(shippingCompany))
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GET
    @Path("/getAllShippingCompanies")
    public String getAllShippingCompanies() {
        //send HTTPrequest to shipping company to get all shipping companies
        String url = "http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/shippingCompany/getAll";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .GET()
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //GET ALL Customers from Customer Service
    @GET
    @Path("/getAllCustomers")
    public String getAllCustomers() {
        //send HTTPrequest to customer service to get all customers
        String url = "http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/customer/getAll";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .GET()
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}