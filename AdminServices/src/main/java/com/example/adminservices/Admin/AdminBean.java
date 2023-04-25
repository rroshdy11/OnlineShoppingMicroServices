package com.example.adminservices.Admin;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

@Stateless
public class AdminBean {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();

    public String addAdmin(Admin admin){
        try {
            entityManager.getTransaction().begin();
            //if user exists return error
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 != null) {
                return "User already exists";
            }
            entityManager.persist(admin);
            entityManager.getTransaction().commit();
            return "Registered Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while registering";
        }
    }
    public String validate(Admin admin){
        try {
            entityManager.getTransaction().begin();
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 == null) {
                entityManager.getTransaction().rollback();
                return "User does not exist";
            }
            if (admin1.getPassword().equals(admin.getPassword())) {
                entityManager.getTransaction().commit();
                return "Login Successful";
            } else {
                entityManager.getTransaction().rollback();
                return "Wrong Password";
            }
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while logging in";
        }
    }

    public String deleteAdmin(String username){
        try {
            entityManager.getTransaction().begin();
            Admin admin = entityManager.find(Admin.class, username);
            if (admin == null) {
                entityManager.getTransaction().rollback();
                return "User does not exist";
            }
            entityManager.remove(admin);
            entityManager.getTransaction().commit();
            return "Admin Deleted Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while deleting admin";
        }
    }
    public String updateAdmin(Admin admin){
        try {
            entityManager.getTransaction().begin();
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 == null) {
                entityManager.getTransaction().rollback();
                return "User does not exist";
            }
            admin1.setPassword(admin.getPassword());
            entityManager.getTransaction().commit();
            return "Admin Updated Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while updating admin";
        }
    }
    public List<Admin> getAllAdmins(){
        try {
            entityManager.getTransaction().begin();
            List<Admin> admins = entityManager.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
            entityManager.getTransaction().commit();
            return admins;
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }
}
