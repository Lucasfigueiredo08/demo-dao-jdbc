package org.example.application;

import org.example.model.dao.DaoFactory;
import org.example.model.dao.SellerDao;
import org.example.model.entities.Department;
import org.example.model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {

    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.findById(3);

        System.out.println(seller);

        System.out.println("\n=== TEST 2: seller findByDepartment ===");
        Department department = new Department(2, null);
        List<Seller> sellerList = sellerDao.findByDepartment(department);
        for (Seller s : sellerList){
            System.out.println(s);
        }

        System.out.println("\n=== TEST 3: seller findAll ===");
        List<Seller> findAllSeller = sellerDao.findAll();
        for (Seller s : findAllSeller){
            System.out.println(s);
        }

        System.out.println("\n=== TEST 4: seller Insert ===");
        Seller newSeller = new Seller(null, "Lucas", "lucas@teste.com", new Date(), 4000.0, department);
        sellerDao.insert(newSeller);
        System.out.println("Novo usuário criado: " + newSeller.getId());

        System.out.println("\n=== TEST 5: seller Update ===");
        seller = sellerDao.findById(1);
        seller.setName("Amanda");
//        seller.setBaseSalary(5000.0);
//        seller.setEmail("Amanda@gmail.com");
//        seller.setDepartment(department);
//        seller.setBirthDate(new Date());
        sellerDao.update(seller);
        System.out.println("Updated completed!!");



    }
}
