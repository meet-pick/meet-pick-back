package org.jpetto.meetpickback.group.groups.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.group.groups.dto.GroupDto;
import org.jpetto.meetpickback.group.groups.entity.Group;
import org.jpetto.meetpickback.group.groups.repository.GroupRepository;
import org.jpetto.meetpickback.group.members.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberService memberService;

    @Transactional
    public GroupDto.createGroupResponse createGroup(Account loginUser, GroupDto.createGroupRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Group group = Group.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        groupRepository.save(group);

        memberService.addOwner(group, loginUser.getId());

        if (request.getMembers() != null) {
            memberService.addMembers(group, request.getMembers());
        }

        return GroupDto.createGroupResponse.builder().message("모임 생성 완료").build();
    }

    @Transactional
    public GroupDto.updateGroupResponse updateGroup(Account loginUser, long groupId, GroupDto.@Valid updateGroupRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (request.getName() != null) {
            group.setName(request.getName());
        }

        if (request.getDescription() != null) {
            group.setDescription(request.getDescription());
        }

        return GroupDto.updateGroupResponse.builder().message("모임 업데이트 완료").build();
    }

    @Transactional
    public GroupDto.deleteGroupResponse deleteGroup(Account loginUser, long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!memberService.isOwner(group, loginUser)) {
            throw new IllegalArgumentException("member can't delete this group");
        }

        groupRepository.delete(group);

        return GroupDto.deleteGroupResponse.builder().message("그룹 삭제 완료했습니다.").build();
    }

    public List<GroupDto.getGroupResponse> getGroup(Account loginUser) {
        List<Group> groups = groupRepository.findByMembersMemberAccountId(loginUser.getId());
        List<GroupDto.getGroupResponse> groupResponses = new ArrayList<>();
        for (Group group : groups) {
            groupResponses.add(
                    GroupDto.getGroupResponse.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .description(group.getDescription())
                    .build()
            );
        }

        return groupResponses;
    }
}
