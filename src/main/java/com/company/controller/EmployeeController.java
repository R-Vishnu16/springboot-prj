package com.company.controller;

import com.company.model.Employee;
import com.company.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vacancies")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Map<String, List<Employee>> getUsers() {
        Map<String, List<Employee>> response = new HashMap<>();
        response.put("users", repository.findAll());
        return response;
    }
}
