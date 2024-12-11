package klu.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import klu.entitiy.Arts;
import klu.service.ArtsService;

@RestController
@RequestMapping("/arts")
public class ArtsController {

    @Autowired
    private ArtsService artsService;


    @Value("${upload.directory}")
    private String uploadDir;

    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptionsRequest() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Arts>> getAllArts() {
        List<Arts> artsList = artsService.getAllArts();
        return ResponseEntity.ok(artsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Arts> getArtById(@PathVariable Long id) {
        Arts art = artsService.getArtById(id);
        if (art != null) {
            return ResponseEntity.ok(art);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<Arts> artsList = artsService.getAllArts();
        List<String> categories = artsList.stream().map(Arts::getCategory).distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }



    private static final String ARTS_SUBDIRECTORY = "arts";

    @PostMapping("/add")
    public ResponseEntity<?> addArt(
            @RequestParam(value = "artTitle") String artTitle,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "category") String category,
            @RequestParam(value = "price") double price,
            @RequestParam(value = "file1", required = false) MultipartFile file1,
            @RequestParam(value = "file2", required = false) MultipartFile file2,
            @RequestParam(value = "file3", required = false) MultipartFile file3,
            @RequestParam(value = "file4", required = false) MultipartFile file4) {

        try {
            String filePath1 = saveFile(file1);
            String filePath2 = saveFile(file2);
            String filePath3 = saveFile(file3);
            String filePath4 = saveFile(file4);

            Arts art = new Arts();
            art.setArtTitle(artTitle);
            art.setDescription(description);
            art.setCategory(category);
            art.setPrice(price);
            art.setPictureUrl1(filePath1 != null ? "/uploads/arts/" + filePath1 : null);
            art.setPictureUrl2(filePath2 != null ? "/uploads/arts/" + filePath2 : null);
            art.setPictureUrl3(filePath3 != null ? "/uploads/arts/" + filePath3 : null);
            art.setPictureUrl4(filePath4 != null ? "/uploads/arts/" + filePath4 : null);

            Arts savedArt = artsService.addArt(art);
            return ResponseEntity.ok(savedArt);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null; // No file uploaded
        }

        String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir, ARTS_SUBDIRECTORY);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // Create arts subdirectory if not exists
        }

        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        return filename;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateArt(
            @PathVariable Long id,
            @RequestParam("artTitle") String artTitle,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") double price,
            @RequestParam(value = "file1", required = false) MultipartFile file1,
            @RequestParam(value = "file2", required = false) MultipartFile file2,
            @RequestParam(value = "file3", required = false) MultipartFile file3,
            @RequestParam(value = "file4", required = false) MultipartFile file4) {

        try {
            Arts existingArt = artsService.getArtById(id);
            if (existingArt == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Art not found");
            }

            existingArt.setArtTitle(artTitle);
            existingArt.setDescription(description);
            existingArt.setCategory(category);
            existingArt.setPrice(price);

            if (file1 != null && !file1.isEmpty()) {
                existingArt.setPictureUrl1("/uploads/" + saveFile(file1));
            }
            if (file2 != null && !file2.isEmpty()) {
                existingArt.setPictureUrl2("/uploads/" + saveFile(file2));
            }
            if (file3 != null && !file3.isEmpty()) {
                existingArt.setPictureUrl3("/uploads/" + saveFile(file3));
            }
            if (file4 != null && !file4.isEmpty()) {
                existingArt.setPictureUrl4("/uploads/" + saveFile(file4));
            }

            Arts updatedArt = artsService.updateArt(existingArt);
            return ResponseEntity.ok(updatedArt);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating art");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArt(@PathVariable Long id) {
        Arts existingArt = artsService.getArtById(id);
        if (existingArt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Art not found");
        }

        try {
            artsService.deleteArtWithDependencies(id);
            return ResponseEntity.ok("Art deleted successfully");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error deleting art: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Arts>> searchArts(@RequestParam String query) {
        List<Arts> searchResults = artsService.searchArts(query);
        return ResponseEntity.ok(searchResults);
    }
}
