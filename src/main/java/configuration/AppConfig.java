package configuration;

import com.restaurant.app.category.repository.CategoryJpaRepository;
import com.restaurant.app.category.repository.CategoryRepository;
import com.restaurant.app.category.repository.JpaWrappedCategoryRepository;
import com.restaurant.app.category.service.BaseCategoryService;
import com.restaurant.app.category.service.CategoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CategoryService categoryServiceInterface(CategoryJpaRepository categoryJpaRepository) {
        return new BaseCategoryService(categoryRepositoryInterface(categoryJpaRepository));
    }

    private CategoryRepository categoryRepositoryInterface(CategoryJpaRepository categoryJpaRepository) {
        return new JpaWrappedCategoryRepository(categoryJpaRepository);
    }
}
