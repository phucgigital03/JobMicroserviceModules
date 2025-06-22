package com.phuc.companyms.company;


import com.phuc.companyms.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();
    boolean updateCompany(Long id, Company company);
    void addCompany(Company company);
    Company getCompanyById(Long id);
    boolean deleteCompanyById(Long id);
    void updateCompanyRating(ReviewMessage reviewMessage);
}
