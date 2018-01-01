package com.skpm.jee7demo.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import com.skpm.jee7demo.domain.Language;

/**
 * DAO for Language
 */
@Stateless
public class LanguageDao {
	@PersistenceContext(unitName = "jeedemo-persistence-unit")
	private EntityManager em;

	public void create(Language entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Language entity = em.find(Language.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Language findById(Long id) {
		return em.find(Language.class, id);
	}

	public Language update(Language entity) {
		return em.merge(entity);
	}

	public List<Language> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Language> findAllQuery = em.createQuery(
				"SELECT DISTINCT l FROM Language l ORDER BY l.id",
				Language.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
