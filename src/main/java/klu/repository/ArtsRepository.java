package klu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import klu.entitiy.Arts;

@Repository
public interface ArtsRepository extends JpaRepository<Arts, Long> {
	 @Query("SELECT a FROM Arts a WHERE LOWER(a.artTitle) LIKE LOWER(CONCAT('%', :query, '%')) " +
	           "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
	           "OR LOWER(a.category) LIKE LOWER(CONCAT('%', :query, '%'))")
	    List<Arts> searchByQuery(String query);
	    // Alternatively, if using Spring Data's method naming conventions:
	    List<Arts> findAllByArtTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategoryContainingIgnoreCase(
	        String artTitle, String description, String category);
	    
	    @Transactional
	    @Modifying
	    @Query("DELETE FROM Wishlist w WHERE w.art.id = :artId")
	    void deleteByArtId(@Param("artId") Long artId);
	
}
