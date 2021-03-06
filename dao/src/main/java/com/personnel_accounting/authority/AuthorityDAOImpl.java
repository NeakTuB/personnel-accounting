package com.personnel_accounting.authority;

import com.personnel_accounting.domain.Authority;
import com.personnel_accounting.enums.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class AuthorityDAOImpl implements AuthorityDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Authority find(String username) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Authority.class, username);
    }

    @Override
    public List<Authority> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Authority").list();
    }

    @Override
    public List<Authority> findByRole(Role role) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Authority where role = :role");
        query.setParameter("role", role);
        return query.list();
    }

    @Override
    public Authority save(Authority authority) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(authority);
        return authority;
    }

    @Override
    public Authority merge(Authority authority) {
        Session session = sessionFactory.getCurrentSession();
        return (Authority) session.merge(authority);
    }

    @Override
    public boolean removeByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Authority authority = session.get(Authority.class, username);
        if (authority == null) return false;
        else {
            try {
                session.delete(authority);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Authority authority) {
        Session session = sessionFactory.getCurrentSession();
        authority = (Authority) session.merge(authority);
        if (authority == null) return false;
        else {
            try {
                session.delete(authority);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
