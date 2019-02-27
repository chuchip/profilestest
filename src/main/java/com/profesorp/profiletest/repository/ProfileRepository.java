package com.profesorp.profiletest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.profesorp.profiletest.entities.ProfileEntity;
@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer>{

}
