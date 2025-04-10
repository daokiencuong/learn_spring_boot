package vn.dkc.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dkc.jobhunter.domain.Company;
import vn.dkc.jobhunter.domain.dto.ResultPaginationDTO;
import vn.dkc.jobhunter.service.CompanyService;
import vn.dkc.jobhunter.util.annotation.ApiMessage;

import java.util.List;
import java.util.Optional;

/**
 * Controller xử lý các yêu cầu liên quan đến quản lý thông tin công ty Cung cấp các API endpoint
 * CRUD (Create, Read, Update, Delete) cho đối tượng Company
 */
@RestController
@RequestMapping("/api/${dkc.application.version}")
public class CompanyController {
    /**
     * Service xử lý logic nghiệp vụ liên quan đến Company
     */
    final CompanyService companyService;

    /**
     * Constructor để inject CompanyService
     * 
     * @param companyService service xử lý logic nghiệp vụ Company
     */
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * API endpoint tạo mới một công ty
     * 
     * @param company thông tin công ty cần tạo (đã được validate)
     * @return ResponseEntity chứa thông tin công ty đã được tạo
     */
    @PostMapping("/companies")
    @ApiMessage("Create new company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company newCompany = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    /**
     * API endpoint lấy thông tin một công ty theo ID
     * 
     * @param id mã định danh của công ty
     * @return ResponseEntity chứa thông tin công ty
     */
    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.companyService.handeGetCompanyById(id));
    }

    /**
     * API endpoint lấy danh sách tất cả các công ty
     * 
     * @return ResponseEntity chứa danh sách các công ty
     */
    @GetMapping("/companies")
    @ApiMessage("Get all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<Company> spec,
            Pageable pageable
            ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.companyService.handeGetAllCompanies(spec, pageable));
    }


    /**
     * API endpoint cập nhật thông tin một công ty
     * 
     * @param company thông tin công ty cần cập nhật (đã được validate)
     * @return ResponseEntity chứa thông tin công ty sau khi cập nhật
     */
    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.companyService.handleUpdateCompany(company));
    }

    /**
     * API endpoint xóa một công ty theo ID
     * 
     * @param id mã định danh của công ty cần xóa
     * @return ResponseEntity không có nội dung (HTTP 204)
     */
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable("id") long id) {
        this.companyService.handeDeleteCompanyById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
