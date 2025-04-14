package vn.dkc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.Company;
import vn.dkc.jobhunter.domain.response.ResultPaginationDTO;
import vn.dkc.jobhunter.repository.CompanyRepository;

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

    public ResultPaginationDTO handeGetAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompanies = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mta = new ResultPaginationDTO.Meta();

        mta.setPage(pageable.getPageNumber() + 1);
        mta.setPageSize(pageable.getPageSize());

        mta.setPages(pageCompanies.getTotalPages());
        mta.setTotal(pageCompanies.getTotalElements());

        result.setMeta(mta);
        result.setResult(pageCompanies.getContent());

        return result;
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
