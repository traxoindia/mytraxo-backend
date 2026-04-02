package com.mytraxo.payroll.repository;

import com.mytraxo.payroll.entity.SettlementRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementRepository extends MongoRepository<SettlementRecord, String> {
}