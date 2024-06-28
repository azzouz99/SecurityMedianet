package com.example.securitymedianet.Entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue
    private Integer id;
    private Date date_début;
    private Date date_fin;

    private String name;
    private String Chef_de_projet;
    private String type;
    private ProjectStatus status;

    @OneToOne
    private User client;
    @OneToMany
    private List<Article> articles;

}
