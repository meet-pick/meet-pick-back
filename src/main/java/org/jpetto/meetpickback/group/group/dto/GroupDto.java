package org.jpetto.meetpickback.group.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "모임 이름", example = "모임1")
        @NotBlank
        private String name;

        @Schema(description = "모임 설명", example = "모임입니다.")
        private String description;

        @Schema(description = "추가 멤버(방장은 자동 추가로 제외)", example = "[2, 3]")
        private List<Long> members;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class updateGroupRequest {
        @Schema(description = "모임 이름", example = "모임1")
        private String name;

        @Schema(description = "모임 설명", example = "모임입니다.")
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
