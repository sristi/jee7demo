package com.skpm.jee7demo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.skpm.jee7demo.domain.Language;

/**
 * Backing bean for Language entities.
 * <p/>
 * This class provides CRUD functionality for all Language entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class LanguageBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Language entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Language language;

	public Language getLanguage() {
		return this.language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "jeedemo-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.language = this.example;
		} else {
			this.language = findById(getId());
		}
	}

	public Language findById(Long id) {

		return this.entityManager.find(Language.class, id);
	}

	/*
	 * Support updating and deleting Language entities
	 */

	public String update() {
		System.out.println("Im called.");
		this.conversation.end();

		/*try {
			if (this.id == null) {
				this.entityManager.persist(this.language);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.language);
				return "view?faces-redirect=true&id=" + this.language.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}*/
		return "";
	}

	public String delete() {
		this.conversation.end();

		try {
			Language deletableEntity = findById(getId());

			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching Language entities with pagination
	 */

	private int page;
	private long count;
	private List<Language> pageItems;

	private Language example = new Language();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Language getExample() {
		return this.example;
	}

	public void setExample(Language example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Language> root = countCriteria.from(Language.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Language> criteria = builder.createQuery(Language.class);
		root = criteria.from(Language.class);
		TypedQuery<Language> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Language> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("name")),
					'%' + name.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Language> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Language entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Language> getAll() {

		CriteriaQuery<Language> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Language.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Language.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final LanguageBean ejbProxy = this.sessionContext
				.getBusinessObject(LanguageBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Language) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Language add = new Language();

	public Language getAdd() {
		return this.add;
	}

	public Language getAdded() {
		Language added = this.add;
		this.add = new Language();
		return added;
	}
}
