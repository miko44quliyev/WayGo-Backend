package com.waygo.application.port.out;

import com.waygo.domain.model.*;



import java.util.List;

public interface GpsPingRepository {

    void save(GpsPing ping);

    List<GpsPing> findAll();
}
