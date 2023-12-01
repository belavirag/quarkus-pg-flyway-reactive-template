package dev.belavirag.template.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Test")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Setter
    @Column(name = "test", columnDefinition = "TEXT", nullable = false)
    private String test;
}
