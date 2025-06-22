package com.phuc.jobms.client;


import com.phuc.jobms.external.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "COMPANYMS",
    url = "${company-service.url}"
)
public interface CompanyClient {
    @GetMapping("/api/companies/{id}")
    Company getCompanyById(@PathVariable("id") Long id);
}
