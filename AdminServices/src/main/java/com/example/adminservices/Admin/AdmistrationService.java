package com.example.adminservices.Admin;

import jakarta.ejb.Stateless;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Path("/v1/administration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class AdmistrationService {
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

}
