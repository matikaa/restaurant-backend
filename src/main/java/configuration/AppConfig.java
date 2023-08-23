package configuration;

import com.restaurant.app.category.repository.CategoryJpaRepository;
import com.restaurant.app.category.repository.CategoryRepository;
import com.restaurant.app.category.repository.JpaWrappedCategoryRepository;
import com.restaurant.app.category.service.BaseCategoryService;
import com.restaurant.app.category.service.CategoryService;
import com.restaurant.app.contact.repository.ContactJpaRepository;
import com.restaurant.app.contact.repository.ContactRepository;
import com.restaurant.app.contact.repository.JpaWrappedContactRepository;
import com.restaurant.app.contact.service.BaseContactService;
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

    public JpaWrappedContactRepository jpaWrappedContactRepository(ContactJpaRepository contactJpaRepository){
        return new JpaWrappedContactRepository(contactJpaRepository);
    }

    @Bean
    public BaseContactService baseContactService(ContactRepository contactRepository){
        return new BaseContactService(contactRepository);
    }
}
