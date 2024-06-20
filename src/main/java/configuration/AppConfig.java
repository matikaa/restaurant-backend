package configuration;

import com.restaurant.cart.repository.delivered.CartDeliveredJpaRepository;
import com.restaurant.cart.repository.delivered.CartDeliveredRepository;
import com.restaurant.cart.repository.delivered.JpaWrappedCartDeliveredRepository;
import com.restaurant.cart.service.delivered.BaseCartDeliveredService;
import com.restaurant.cart.service.delivered.CartDeliveredService;
import com.restaurant.category.repository.CategoryJpaRepository;
import com.restaurant.category.repository.CategoryRepository;
import com.restaurant.category.repository.JpaWrappedCategoryRepository;
import com.restaurant.category.service.BaseCategoryService;
import com.restaurant.category.service.CategoryService;
import com.restaurant.contact.repository.ContactJpaRepository;
import com.restaurant.contact.repository.ContactRepository;
import com.restaurant.contact.repository.JpaWrappedContactRepository;
import com.restaurant.contact.service.BaseContactService;
import com.restaurant.contact.service.ContactService;
import com.restaurant.food.repository.FoodJpaRepository;
import com.restaurant.food.repository.FoodRepository;
import com.restaurant.food.repository.JpaWrappedFoodRepository;
import com.restaurant.food.service.BaseFoodService;
import com.restaurant.food.service.FoodService;
import com.restaurant.jwt.repository.JpaWrappedJwtRepository;
import com.restaurant.jwt.repository.JwtJpaRepository;
import com.restaurant.jwt.repository.JwtRepository;
import com.restaurant.jwt.service.BaseJwtService;
import com.restaurant.jwt.service.JwtService;
import com.restaurant.cart.repository.current.JpaWrappedCartRepository;
import com.restaurant.cart.repository.current.CartJpaRepository;
import com.restaurant.cart.repository.current.CartRepository;
import com.restaurant.cart.service.current.BaseCartService;
import com.restaurant.cart.service.current.CartService;
import com.restaurant.user.repository.JpaWrappedUserRepository;
import com.restaurant.user.repository.UserJpaRepository;
import com.restaurant.user.repository.UserRepository;
import com.restaurant.user.service.BaseUserService;
import com.restaurant.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public CategoryService categoryServiceInterface(CategoryJpaRepository categoryJpaRepository) {
        return new BaseCategoryService(categoryRepositoryInterface(categoryJpaRepository));
    }

    private CategoryRepository categoryRepositoryInterface(CategoryJpaRepository categoryJpaRepository) {
        return new JpaWrappedCategoryRepository(categoryJpaRepository);
    }

    @Bean
    public ContactService contactServiceInterface(ContactJpaRepository contactJpaRepository) {
        return new BaseContactService(contactRepositoryInterface(contactJpaRepository));
    }

    private ContactRepository contactRepositoryInterface(ContactJpaRepository contactJpaRepository) {
        return new JpaWrappedContactRepository(contactJpaRepository);
    }

    @Bean
    public FoodService foodServiceInterface(FoodJpaRepository foodJpaRepository) {
        return new BaseFoodService(foodRepositoryInterface(foodJpaRepository));
    }

    private FoodRepository foodRepositoryInterface(FoodJpaRepository foodJpaRepository) {
        return new JpaWrappedFoodRepository(foodJpaRepository);
    }

    @Bean
    public UserService userServiceInterface(UserJpaRepository userJpaRepository,
                                            JwtService jwtService,
                                            PasswordEncoder passwordEncoder,
                                            AuthenticationManager authenticationManager) {
        return new BaseUserService(userRepositoryInterface(userJpaRepository),
                jwtService, passwordEncoder, authenticationManager);
    }

    private UserRepository userRepositoryInterface(UserJpaRepository userJpaRepository) {
        return new JpaWrappedUserRepository(userJpaRepository);
    }

    @Bean
    public BaseJwtService jwtService(JwtRepository jwtRepository, @Value("${authentication.secret}") String jwtSecret) {
        return new BaseJwtService(jwtRepository, jwtSecret);
    }

    @Bean
    public JwtRepository jwtRepository(JwtJpaRepository jwtJpaRepository) {
        return new JpaWrappedJwtRepository(jwtJpaRepository);
    }

    @Bean
    public CartDeliveredService cartDeliveredServiceInterface(CartDeliveredJpaRepository cartDeliveredJpaRepository,
                                                              FoodService foodService, CategoryService categoryService) {
        return new BaseCartDeliveredService(cartDeliveredRepositoryInterface(cartDeliveredJpaRepository),
                foodService, categoryService);
    }

    private CartDeliveredRepository cartDeliveredRepositoryInterface(
            CartDeliveredJpaRepository cartDeliveredJpaRepository) {
        return new JpaWrappedCartDeliveredRepository(cartDeliveredJpaRepository);
    }

    @Bean
    public CartService cartServiceInterface(CartJpaRepository cartJpaRepository,
                                            CartDeliveredService cartDeliveredService) {
        return new BaseCartService(cartRepositoryInterface(cartJpaRepository), cartDeliveredService);
    }

    private CartRepository cartRepositoryInterface(CartJpaRepository cartJpaRepository) {
        return new JpaWrappedCartRepository(cartJpaRepository);
    }
}
