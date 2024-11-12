package com.aidynamo.Dan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanRepository extends JpaRepository<Dan,Long> {
    Dan findByEmail(String email);
}
