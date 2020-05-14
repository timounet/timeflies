package org.timeflies.timers;

import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Transactional(REQUIRED)
public class TimerService {
    @Transactional(SUPPORTS)
    public List<Timer> findAll() {
        return Timer.listAll();
    }

    @Transactional(SUPPORTS)
    public List<Timer> listSortBy(String orderBy) {
        return Timer.list(orderBy);
    }

    @Transactional(SUPPORTS)
    public List<Timer> userList(Long userId) {
        return Timer.find("userId", Sort.descending("start"), userId).list();
    }

    @Transactional(SUPPORTS)
    public Timer userLatest(Long userId) {
        return Timer.find("userId", Sort.descending("start"), userId).firstResult();
    }

    @Transactional(SUPPORTS)
    public List<Timer> projectList(Long projectId) {
        return Timer.find("projectId", Sort.descending("start"), projectId).list();
    }

    @Transactional(SUPPORTS)
    public Timer projectLatest(Long projectId) {
        return Timer.find("projectId", Sort.descending("start"), projectId).firstResult();
    }

    @Transactional(SUPPORTS)
    public Timer findById(Long id) {
        return Timer.findById(id);
    }

    public Timer persist(@Valid Timer timer) {
        Timer.persist(timer);
        return timer;
    }

    public Timer update(@Valid Timer timer) {
        Timer entity = Timer.findById(timer.id);
        entity.projectId = timer.projectId;
        entity.userId = timer.userId;
        entity.start = timer.start;
        entity.finish = timer.finish;
        return entity;
    }

    public void delete(Long id) {
        Timer u = Timer.findById(id);
        u.delete();
    }
}
