package org.timeflies.projects;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Transactional(REQUIRED)
public class UsersService {


    @Transactional(SUPPORTS)
    public List<Users> findAll() {
        return Users.listAll();
    }

    @Transactional(SUPPORTS)
    public List<Users> listSortBy(String orderBy) {
        return Users.list(orderBy);
    }

    @Transactional(SUPPORTS)
    public Users findById(Long id) {
        return Users.findById(id);
    }

    @Transactional(SUPPORTS)
    public Users findRandom() {
        Users randomUser = null;
        while (randomUser == null) {
            randomUser = Users.findRandom();
        }
        return randomUser;
    }

    public Users persist(@Valid Users user) {
        Users.persist(user);
        return user;
    }

    public Users update(@Valid Users user) {
        Users entity = Users.findById(user.id);
        entity.userName = user.userName;
        entity.lastName = user.lastName;
        entity.firstName = user.firstName;
        entity.pictureUrl = user.pictureUrl;
        entity.status = user.status;
        return entity;
    }

    public void delete(Long id) {
        Users u = Users.findById(id);
        u.delete();
    }
}
