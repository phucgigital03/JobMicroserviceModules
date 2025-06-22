package com.phuc.jobms.job;


import com.phuc.jobms.dto.JobDTO;

import java.util.List;

public interface JobService {
    List<JobDTO> getJobs();
    void addJob(Job job);
    JobDTO getJobById(Long id);
    boolean deleteJobById(Long id);
    boolean updateJobById(Long id, Job job);
}
