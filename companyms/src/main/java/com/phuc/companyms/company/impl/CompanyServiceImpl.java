package com.phuc.companyms.company.impl;

import com.phuc.companyms.client.ReviewClient;
import com.phuc.companyms.company.Company;
import com.phuc.companyms.company.CompanyRepository;
import com.phuc.companyms.company.CompanyService;
import com.phuc.companyms.dto.ReviewMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ReviewClient reviewClient;

    public CompanyServiceImpl(CompanyRepository companyRepository, ReviewClient reviewClient) {
        this.companyRepository = companyRepository;
        this.reviewClient = reviewClient;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public boolean updateCompany(Long id, Company company) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company existingCompany = companyOptional.get();
            existingCompany.setName(company.getName());
            existingCompany.setDescription(company.getDescription());
            companyRepository.save(existingCompany);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteCompanyById(Long id) {
        if(companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {
        Double averageRating = reviewClient.getAverageRating(reviewMessage.getCompanyId());
        Optional<Company> companyOptional = companyRepository.findById(reviewMessage.getCompanyId());
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            company.setRating(averageRating);
            companyRepository.save(company);
        } else {
            System.out.println("Company with ID " + reviewMessage.getCompanyId() + " not found.");
        }
    }

}
