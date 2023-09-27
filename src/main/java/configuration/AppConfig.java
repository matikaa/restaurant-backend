package configuration;

import com.restaurant.app.cart.repository.delivered.CartDeliveredJpaRepository;
import com.restaurant.app.cart.repository.delivered.CartDeliveredRepository;
import com.restaurant.app.cart.repository.delivered.JpaWrappedCartDeliveredRepository;
import com.restaurant.app.cart.service.delivered.BaseCartDeliveredService;
import com.restaurant.app.cart.service.delivered.CartDeliveredService;
import com.restaurant.app.category.repository.CategoryJpaRepository;
import com.restaurant.app.category.repository.CategoryRepository;
import com.restaurant.app.category.repository.JpaWrappedCategoryRepository;
import com.restaurant.app.category.service.BaseCategoryService;
import com.restaurant.app.category.service.CategoryService;
import com.restaurant.app.contact.repository.ContactJpaRepository;
import com.restaurant.app.contact.repository.ContactRepository;
import com.restaurant.app.contact.repository.JpaWrappedContactRepository;
import com.restaurant.app.contact.service.BaseContactService;
import com.restaurant.app.contact.service.ContactService;
import com.restaurant.app.food.repository.FoodJpaRepository;
import com.restaurant.app.food.repository.FoodRepository;
import com.restaurant.app.food.repository.JpaWrappedFoodRepository;
import com.restaurant.app.food.service.BaseFoodService;
import com.restaurant.app.food.service.FoodService;
import com.restaurant.app.jwt.repository.JpaWrappedJwtRepository;
import com.restaurant.app.jwt.repository.JwtJpaRepository;
import com.restaurant.app.jwt.repository.JwtRepository;
import com.restaurant.app.jwt.service.BaseJwtService;
import com.restaurant.app.jwt.service.JwtService;
import com.restaurant.app.cart.repository.current.JpaWrappedCartRepository;
import com.restaurant.app.cart.repository.current.CartJpaRepository;
import com.restaurant.app.cart.repository.current.CartRepository;
import com.restaurant.app.cart.service.current.BaseCartService;
import com.restaurant.app.cart.service.current.CartService;
import com.restaurant.app.user.repository.JpaWrappedUserRepository;
import com.restaurant.app.user.repository.UserJpaRepository;
import com.restaurant.app.user.repository.UserRepository;
import com.restaurant.app.user.service.BaseUserService;
import com.restaurant.app.user.service.UserService;
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
    public CartDeliveredService cartDeliveredServiceInterface(CartDeliveredJpaRepository cartDeliveredJpaRepository) {
        return new BaseCartDeliveredService(cartDeliveredRepositoryInterface(cartDeliveredJpaRepository));
    }

    private CartDeliveredRepository cartDeliveredRepositoryInterface(
            CartDeliveredJpaRepository cartDeliveredJpaRepository) {
        return new JpaWrappedCartDeliveredRepository(cartDeliveredJpaRepository);
    }

    @Bean
    public CartService cartServiceInterface(CartJpaRepository cartJpaRepository, CartDeliveredService cartDeliveredService) {
        return new BaseCartService(cartRepositoryInterface(cartJpaRepository), cartDeliveredService);
    }

    private CartRepository cartRepositoryInterface(CartJpaRepository cartJpaRepository) {
        return new JpaWrappedCartRepository(cartJpaRepository);
    }
}
