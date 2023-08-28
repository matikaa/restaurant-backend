package com.restaurant.app.contact.repository;

import com.restaurant.app.contact.repository.dto.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactJpaRepository extends JpaRepository<ContactEntity, Long> {
}
