package org.example.application;

import org.example.model.dao.DaoFactory;
import org.example.model.dao.DepartmentDao;
import org.example.model.entities.Department;

import java.util.List;
import java.util.Scanner;

public class Program2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("\n=== TEST 1: seller Insert ===");
        Department newDepartment = new Department(null, "Sofás");
        departmentDao.insert(newDepartment);
        System.out.println("Novo departamento criado: " + newDepartment.getId());

        System.out.println("\n=== TEST 2: seller Update ===");
        newDepartment.setName("Armários");
        departmentDao.update(newDepartment);
        System.out.println("Departamento atualizado: " + newDepartment.getName());

        System.out.println("\n=== TEST 3: seller findById ===");
        Department department = departmentDao.findById(3);
        System.out.println(department);

        System.out.println("\n=== TEST 4: seller findAll ===");

        List<Department> departments = departmentDao.findAll();

        for (Department dp : departments){
            System.out.println(dp);
        }

        System.out.println("\n=== TEST 5: seller DeleteById ===");
        System.out.println("Digite o ID que deseja excluir: ");
        int id = sc.nextInt();
        departmentDao.deleteById(id);
        System.out.println("Departamento de Id: " + id + " deletado");






        sc.close();
    }
}
