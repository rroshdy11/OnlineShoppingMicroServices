package com.example.onlineshopping.Product;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Stateless
@Path("/v1/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductService {
    @EJB
    private ProductBean productBean;

    @GET
    @Path("/getAllProducts")
    public List<Product> getAllProducts() {
        return productBean.getAll();
    }
    @GET
    @Path("/getProductById/{id}")
    public Product getProductById(@PathParam("id") int id) {
        return productBean.getProductById(id);
    }
}
