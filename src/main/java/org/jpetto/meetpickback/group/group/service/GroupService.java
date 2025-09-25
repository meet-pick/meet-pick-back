package org.jpetto.meetpickback.group.group.service;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.group.group.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
}
