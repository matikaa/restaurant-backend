package com.restaurant.contact.repository;

import com.restaurant.contact.repository.dto.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactJpaRepository extends JpaRepository<ContactEntity, Long> {
}
