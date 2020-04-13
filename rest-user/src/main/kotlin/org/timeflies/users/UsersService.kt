package org.timeflies.users


import io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider
import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.hibernate.orm.panache.PanacheEntityBase.listAll
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations.listAll
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional
import javax.transaction.Transactional.TxType.REQUIRED
import javax.transaction.Transactional.TxType.SUPPORTS

@ApplicationScoped
@Transactional(REQUIRED)
class UsersService {
    @Transactional(SUPPORTS)
    fun findAll(): List<Users> {
        return listAll()
    }

}