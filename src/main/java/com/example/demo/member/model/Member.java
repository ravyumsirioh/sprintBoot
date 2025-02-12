package com.example.demo.member.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "members")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private LocalTime timeOfBirth;
    private Integer gender;
    private String location;
    private String characterLevel;
    private Integer characterId;
    private Integer updateCount;
    private Integer termConditionYn;
    private Integer character1Collected;
    private Integer character2Collected;
    private Integer character3Collected;
    private Integer character4Collected;
    private Integer character5Collected;
    private Integer attendanceCount;
    private String attendedAt;
    private Integer totalAttendanceCount;

    @ElementCollection
    private int[] personalityMatrixTile;
}
