package com.javaweb.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.customexception.FieldRequiredException;
import com.javaweb.model.BuildingDTO;
import com.javaweb.model.ErrorResponseDTO;
import com.javaweb.service.BuildingService;

@RestController
public class BuildingAPI {
	
	@Autowired
	private BuildingService buildingService;
	
	@GetMapping(value = "/api/building")
	public List<BuildingDTO> getBuilding(@RequestParam(name="name", required=false) String name,
										@RequestParam(name="numberOfBasement", required=false) Integer numberOfBasement) {
		List<BuildingDTO> result = buildingService.findAll(name, numberOfBasement);
		return result;
	}
	
	@PostMapping("/api/building")
	public Object getBuilding(@RequestBody BuildingDTO building) {
		valiDate(building);
		return null;
	}
	
	public void valiDate(BuildingDTO buildingDTO) {
		if (buildingDTO.getName() == null || buildingDTO.getName().equals("")) {
			throw new FieldRequiredException("Name is null!");
		}
	}
}
