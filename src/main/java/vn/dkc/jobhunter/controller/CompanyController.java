package vn.dkc.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dkc.jobhunter.domain.Company;
import vn.dkc.jobhunter.service.CompanyService;

import java.util.List;

@RestController
public class CompanyController {
    final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company newCompany = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handeGetCompanyById(id));
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handeGetAllCompanies());
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handleUpdateCompany(company));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable("id") long id) {
        this.companyService.handeDeleteCompanyById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
