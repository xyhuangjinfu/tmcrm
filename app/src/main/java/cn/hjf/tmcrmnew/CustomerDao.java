package cn.hjf.tmcrmnew;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Collections;
import java.util.List;

@Dao
public class CustomerDao {
	@Insert
	void insert(Customer... customers) {

	}

//	@Query("SELECT * FROM customer WHERE name LIKE :first AND " +
//			"last_name LIKE :last LIMIT 1")
//	Customer findByName(String name) {
//
//	}

	@Query("SELECT * FROM customer")
	List<Customer> getAll() {
		return Collections.emptyList();
	}
}
