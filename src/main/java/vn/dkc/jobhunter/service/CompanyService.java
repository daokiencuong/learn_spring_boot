package vn.dkc.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Company;
import vn.dkc.jobhunter.repository.CompanyRepository;

import java.util.List;

@Service
public class CompanyService {
    final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public Company handeGetCompanyById(long id) {
        return this.companyRepository.findById(id).orElse(null);
    }

    public List<Company> handeGetAllCompanies() {
        return this.companyRepository.findAll();
    }

    public Company handleUpdateCompany(Company company) {
        if(handeGetCompanyById(company.getId()) != null) {
            return this.companyRepository.save(company);
        } else {
            return null;
        }
    }

    public void handeDeleteCompanyById(long id) {
        this.companyRepository.deleteById(id);
    }
}
