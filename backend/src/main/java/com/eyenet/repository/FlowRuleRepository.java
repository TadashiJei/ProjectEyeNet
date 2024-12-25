package com.eyenet.repository;

import com.eyenet.model.document.FlowRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowRuleRepository extends MongoRepository<FlowRule, String> {
    List<FlowRule> findBySwitchId(String switchId);
    List<FlowRule> findBySwitchIdAndTableId(String switchId, Integer tableId);
    void deleteBySwitchId(String switchId);
}
