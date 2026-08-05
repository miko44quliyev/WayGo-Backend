package org.example.waygo.application.port.out;

import org.example.waygo.domain.model.GpsPing;

import java.util.List;

public interface GpsPingRepository {

    void save(GpsPing ping);

    List<GpsPing> findAll();
}
