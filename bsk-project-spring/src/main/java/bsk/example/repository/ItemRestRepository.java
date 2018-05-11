package bsk.example.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import bsk.example.domain.Item;

@RepositoryRestResource(collectionResourceRel = "items", path = "items")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public interface ItemRestRepository extends CrudRepository<Item, Long>{

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	<S extends Item> S save(S item);
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	<S extends Item> Iterable<S> saveAll(Iterable<S> items);
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	void delete(Item item);
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	void deleteAll();
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	void deleteAll(Iterable<? extends Item> items);
}