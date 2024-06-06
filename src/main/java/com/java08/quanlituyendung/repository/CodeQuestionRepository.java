package com.java08.quanlituyendung.repository;
import com.java08.quanlituyendung.entity.Test.CodeQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeQuestionRepository extends JpaRepository<CodeQuestionEntity, Long> {

}
