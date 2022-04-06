package com.etiqa.websocket.chat.repository;

import com.etiqa.websocket.chat.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findAllById(Long id);
}
