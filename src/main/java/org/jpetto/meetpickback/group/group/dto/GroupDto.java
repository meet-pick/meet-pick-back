package org.jpetto.meetpickback.group.group.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GroupDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class createGroupRequest {
        @NotBlank
        private String name;
        private String description;
        private List<Long> members;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class updateGroupRequest {
        private String name;
        private String description;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class createGroupResponse {
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class updateGroupResponse {
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class deleteGroupResponse {
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getGroupResponse {
        private long id;
        private String name;
        private String description;
        private String message;
    }
}
