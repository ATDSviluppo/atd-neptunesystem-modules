package com.HMIModule.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TranslatedText")
@Data
public class TranslatedText {

    @Id
    @Column(name = "TextId")
    private String TextId;

    @Column(name = "LanguageId")
    private String LanguageId;

    @Column(name = "Text")
    private String Text;
}
