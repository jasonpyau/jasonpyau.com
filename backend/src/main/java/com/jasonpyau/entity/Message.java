package com.jasonpyau.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages")
public class Message {

    public static final String MESSAGE_ID_ERROR = "Invalid 'id', message not found.";
    public static final String MESSAGE_NAME_ERROR = "'name' should be between 3-50 characters.";
    public static final String MESSAGE_CONTACT_INFO_ERROR = "'contactInfo' should be between 6-100 characters.";
    public static final String MESSAGE_BODY_ERROR = "'body' should be between 15-1000 characters.";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(min = 3, max = 50, message = MESSAGE_NAME_ERROR)
    @NotBlank(message = MESSAGE_NAME_ERROR)
    private String name;

    @Column(name = "contact_info", nullable = false)
    @Size(min = 6, max = 100, message = MESSAGE_CONTACT_INFO_ERROR)
    @NotBlank(message = MESSAGE_CONTACT_INFO_ERROR)
    private String contactInfo;

    @Column(name = "body", nullable = false, columnDefinition = "varchar(1000)")
    @Size(min = 15, max = 1000, message = MESSAGE_BODY_ERROR)
    @NotBlank(message = MESSAGE_BODY_ERROR)
    private String body;

    @Column(name = "date", nullable = false)
    private String date;

}
