package com.phuc.jobms.mapper;

import com.phuc.jobms.dto.JobDTO;
import com.phuc.jobms.external.Company;
import com.phuc.jobms.external.Review;
import com.phuc.jobms.job.Job;

import java.util.List;

public class JobMapper {
    public static JobDTO convertJobDTO(Job job, Company company, List<Review> reviews) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setMinimumSalary(job.getMinimumSalary());
        jobDTO.setMaximumSalary(job.getMaximumSalary());
        jobDTO.setCompany(company);
        jobDTO.setReviews(reviews);
        return jobDTO;
    }
}
