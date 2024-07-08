package com.example.securitymedianet.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @Column(length = 510)
    private String description;
    private NotificationDegree productivity;
    private NotificationDegree profitability;
    @OneToOne(mappedBy = "notification")
    @JsonIgnore
    private Article article;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.length() > 255) {
            this.description = description.substring(0, 255);
        } else {
            this.description = description;
        }
    }
}
