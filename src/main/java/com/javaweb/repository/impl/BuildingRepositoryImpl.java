package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.NumberUp;

import java.sql.Statement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository{
	static final String DB_URL = "jdbc:mysql://localhost:3306/java_web_project01";
	static final String USER = "root";
	static final String PASS = "123456";
	
	public static void joinTable(Map<String, Object> params, List<String> typeCode, StringBuilder sql) {
		String staffId = (String) params.get("staffId");
		if (StringUtil.checkString(staffId)) {
			sql.append("INNER JOIN assignmentbuilding ab ON b.id = ab.buildingid ");
		}
		if (typeCode != null && typeCode.size() != 0) {
			sql.append("INNER JOIN buildingtype ON b.id = buildingtype.buildingid ");
			sql.append("INNER JOIN renttype ON renttype.id = buildingtype.renttypeid ");
		}
		String rentAreaTo = (String) params.get("areaTo");
		String rentAreaFrom = (String) params.get("areaFrom");
		if (StringUtil.checkString(rentAreaTo) == true || StringUtil.checkString(rentAreaFrom) == true) {
			sql.append("INNER JOIN rentarea ON rentarea.buildingid = b.id ");
		}
	}
	
	public static void queryNomal(Map<String, Object> params, StringBuilder where) {
		for (Map.Entry<String, Object> it : params.entrySet()) {
			if (!it.getKey().equals("staffId") 
			    && !it.getKey().equals("typeCode") 
			    && !it.getKey().startsWith("area") 
			    && !it.getKey().startsWith("rentPrice")) {
				
				String value = it.getValue().toString();
				if (StringUtil.checkString(value)) {
					if (NumberUtil.isNumber(value) == true) {
						where.append(" AND b." + it.getKey() + " = " + value);
					} else {
						where.append(" AND b." + it.getKey() + " LIKE '%" + value + "%' ");
					}
				}
			}
		}
	}
	
	public static void querySpecial(Map<String, Object> params, List<String> typeCode, StringBuilder where) {
		String staffId = (String) params.get("staffId");
		if (StringUtil.checkString(staffId)) {
			where.append(" AND ab.staffid = " + staffId);
		}
		
		String rentAreaTo = (String) params.get("areaTo");
		String rentAreaFrom = (String) params.get("areaFrom");
		
		if (StringUtil.checkString(rentAreaTo) == true || StringUtil.checkString(rentAreaFrom) == true) {
			if (StringUtil.checkString(rentAreaTo)) {
				where.append(" AND rentarea.value <= " + rentAreaTo);
			}
			if (StringUtil.checkString(rentAreaFrom)) {
				where.append(" AND rentarea.value >= " + rentAreaFrom);
			}
		}
		
		String rentPriceTo = (String) params.get("rentPriceTo");
		String rentPriceFrom = (String) params.get("rentPriceFrom");
		
		if (StringUtil.checkString(rentPriceTo) == true || StringUtil.checkString(rentPriceFrom) == true) {
			if (StringUtil.checkString(rentAreaFrom)) {
				where.append(" AND b.rentprice >= " + rentPriceFrom);
			}

			if (StringUtil.checkString(rentAreaTo)) {
				where.append(" AND b.rentprice <= " + rentPriceTo);
			}
		}
		
		if (typeCode != null && typeCode.size() != 0) {
			List<String> code = new ArrayList<>();
			for (String item : typeCode) {
				code.add("'" + item + "'");
			}
			where.append(" AND renttype.code IN(" + String.join(",", code) + ") ");
		}
	}
	
	@Override
	public List<BuildingEntity> findAll(Map<String, Object> params, List<String> typeCode) {
		StringBuilder sql = new StringBuilder(
					"SELECT b.id, b.name, b.ward, b.districtid, " + 
					"b.street, b.floorarea, b.rentprice, b.servicefee, b.brokeragefee" +
					"\n" + 
					"FROM building b" + "\n"
				);
		joinTable(params, typeCode, sql);
		StringBuilder where = new StringBuilder("\n" + "WHERE 1=1");
		queryNomal(params, where);
		querySpecial(params, typeCode, where);
		where.append("\n" + "GROUP BY b.id;");
		sql.append(where);
		List<BuildingEntity> result = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString());){
			
			while(rs.next()) {
				BuildingEntity buildingEntity = new BuildingEntity();
				buildingEntity.setId(rs.getInt("b.id"));
				buildingEntity.setName(rs.getString("b.name"));
				buildingEntity.setWard(rs.getString("b.ward"));
				buildingEntity.setDistrictid(rs.getLong("b.districtid"));
				buildingEntity.setStreet(rs.getString("b.street"));
				buildingEntity.setFloorArea(rs.getLong("b.floorarea"));
				buildingEntity.setRentPrice(rs.getLong("b.rentprice"));
				buildingEntity.setServiceFee(rs.getString("b.servicefee"));
				buildingEntity.setBrokerageFee(rs.getLong("b.brokeragefee"));
				result.add(buildingEntity);
			}
			System.out.println("Connected database successfully...");
		} catch (Exception e) {
			System.out.println("Connected database failed...");
			e.printStackTrace(); 
		}
		return result;
	}

}
