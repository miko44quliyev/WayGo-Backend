package com.waygo.traffic.application.port.outbound;

import com.waygo.traffic.domain.entity.GpsPing;




import java.util.List;

public interface GpsPingRepository {

    void save(GpsPing ping);

    List<GpsPing> findAll();
}
