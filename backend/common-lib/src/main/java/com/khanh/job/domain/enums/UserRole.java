package com.khanh.job.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole implements BaseEnum {
  ROLE_ADMIN("Admin", "Administrator"),
  ROLE_JOB_SEEKER("JobSeeker", "Job Seeker"),
  ROLE_EMPLOYER("Employer", "Employer");

  private final String code;
  private final String description;
}
