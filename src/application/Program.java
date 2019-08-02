package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
	
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println("===Teste 1: seller findById");
		Seller seller = sellerDao.findByid(3);
		System.out.println(seller);
		
		System.out.println("\n===Teste 2: seller findByDepartment");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== TEST 3: seller findAll =====");
		list = sellerDao.findAll();
		for (Seller obj : list) {
			System.out.println(obj);
		}
		
		//Esse bloco n�o da erro porque, no banco de dados  na coluna Id n�o aceita nulo
		System.out.println("\n=== TEST 4: seller insert =====");
		Seller seller2 = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(seller2);
		System.out.println("Inserted! new id = " + seller2.getId());
		
		System.out.println("\n=== TEST 5: seller update =====");
		seller = sellerDao.findByid(1);
		seller.setName("Marta Wayne");
		sellerDao.update(seller);
		System.out.println("Update completo");
	}

}
