package com.phuc.jobms.job.impl;


import com.phuc.jobms.client.CompanyClient;
import com.phuc.jobms.client.ReviewClient;
import com.phuc.jobms.dto.JobDTO;
import com.phuc.jobms.external.Company;
import com.phuc.jobms.external.Review;
import com.phuc.jobms.job.Job;
import com.phuc.jobms.job.JobRepository;
import com.phuc.jobms.job.JobService;
import com.phuc.jobms.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;
    private int retryCount = 0;

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
//    @CircuitBreaker(
//            name = "companyBreaker",
//            fallbackMethod = "getJobsFallback"
//    )
//    @Retry(
//            name = "companyBreaker",
//            fallbackMethod = "getJobsFallback"
//    )
    @RateLimiter(
            name = "companyLimiter"
//            fallbackMethod = "getJobsFallback"
    )
    public List<JobDTO> getJobs() {
        System.out.println("retryCount: " + ++retryCount);
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map(job -> {
            Company company = companyClient.getCompanyById(job.getCompanyId());
            List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
            return JobMapper.convertJobDTO(job, company, reviews);

        }).toList();
    }

    public List<String> getJobsFallback(Exception e) {
        return List.of("Fallback: Unable to fetch jobs at the moment");
    }

    @Override
    public void addJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        if(job == null) {
            return null;
        }

        // Fetch company and reviews using Feign clients
        Company company = null;
        List<Review> reviews = null;
        try {
            company = companyClient.getCompanyById(job.getCompanyId());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            reviews = reviewClient.getReviews(job.getCompanyId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return JobMapper.convertJobDTO(job, company, reviews);
    }

    @Override
    public boolean deleteJobById(Long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateJobById(Long id, Job job) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job existingJob = jobOptional.get();
            existingJob.setTitle(job.getTitle());
            existingJob.setDescription(job.getDescription());
            existingJob.setLocation(job.getLocation());
            existingJob.setMinimumSalary(job.getMinimumSalary());
            existingJob.setMaximumSalary(job.getMaximumSalary());
            jobRepository.save(existingJob);
            return true;
        } else {
            return false;
        }
    }

}
