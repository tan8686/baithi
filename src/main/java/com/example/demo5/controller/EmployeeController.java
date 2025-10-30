package com.example.demo5.controller;

import com.example.demo5.model.Employee;
import com.example.demo5.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository repo;

    // CREATE
    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee employee) {
        Employee saved = repo.save(employee);
        return ResponseEntity.ok(saved);
    }

    // READ - Get all
    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    // READ - Search by name
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(repo.findByNameContainingIgnoreCase(keyword));
    }

    // READ - Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id,
                                           @Valid @RequestBody Employee updated) {
        return repo.findById(id)
                .map(emp -> {
                    emp.setName(updated.getName());
                    emp.setPosition(updated.getPosition());
                    emp.setSalary(updated.getSalary());
                    return ResponseEntity.ok(repo.save(emp));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
