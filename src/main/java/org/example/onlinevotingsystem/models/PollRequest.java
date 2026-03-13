package org.example.onlinevotingsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollRequest {
    private String title;
    private String description;
    private List<Option> options;
    private String pollDate;
    private Category category;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isActive;
    private User admin;
    private String type;
    private String votingStrategy;
    private String weight;



}