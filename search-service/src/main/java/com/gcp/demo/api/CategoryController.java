package com.gcp.demo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.demo.Constants;
import com.gcp.demo.model.Category;
import com.gcp.demo.model.FilterOptions;
import com.gcp.demo.model.SearchResult;
import com.gcp.demo.service.SearchService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {

    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private SearchService service;

    @GetMapping({"/categories", "/categories/{categoryId}/subcategories"})
    @SneakyThrows
    public List<Category> getCategories(@PathVariable String categoryId) {
        return service.getCategories(categoryId);
    }

    @GetMapping({"/categories/{categoryId}/search", "/categories/{categoryId}/subcategories/{subCategoryId}/search"})
    @SneakyThrows
    public SearchResult getCategoryResults(@PathVariable String categoryId, @PathVariable(required = false) String subCategoryId,
                                            @RequestParam(required = false) String filterOptions, String sortBy) {
        return service.getCategoryResults(categoryId, subCategoryId, getFilterOptions(Optional.ofNullable(filterOptions)), Constants.toSortBy(sortBy));
    }

    @SneakyThrows
    private Optional<FilterOptions> getFilterOptions(Optional<String> filterOptions) throws IOException {
        if (!filterOptions.isPresent())
            return Optional.empty();
        return Optional.of(mapper.readValue(filterOptions.get(), FilterOptions.class));
    }

}
