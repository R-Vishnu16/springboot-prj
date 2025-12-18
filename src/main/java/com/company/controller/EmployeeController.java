package com.company.controller;

import com.company.model.Employee;
import com.company.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/candidates")
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

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody Employee employee) {
        try {
            System.out.println("Attempting to add employee: " + employee.getFirstName());
            Employee savedEmployee = repository.save(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
        } catch (Exception e) {
            System.err.println("Error adding employee: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error adding employee");
            error.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public Employee updateCandidate(
            @PathVariable int id,
            @RequestBody Employee updatedEmployee) {

        Employee existingEmployee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        existingEmployee.setFirstName(updatedEmployee.getFirstName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setPhone(updatedEmployee.getPhone());
        existingEmployee.setImage(updatedEmployee.getImage());
        existingEmployee.setCompany(updatedEmployee.getCompany());
        existingEmployee.setRole(updatedEmployee.getRole());

        return repository.save(existingEmployee);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deleteCandidate(@PathVariable Integer id) {
        System.out.println("Attempting to delete candidate with ID: " + id);

        Employee employee = repository.findById(id).orElse(null);
        if (employee == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Candidate not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        repository.delete(employee);
        repository.flush();

        System.out.println("Candidate with ID: " + id + " deleted successfully");

        Map<String, String> response = new HashMap<>();
        response.put("message", "Candidate deleted successfully");
        return ResponseEntity.ok(response);
    }

}
