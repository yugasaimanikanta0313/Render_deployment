package klu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import klu.entitiy.Arts;
import klu.repository.ArtsRepository;
import klu.repository.WishlistRepository;


@Service
public class ArtsService {

    @Autowired
    private ArtsRepository artsRepository;
    @Autowired
    private WishlistRepository wishlistRepository;

    public Arts addArt(Arts art) {
        return artsRepository.save(art);
    }

    public List<Arts> getAllArts() {
        return artsRepository.findAll();
    }

    public Arts getArtById(Long id) {
        return artsRepository.findById(id).orElse(null);
    }

    public void deleteArt(Long id) {
        artsRepository.deleteById(id);
    }
    public Arts updateArt(Arts existingArt) {
        return artsRepository.save(existingArt);
    }
//    public void deleteWishlistsByArtId(Long artId) {
//        wishlistRepository.deleteByArtId(artId); // Implement this in your repository
//    }
    @Transactional
    public void deleteArtWithDependencies(Long artId) {
        // First, delete associated wishlist entries
        wishlistRepository.deleteByArtId(artId);

        // Then, delete the art itself
        artsRepository.deleteById(artId);
    }

    public List<Arts> searchArts(String query) {
        return artsRepository.findAllByArtTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategoryContainingIgnoreCase(query, query, query);
    }

}
