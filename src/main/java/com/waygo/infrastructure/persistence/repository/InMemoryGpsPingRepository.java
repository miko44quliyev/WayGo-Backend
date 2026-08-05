package com.waygo.infrastructure.persistence.repository;

import com.waygo.domain.model.*;

import com.waygo.application.port.out.GpsPingRepository;

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
