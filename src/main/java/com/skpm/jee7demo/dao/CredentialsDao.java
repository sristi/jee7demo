package com.skpm.jee7demo.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import com.skpm.jee7demo.domain.Credentials;

/**
 * DAO for Credentials
 */
@Stateless
public class CredentialsDao {
	@PersistenceContext(unitName = "jeedemo-persistence-unit")
	private EntityManager em;

	public void create(Credentials entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Credentials entity = em.find(Credentials.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Credentials findById(Long id) {
		return em.find(Credentials.class, id);
	}

	public Credentials update(Credentials entity) {
		return em.merge(entity);
	}

	public List<Credentials> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Credentials> findAllQuery = em.createQuery(
				"SELECT DISTINCT c FROM Credentials c ORDER BY c.id",
				Credentials.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
