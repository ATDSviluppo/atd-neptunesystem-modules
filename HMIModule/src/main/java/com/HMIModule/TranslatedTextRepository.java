package com.HMIModule;

import com.HMIModule.Entity.TranslatedText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslatedTextRepository extends JpaRepository<TranslatedText, String> {
}
