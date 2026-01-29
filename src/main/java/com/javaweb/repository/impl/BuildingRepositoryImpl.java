package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository{
	static final String DB_URL = "jdbc:mysql://localhost:3306/java_web_project01";
	static final String USER = "root";
	static final String PASS = "123456";
	
	@Override
	public List<BuildingEntity> findAll(String name, Integer numberOfBasement) {
		StringBuilder sql = new StringBuilder("SELECT * FROM building b WHERE 1 = 1" + " ");
		if (name != null && !name.equals("")) {
			sql.append("AND b.name like '%" + name + "%'" + " ");
		}
		
		if (numberOfBasement != null) {
			sql.append("AND b.numberofbasement = " + numberOfBasement + " ");
		}
		
		List<BuildingEntity> result = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString());){
			
			while(rs.next()) {
				BuildingEntity building = new BuildingEntity();
				building.setName(rs.getString("name"));
				building.setNumberOfBasement(rs.getInt("numberOfBasement"));
				result.add(building);
			}
			System.out.println("Connected database successfully...");
		} catch (Exception e) {
			System.out.println("Connected database failed...");
		}
		return result;
	}

}
