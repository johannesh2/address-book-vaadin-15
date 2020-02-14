package org.vaadin.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.backend.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
