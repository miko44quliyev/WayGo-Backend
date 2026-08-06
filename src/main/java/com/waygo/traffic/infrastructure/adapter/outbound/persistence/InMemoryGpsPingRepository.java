package com.waygo.traffic.infrastructure.adapter.outbound.persistence;

import com.waygo.traffic.domain.entity.GpsPing;


import com.waygo.traffic.application.port.outbound.GpsPingRepository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryGpsPingRepository implements GpsPingRepository {

    private final CopyOnWriteArrayList<GpsPing> storage = new CopyOnWriteArrayList<>();

    @Override
    public void save(GpsPing ping) {
        storage.add(ping);
    }

    @Override
    public List<GpsPing> findAll() {
        return new ArrayList<>(storage);
    }
}
