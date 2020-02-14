package org.vaadin.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.backend.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
