package org.timeflies.projects;

import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Transactional(REQUIRED)
public class ProjectService {
    @Transactional(SUPPORTS)
    public List<Project> findAll() {
        return Project.listAll();
    }

    @Transactional(SUPPORTS)
    public List<Project> listSortBy(String orderBy) {
        return Project.list(orderBy);
    }

    @Transactional(SUPPORTS)
    public List<Project> userList(Long userId) {
        return Project.find("userId", Sort.descending("lastModified"), userId).list();
    }

    @Transactional(SUPPORTS)
    public Project userLatest(Long userId) {
        return Project.find("userId", Sort.descending("lastModified"), userId).firstResult();
    }

    @Transactional(SUPPORTS)
    public Project findById(Long id) {
        return Project.findById(id);
    }

    @Transactional(SUPPORTS)
    public Project userRandom(Long userId) {
        long count = Project.find("userId", userId).count();
        SecureRandom random = new SecureRandom();
        int randomProj = random.nextInt((int) count);
        return Project.find("userId", userId).page(randomProj, 1).firstResult();
    }

    public Project persist(@Valid Project proj) {
        Project.persist(proj);
        return proj;
    }

    public Project update(@Valid Project proj) {
        Project entity = Project.findById(proj.id);
        entity.name = proj.name;
        entity.userId = proj.userId;
        entity.description = proj.description;
        entity.colour = proj.colour;
        entity.lastModified = proj.lastModified;
        entity.isArchived = proj.isArchived;
        return entity;
    }

    public void delete(Long id) {
        Project u = Project.findById(id);
        u.delete();
    }
}
